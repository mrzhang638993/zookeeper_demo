package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * union会将数据进行合并操作，但是合并的时候对应的数据是不会去掉的
 * hadoop
 * hadoop
 * hive
 * hive
 * flume
 * spark
 **/
object UnionTrans {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val valueDataset: DataSet[String] = env.fromCollection(List("hadoop", "hive", "flume"))
    val valueDataset1: DataSet[String] = env.fromCollection(List("hadoop", "hive", "spark"))
    val unionValue: DataSet[String] = valueDataset.union(valueDataset1)
    // 去重操作实现
    //unionValue.print()
    //根据元素属性的位置进行去重操作实现的话，只适用于元组进行操作的。
    val distinctValue: DataSet[String] = unionValue.distinct()
    distinctValue.print()
  }
}
