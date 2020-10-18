package com.itheima.env

import org.apache.flink.api.scala._

/**
 * flink程序发送到远程服务器进行服务使用
 **/
object BatchRemoteEnv {

  def main(args: Array[String]): Unit = {
    //  创建集群环境操作使用
    // host: String, port: Int, jarFiles: String*
    val env: ExecutionEnvironment = ExecutionEnvironment.createRemoteEnvironment("cdh1", 8081)
    // 读取csv文件的内容
    val flinkDatas: DataSet[(Long, String, Long, Float)] = env.readCsvFile[(Long, String, Long, Float)]("F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\src\\main\\scala\\dataset\\score.csv")
    // 根据原子的姓名进行分组操作，根据成绩降序排列操作实现
    flinkDatas.print()
  }
}
