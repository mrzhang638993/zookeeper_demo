package com.itheima.env

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * 对应的实现本地操作环境的特性
 **/
object BatchLocalEven {

  def main(args: Array[String]): Unit = {
    // 创建flink的本地运行环境. 设置并行度进行操作实现
    //val env: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment()
    //env.setParallelism(2)
    val env: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(2)
    //  读取数据
    val sourceValue: DataSet[Int] = env.fromCollection(List(1, 2, 3, 4))
    sourceValue.print()
  }
}
