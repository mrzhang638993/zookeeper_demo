package com.itheima.stream.trans

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 测试socket连接,key——by进行分组操作实现
 **/
object Trans_KeyBy {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    // 创建连接操作实现
    /* val sourceValue: DataStream[String] = env.socketTextStream("cdh1", 9999)
     val turpleValue: DataStream[(String, Int)] = sourceValue.flatMap(item => {
       item.split(" ")
     }).map((_, 1)).keyBy(1).sum(1)
     turpleValue.print()*/
    //
    val sourceStream: DataStream[String] = env.socketTextStream("cdh1", 9999)
    val destValue: DataStream[(String, Int)] = sourceStream.flatMap(item => {
      item.split(" ").map((_, 1))
    })
    destValue.print()
    env.execute("job")
  }
}
