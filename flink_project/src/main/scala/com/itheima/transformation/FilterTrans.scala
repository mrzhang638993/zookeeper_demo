package com.itheima.transformation

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

/**
 * filter算子执行操作实现
 **/
object FilterTrans {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 读取数据，执行过滤操作
    val sourceStr: DataSet[String] = env.fromCollection(List("hadoop", "hive", "spark", "flink"))
    // 打印输出操作结果和实现
    val filterStr: DataSet[String] = sourceStr.filter(str => str.startsWith("h"))
    filterStr.print()
  }
}
