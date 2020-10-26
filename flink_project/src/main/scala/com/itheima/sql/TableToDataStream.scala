package com.itheima.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.api.scala._


/**
 *  table转换成为dataStream数据类型
 * */
object TableToDataStream {

  def main(args: Array[String]): Unit = {
    // 获取流处理环境执行操作实现
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //  设置并行度
    env.setParallelism(1)
    //  获取table的运行环境
    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //  执行运行环境的操作实现
    val sourceData: DataStream[(Long, Int, String)] = env.fromCollection(List(
      (1L, 1, "Hello"),
      (2L, 2, "Hello"),
      (6L, 6, "Hello"),
      (7L, 7, "Hello World"),
      (8L, 8, "Hello World"),
      (20L, 20, "Hello World"))
    )
    //  将dataStream转换成为table
    val table: Table = tableEnv.fromDataStream(sourceData)
    // 将table转化成为dataStream进行操作实现.将table转化成为元祖的数据执行操作
    tableEnv.toAppendStream[(Long,Int,String)](table)
    //  执行相关的流处理操作实现。Boolean为true的时候代表的是增加，false代表的是删除操作
    val retractStream: DataStream[(Boolean, (Long, Int, String))] = tableEnv.toRetractStream[(Long, Int, String)](table)
    // 打印输出
    retractStream.print()
    //  开始执行操作
    env.execute()
  }
}
