package com.itcast.spark.sparktest

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object RDDPrograming {
  def main(args: Array[String]): Unit = {
    //对应的完成相关的rdd的编程指导和实现操作的。加深相关的rdd的编程实现理解的。
    //统计单词的格式信息？
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("sparkLearning")
    //SparkContext执行操作实现
    val context = new SparkContext(conf)
    val accumulator: LongAccumulator = context.longAccumulator("test")
    val ints = List(1, 2, 3, 4, 5)
    val value: RDD[Int] = context.parallelize(ints)
    //执行了一次对应的action操作实现
    println(value.count())
    //对应的完成累加操作实现机制的。
    value.foreach(accumulator.add(_))
    println(accumulator.value)
    //可以模仿对应的累加器的逻辑来实现更多的。
  }
}