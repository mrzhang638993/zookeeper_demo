package com.itheima.sink

import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.core.fs.FileSystem

/**
 * map数据的落地
 **/
object MapToSink {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 设置并行第
    env.setParallelism(2)
    //  数据的读取操作实现
    val mapValue: DataSet[(Int, String)] = env.fromCollection(Map(1 -> "spark", 2 -> "flink"))
    //  实现数据的落地操作实现
    mapValue.writeAsText("./dataset/mapToSink/", FileSystem.WriteMode.OVERWRITE)
    // 上面的操作并没有真正的输出的。
    env.execute("mapToSink")
  }
}
