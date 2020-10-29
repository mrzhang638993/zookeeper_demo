package com.itheima.sql

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.table.api.scala.{BatchTableEnvironment}

/**
 * 完成大数据作业3
 *
 * 本次答案的适用场景是批处理操作的。不要使用流处理操作的。
 * */
object Exec3 {
  def main(args: Array[String]): Unit = {
    //  获取流环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    //  获取table执行环境
    val tableEnv: BatchTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //  读取数据
    val sourceValue: DataSet[String] = env.readTextFile("E:\\idea_works\\java\\zookeeper_demo\\flink_project\\src\\main\\scala\\com\\itheima\\logs")
    //  转换成为对应的case class
    import org.apache.flink.api.scala._
    // 流式数据不断的产生和增加中的，如何进行统计操作实现的？如何维护一个共同的数据的。需要进行累计求和操作实现的
    val myNetwork: DataSet[Network] = sourceValue.map {
      it => {
        val arr: Array[String] = it.split(",")
        Network(arr(0), arr(1), arr(2).toInt)
      }
    }.groupBy("name").reduce((priv,next)=>Network(priv.name,priv.gender,priv.RetainTime+next.RetainTime))
    import org.apache.flink.table.api.scala._
    val tableSink: Table = tableEnv.fromDataSet(myNetwork)
    val finaltable: Table = tableSink.select('name, 'gender, 'RetainTime)
      .where('RetainTime >= 120)
    val value: DataSet[Network] = tableEnv.toDataSet[Network](finaltable)
    value.writeAsText("./dataset/exec2",WriteMode.OVERWRITE)
    env.execute()
  }
}

case class Network(name:String,gender:String,RetainTime:Int)
