package com.itheima.batch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.configuration.Configuration

object BatchFromFolder {
  /**
   * 便利目录数据
   **/
  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 读取数据,执行操作.递归读取文件夹数据
    val params: Configuration = new Configuration()
    // 设置递归读取参数
    params.setBoolean("recursive.file.enumeration", true)
    val folderDataset: DataSet[String] = env.readTextFile("F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\src\\main\\scala\\dataset").withParameters(params)
    folderDataset.print()
  }
}
