package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * 执行hashPartition分区操作实现。根据指定的字段进行分区操作
 **/
object PartitionHashTrans {

  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 执行hash分区操作实现
    //val sourceValue: DataSet[Int] = env.fromCollection(List(1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2))
    val sourceValue: DataSet[(String, Int)] = env.fromCollection(List(("flink", 1), ("spark", 2), ("flume", 3)))
    // 设置并行度为2。
    env.setParallelism(2)
    // 进行数据分区操作shi.根据指定的key进行分区操作实现
    //val partitionValue: DataSet[Int] = sourceValue.partitionByHash(_.toString)
    //  不能使用_来代替的，turple对应的是根据元组的字段进行分区操作的。
    val partitionValue: DataSet[(String, Int)] = sourceValue.partitionByHash(0)
    partitionValue.writeAsText("./dataset/partitions")
    partitionValue.print()
  }
}
