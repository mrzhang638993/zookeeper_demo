package cn.itcast.spark.rdd

import junit.framework.Test
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit

class Exec2 {

  private val exec: SparkConf = new SparkConf().setMaster("local").setAppName("exec2")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")

  @junit.Test
  def  testCache(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5))
    //  rdd数据的持久化，默认是内存中的
    value.cache()
    context.stop()
  }

  /**
   * 下面测试使用checkPoint的代码
   * */
  @junit.Test
  def testCheckPoint(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5))
    //  手动的将当前的rdd创建一个checkpoint便于后续的回复和查找机制的。
    value.checkpoint()
    context.stop()
  }


}
