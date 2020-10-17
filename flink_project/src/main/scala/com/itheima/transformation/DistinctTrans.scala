package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * 去重操作实现管理
 * distinct 去重操作实现
 **/
object DistinctTrans {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val destValue: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))
    //根据指定的字段进行去重操作实现和管理操作。
    val distinctValue: DataSet[(String, Int)] = destValue.distinct(0)
    distinctValue.print()
  }
}
