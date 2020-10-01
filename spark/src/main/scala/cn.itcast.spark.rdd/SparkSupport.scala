package cn.itcast.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class SparkSupport {

  private val exec: SparkConf = new SparkConf().setMaster("local").setAppName("exec2")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")

  /**
   * 测试spark对于数值型的支持操作
   * variance  求解方差
   * sampleVariance  从采样中求解方差
   * stdev   求解标准差
   * sampleStdev   从采用中求解标准差
   * */
  @Test
  def numeric(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 10, 20, 30, 50, 100),numSlices = 2)
    println("求解最大值"+value.max())
    println("求解最小值"+value.min())
    println("求解平均值"+value.mean())
    println("求解总和"+value.sum())
    context.stop()
  }

  /**
   * 对于kv类型的数据的支持
   * 聚合操作：reduceByKey foldByKey combineByKey
   * 分组操作：cogroup groupByKey
   * 连接操作：join leftOuterJoin rightOuterJoin
   * 排序操作： sortBy sortByKey
   * action操作： countByKey take collect
   * */


}
