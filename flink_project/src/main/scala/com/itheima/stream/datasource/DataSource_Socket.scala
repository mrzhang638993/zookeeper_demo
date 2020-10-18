package com.itheima.stream.datasource

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 使用socket读取文本数据
 **/
object DataSource_Socket {

  def main(args: Array[String]): Unit = {
    // 创建流的处理环境信息
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    // 读取数据执行操作
    val sourceStream: DataStream[String] = env.socketTextStream("cdh1", 9999)
    // 数据转换操作
    val mapDataStream: DataStream[String] = sourceStream.flatMap { line => line.split(" ") }
    // 数据打印操作
    mapDataStream.print()
    env.execute("socket_job")
  }
}
