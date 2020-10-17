package com.itheima.transformation

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * sortPartition分区操作实现
 * 将分区的数据进行排序操作之后输出到文件中
 * 这个的使用是很关键的，后续的很多的输出的操作都是可以输出到对应的文件中进行操作实现的。
 **/
object SortPartitionTrans {
  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val sourceValue: DataSet[String] = env.fromCollection(List("hadoop", "hadoop", "hadoop", "hive", "hive", "spark", "spark", "flink"))
    // 设置并行度
    env.setParallelism(2)
    // 对应的适配相关的数据执行操作实现.根据索引操作的话,只能够是turple顺序的。单个的字符串是不能使用这个的。
    val sortValue: DataSet[String] = sourceValue.sortPartition(x => x, Order.ASCENDING)
    sortValue.writeAsText("./dataset/sortOutPut")
    sortValue.print()
  }
}
