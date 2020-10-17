package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * mapPartitions操作和map操作类似，map操作的是单个的记录的，
 * mapPartitions操作的是一整个分区的数据的。
 **/
object TestMapPartition {

  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val listDataset: DataSet[String] = env.fromCollection(List("1,张三", "2,李四", "3,王五", "4,赵六"))
    //  分区操作，默认的是hash分区操作的
    val persons: DataSet[Persons] = listDataset.mapPartition {
      // map操作对应的是单个的元素的，mapPartition遍历迭代的时候对应的iterable对象的。处理的数据量相对而言更加的高的。
      iterable => {
        // 可以在这个数据中开启redis操作的，开启的过程中对应的创建的连接的数量较少的。
        iterable.map {
          text => {
            val arrs: Array[String] = text.split(",")
            Persons(arrs(0), arrs(1))
          }
        }
      }
    }
    persons.print()
  }
}

case class Persons(id: String, name: String)
