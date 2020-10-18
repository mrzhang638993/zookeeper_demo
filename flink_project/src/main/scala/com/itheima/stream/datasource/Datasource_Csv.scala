package com.itheima.stream.datasource

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 使用csv文件进行数据分析操作实现
 **/
object Datasource_Csv {

  def main(args: Array[String]): Unit = {
    // 创建流的处理环境信息
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    // 读取数据
    val sourceValue: DataStream[String] = env.readTextFile("hdfs://cdh1:8020/flink_datas/score.csv")
    sourceValue.print()
    env.execute("csv_source")
  }
}
