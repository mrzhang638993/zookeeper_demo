package com.itheima.broad

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem

/**
 * flink累加器执行操作实现
 * flink程序操作的话。至少需要一次sink操作实现的
 **/
object CounterDemo {
  /**
   * 需要统计整体的执行任务的过程中，每一个元素对应的个数信息
   **/
  def main(args: Array[String]): Unit = {
    //  定义执行环境和相关的操作实现
    val env: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(2)
    //  加载本地集合。
    val sourceDataset: DataSet[String] = env.fromCollection(List("a", "b", "c", "d"))
    // map转换操作.
    val countValue: DataSet[String] = sourceDataset.map(new RichMapFunction[String, String] {
      val intCount = new IntCounter()

      override def map(value: String): String = {
        // 参数1：累加器的名称，参数二：对应的是累加器的操作的
        intCount.add(1)
        value
      }

      override def open(parameters: Configuration): Unit = {
        // 全局只会执行一次的操作，注册累加器实现相关的操作实现
        getRuntimeContext.addAccumulator("int_counter", intCount)
      }
    })
    // 可以执行数据的输出
    countValue.writeAsText("./dataset/counter", FileSystem.WriteMode.OVERWRITE)
    // 执行任务
    val result: JobExecutionResult = env.execute("int_counter")
    //  获取累加器的执行结果
    val accumulatorResult: Int = result.getAccumulatorResult[Int]("int_counter")
    // 获取到累加器的执行结果
    println(accumulatorResult)
  }
}
