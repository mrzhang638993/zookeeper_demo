package com.itheima.sql

import java.util.Date

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment

/**
 * 完成订单数据的统计操作实现
 * */
object Exec2 {

  def main(args: Array[String]): Unit = {
    // 获取流执行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //  获取表的执行环境
    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    // 对应的完成数据的加载和操作实现
    import org.apache.flink.api.scala._
    // 获取原始数据
    val sourceValue: DataStream[MyOrder] = env.fromCollection(List(
      MyOrder(1, "zhangsan", "2018-10-20 15:30", 358.5),
      MyOrder(2, "zhangsan", "2018-10-20 16:30", 131.5),
      MyOrder(3, "lisi", "2018-10-20 16:30", 127.5),
      MyOrder(4, "lisi", "2018-10-20 16:30", 328.5),
      MyOrder(5, "lisi", "2018-10-20 16:30", 432.5),
      MyOrder(6, "zhaoliu", "2018-10-20 22:30", 451.0),
      MyOrder(7, "zhaoliu", "2018-10-20 22:30", 362.0),
      MyOrder(8, "zhaoliu", "2018-10-20 22:30", 364.0),
      MyOrder(9, "zhaoliu", "2018-10-20 22:30", 341.0)
    ))
    // 注册为表
    import org.apache.flink.table.api.scala._
    tableEnv.registerDataStream("t_order",sourceValue,'id,'name,'orderDate,'money)
    //  执行sql查询操作实现
    // count输出的结果是Long类型的数据
    val table: Table = tableEnv.sqlQuery("select id ,sum(money) as allMoney,max(money) as maxMoney ,min(money) as minMoney,count(1) as total from t_order group by id ")
    val value: DataStream[(Boolean, (Int,Double,Double,Double,Long))] = table.toRetractStream[(Int,Double,Double,Double,Long)]
    val destValue: DataStream[(Int,Double,Double,Double,Long)] = value.map(it => it._2)
    destValue.print()
    env.execute()
  }
}


case class MyOrder(id:Int,name:String,orderDate:String,money:Double)