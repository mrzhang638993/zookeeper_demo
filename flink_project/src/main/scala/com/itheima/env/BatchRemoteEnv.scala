package com.itheima.env

import org.apache.flink.api.scala._

/**
 * flink程序发送到远程服务器进行服务使用
 **/
object BatchRemoteEnv {

  def main(args: Array[String]): Unit = {
    //  创建集群环境操作使用
    // host: String, port: Int, jarFiles: String*
    val env: ExecutionEnvironment = ExecutionEnvironment.createRemoteEnvironment("192.168.1.205", 8081)
    // 读取csv文件的内容
    val flinkDatas: DataSet[Score] = env.readCsvFile[Score]("hdfs://cdh1:8020/flink_datas/score.csv")
    // 根据原子的姓名进行分组操作，根据成绩降序排列操作实现
    flinkDatas.print()
  }
}

case class Score(id: Int, name: String, chid: Int, score: Float)
