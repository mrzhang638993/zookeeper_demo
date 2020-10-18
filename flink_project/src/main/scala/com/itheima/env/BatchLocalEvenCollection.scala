package com.itheima.env

import java.util.Date

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

object BatchLocalEvenCollection {
  def main(args: Array[String]): Unit = {
    // 创建flink的本地运行环境. 使用Java的jvm的操作实现
    val start = new Date().getTime
    //  在开发和体哦啊是模式的情况下是很好的。单线程的，基于java的collection操作的
    val env: ExecutionEnvironment = ExecutionEnvironment.createCollectionsEnvironment
    //  读取数据
    val sourceValue: DataSet[Int] = env.fromCollection(List(1, 2, 3, 4))
    val end = new Date().getTime
    println((end - start) + "ms")
    sourceValue.print()
  }
}
