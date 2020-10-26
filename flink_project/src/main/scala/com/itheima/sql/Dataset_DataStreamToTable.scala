package com.itheima.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.table.descriptors.FileSystem
import org.apache.flink.table.sinks.CsvTableSink
/**
 * 执行批处理或者是流处理环境执行操作
 * 不要使用sql中的关键字作为sql中的表的，或出现sql语法转换异常的。
 * */
object Dataset_DataStreamToTable {
  def main(args: Array[String]): Unit = {
      // 1.获取流式处理环境
      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
      // 2.获取table处理环境
      val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
      // 3.加载本地集合数据
      val dataStream: DataStream[Order1] = env.fromCollection(List(
        Order1(1, "beer", 3),
        Order1(2, "diaper", 4),
        Order1(3, "rubber", 2)
      ))
    // 4. 转换为表,注册为表结构数据
    tableEnv.registerDataStream("order1",dataStream)
    // 5.执行sql语句
    val table: Table = tableEnv.sqlQuery("select * from order1  where user=1")
    // 6.写入csv文件
    // 打印表结构进行输出操作实现。打印表的输出结构数据和操作实现
    table.printSchema()
    // /**
    //  * A simple [[TableSink]] to emit data as CSV files.
    //  *
    //  * @param path The output path to write the Table to.
    //  * @param fieldDelim The field delimiter
    //  * @param numFiles The number of files to write to
    //  * @param writeMode The write mode to specify whether existing files are overwritten or not.
    //  */
    table.writeToSink(new CsvTableSink("./dataset/score_sql.csv",",",1,WriteMode.OVERWRITE))
    // 7. 执行任务
    env.execute()
  }
}

case  class Order1(user:Long,product:String,amount:Int)
