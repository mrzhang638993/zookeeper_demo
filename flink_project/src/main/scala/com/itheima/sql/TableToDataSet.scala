package com.itheima.sql


import org.apache.flink.api.scala._
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.api.{Table, TableEnvironment}

/**
 *批处理环境下面，println操作是会执行任务的。流式环境下面需要手动的触发操作实现的
 * */
object TableToDataSet {

  def main(args: Array[String]): Unit = {
    // 获取流处理环境执行操作实现
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //  设置并行度
    env.setParallelism(1)
    //  获取table的运行环境
    val tableEnv: BatchTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //  执行运行环境的操作实现
    val sourceData: DataSet[(Long, Int, String)] = env.fromCollection(List(
      (1L, 1, "Hello"),
      (2L, 2, "Hello"),
      (6L, 6, "Hello"),
      (7L, 7, "Hello World"),
      (8L, 8, "Hello World"),
      (20L, 20, "Hello World"))
    )
    //  将dataStream转换成为table
    val table: Table = tableEnv.fromDataSet(sourceData)
    // 将table转化成为dataStream进行操作实现.将table转化成为元祖的数据执行操作
    val result: DataSet[(Long, Int, String)] = tableEnv.toDataSet[(Long, Int, String)](table)
    // table转换为dataset执行操作实现
    result.print()
    //  开始执行操作
    env.execute()
  }
}
