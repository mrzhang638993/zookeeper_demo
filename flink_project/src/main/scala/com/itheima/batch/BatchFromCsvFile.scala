package com.itheima.batch

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object BatchFromCsvFile {
  /**
   * 从csv文件进行读取操作实现
   **/
  def main(args: Array[String]): Unit = {
    //  创建执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 加载csv文件和实现.需要指定文件生成的类型的
    val csvFile: DataSet[Subject] = env.readCsvFile[Subject]("F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\src\\main\\scala\\dataset\\subject.csv")
    csvFile.print()
  }
}

case class Subject(id: Long, name: String)
