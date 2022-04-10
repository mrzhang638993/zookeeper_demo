package com.itcast.spark.sparktest

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//测试spark对于共享变量的操作实现的
object SparkAccumulator {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("sparkLearning")
    //SparkContext执行操作实现
    val context = new SparkContext(conf)
    var count=0;
    val ints = List(1, 2, 3, 4, 5)
    var value: RDD[Int] = context.parallelize(ints)
    //使用累计器完成对应的聚合操作实现。本地的这种聚合模式是存在问题的。对于共享变量的修改推荐使用累加器或者是共享变量实现的。
    value.map(num=>count+=num)
  }
}
