package com.itheima.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
/**
 * 执行批处理或者是流处理环境执行操作
 * */
object Dataset_DataStreamToTable {
  def main(args: Array[String]): Unit = {
      // 1.获取流式处理环境
      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
      // 2.获取table处理环境
      val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
      // 3.加载本地集合数据
      val dataStream: DataStream[Order] = env.fromCollection(List(
        Order(1, "beer", 3),
        Order(2, "diaper", 4),
        Order(3, "rubber", 2)
      ))
    // 4. 转换为表,注册为表结构数据
    tableEnv.registerDataStream("order",dataStream)
    // 5.执行sql语句
    val table: Table = tableEnv.sqlQuery("select * from order")
    // 6.写入csv文件
    // 打印表结构进行输出操作实现
    table.printSchema()
    // 7. 执行任务
    env.execute()
  }
}


case  class Order(user:Long,product:String,amount:Int)
