package com.itheima.batch

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object BatchFromCompressedFile {

  def main(args: Array[String]): Unit = {
    //  创建运行时环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 读取数据
    val value: DataSet[String] = env.readTextFile("F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\src\\main\\scala\\dataset\\wordcount.txt.gz")
    value.print()
  }
}
