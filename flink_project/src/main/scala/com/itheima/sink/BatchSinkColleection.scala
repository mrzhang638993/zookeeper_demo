package com.itheima.sink

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * 执行数据的sink操作
 **/
object BatchSinkColleection {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 执行数据的输出操作实现
    env.setParallelism(2)
    val destValue: DataSet[(Int, String, Double)] = env.fromCollection(List((19, "zhangsan", 178.8),
      (17, "lisi", 168.8),
      (18, "wangwu", 184.8),
      (21, "zhaoliu", 164.8)))
    // 标准输出，打印输出
    destValue.print()
    // 错误输出
    destValue.printToErr()
    // 元组输出
    val tuples: Seq[(Int, String, Double)] = destValue.collect()
    println(tuples)
  }
}
