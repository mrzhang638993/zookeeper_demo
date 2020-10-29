package com.itheima.sql

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment

/**
 * 完成大数据作业3
 * */
object Exec3 {
  def main(args: Array[String]): Unit = {
    //  获取流环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    //  获取table执行环境
    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //  读取数据
    val sourceValue: DataStream[String] = env.readTextFile("E:\\idea_works\\java\\zookeeper_demo\\flink_project\\src\\main\\scala\\com\\itheima\\logs")
    //  转换成为对应的case class
    import org.apache.flink.api.scala._
    val myNetwork: DataStream[(String,String,Int)] = sourceValue.map {
      it => {
        val arr: Array[String] = it.split(",")
        (arr(0), arr(1), arr(2).toInt)
      }
    }.keyBy(1).reduce((priv, next) => (priv._1, priv._2, priv._3 + next._3))
    // 注册为表，进行数据统计操作实现
    import org.apache.flink.table.api.scala._
    tableEnv.registerDataStream("network",myNetwork,'name,'gender,'RetainTime)
    // 执行sql查询操作
    val table: Table = tableEnv.sqlQuery("select  name,gender, RetainTime from network  where RetainTime>=120 ")
    val countValue: DataStream[(Boolean, (String,String,Int))] = table.toRetractStream[(String,String,Int)]
    val destValue: DataStream[(String,String,Int)] = countValue.map(it => it._2)
    destValue.print()
    env.execute()
  }
}

case class Network(name:String,gender:String,RetainTime:Int)
