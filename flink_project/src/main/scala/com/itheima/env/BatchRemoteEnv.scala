package com.itheima.env

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._

/**
 * flink程序发送到远程服务器进行服务使用
 **/
object BatchRemoteEnv {

  def main(args: Array[String]): Unit = {
    //  创建集群环境操作使用
    // host: String, port: Int, jarFiles: String*
    // jar文件拷贝的过程比较的消耗时间的，会导致出现多次的报错信息的。
    val env: ExecutionEnvironment = ExecutionEnvironment.createRemoteEnvironment("cdh1", 8081, "F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\target\\flink_project-1.0-SNAPSHOT.jar")
    // 读取csv文件的内容
    val flinkDatas: DataSet[(Long, String, Long, Float)] = env.readCsvFile[(Long, String, Long, Float)]("hdfs://cdh1:8020/flink_datas/score.csv")
    val descValue: DataSet[(Long, String, Long, Float)] = flinkDatas.groupBy(1).sortGroup(3, Order.DESCENDING).first(1)
    // 根据原子的姓名进行分组操作，根据成绩降序排列操作实现.获取最好的成绩
    descValue.print()
  }
}
