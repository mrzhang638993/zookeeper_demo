package cn.itcast.spark.rdd

import junit.framework.Test
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit


class ActionApi {

  private val exec: SparkConf = new SparkConf().setMaster("local").setAppName("exec2")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")

  @junit.Test
  def  reduceByKey(): Unit ={
    val value: RDD[(String, Double)] = context.parallelize(Seq(("手机", 10.0), ("手机", 15.0), ("电脑", 20.0)))
    //   def reduce(f: (T, T) => T): T = withScope {
    // 计算出来的结果是一个元组的，不是一个reduce的
    val tuple: (String, Double) = value.reduce((curr: (String, Double), agg: (String, Double)) => ("总价", curr._2 + agg._2))
    //=====返回的是整个类型的数据的=====(总价,45.0)
    println("=====返回整个partition的分区数据====="+tuple)
    context.stop()
  }
}
