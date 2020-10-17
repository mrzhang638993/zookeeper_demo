package com.itheima.batch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

object BatchFromFile {
  /**
   * 从文件中读取数据进行操作
   **/
  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //  读取数据
    val txtDataSet: DataSet[String] = env.readTextFile("F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\src\\main\\scala\\dataset\\data.txt")
    // 数据展示操作
    //txtDataSet.print()
    // 加载hdfs文件
    val hdfsText: DataSet[String] = env.readTextFile("hdfs://cdh1:8020/test/input/wordcount.txt.txt")
    hdfsText.print()
  }
}
