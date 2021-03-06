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
    //  数据的读取操作实现
    val mapValue: DataSet[(Int, String)] = env.fromCollection(Map(1 -> "spark", 2 -> "flink"))
    //  实现数据的落地操作实现
    // 设置并行度,如果并行度大于1的话，会输出多个文件的。
    mapValue.setParallelism(2).writeAsText("./dataset/mapToSink/", FileSystem.WriteMode.OVERWRITE)
    // 上面的操作并没有真正的输出的。
    env.execute("mapToSink")
  }
}
