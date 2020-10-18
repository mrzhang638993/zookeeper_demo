package com.itheima.stream.datasource

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * flink流式处理操作实现
 **/
object Datasource_Collection {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置并行度.默认的并行度和cpu的核数是一样的。
    env.setParallelism(1)
    //  导入数据
    val sourceValue: DataStream[Int] = env.fromCollection(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    //  打印数据
    sourceValue.print()
    // 运行任务（在批处理的使用print方法是可以触发任务的，在流环境下面是需要手动的触发任务的）
    env.execute("stream")
  }
}
