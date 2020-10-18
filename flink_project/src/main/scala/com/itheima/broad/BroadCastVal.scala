package com.itheima.broad

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

/**
 * 广播变量定义和执行操作实现
 **/
object BroadCastVal {
  def main(args: Array[String]): Unit = {
    //  定义执行环境和相关的操作实现
    val env: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(2)
    val studentData: DataSet[(Int, String)] = env.fromCollection(List((1, "张三"), (2, "李四"), (3, "王五")))
    val scoreData: DataSet[(Int, String, Int)] = env.fromCollection(List((1, "语文", 50), (2, "数学", 70), (3, "英文", 86)))
    // 广播学生的数据集数据
    val stuDataset: DataSet[(String, String, Int)] = scoreData.map(new RichMapFunction[(Int, String, Int), (String, String, Int)] {
      // 定义成员变量
      var list: List[(Int, String)] = null

      //广播变量和数据操作
      override def map(value: (Int, String, Int)): (String, String, Int) = {
        // 在map方法中需要进行数据的对比
        val studentId: Int = value._1
        val tuples: List[(String, String, Int)] = list.filter(item => item._1.equals(studentId)).map(it => (it._2, value._2, value._3))
        tuples(0)
      }

      /**
       * 重写open方法，对应的open方法会在map方式执行之前执行的，并且只会执行一次。
       **/
      override def open(parameters: Configuration): Unit = {
        import scala.collection.JavaConverters._
        list = getRuntimeContext.getBroadcastVariable[(Int, String)]("stuDataset").asScala.toList
        // 下面需要对数据进行处理操作，将Nothing类型的转换成为真实的业务数据的类型
      }
    }).withBroadcastSet(studentData, "stuDataset")
    stuDataset.print()
  }
}
