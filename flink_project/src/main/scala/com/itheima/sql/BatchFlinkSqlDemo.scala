package com.itheima.sql

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.api.scala._

/**
 * 执行flink的相关的操作实现
 * */
object BatchFlinkSqlDemo {

  def main(args: Array[String]): Unit = {
    // 获取流处理环境执行操作实现
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //  设置并行度
    env.setParallelism(1)
    //  获取table的运行环境
    val tableEnv: BatchTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //  执行操作环境更新管理实践
    val dataset: DataSet[Order11] = env.fromCollection(List(
      Order11(1, "zhangsan", "2018-10-20 15:30", 358.5),
      Order11(2, "zhangsan", "2018-10-20 16:30", 131.5),
      Order11(3, "lisi", "2018-10-20 16:30", 127.5),
      Order11(4, "lisi", "2018-10-20 16:30", 328.5),
      Order11(5, "lisi", "2018-10-20 16:30", 432.5),
      Order11(6, "zhaoliu", "2018-10-20 22:30", 451.0),
      Order11(7, "zhaoliu", "2018-10-20 22:30", 362.0),
      Order11(8, "zhaoliu", "2018-10-20 22:30", 364.0),
      Order11(9, "zhaoliu", "2018-10-20 22:30", 341.0)
    ))
    // 注册操作
    tableEnv.registerDataSet("order1",dataset)
    // 执行sql查询操作
    val table: Table = tableEnv.sqlQuery("select *  from order1")
    table.printSchema()

  }
}
/**
 * 创建订单的样例类数据
 * */
case class  Order11(id:Int,userName:String,createTime:String,money:Double)
