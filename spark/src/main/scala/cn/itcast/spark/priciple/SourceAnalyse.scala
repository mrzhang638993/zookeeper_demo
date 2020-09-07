package cn.itcast.spark.priciple

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * spark的源码分析操作
 * */
class SourceAnalyse {

  private val exec: SparkConf = new SparkConf().setMaster("local[6]").setAppName("exec2")
  private val context = new SparkContext(exec)
  /**
   * 理解源码操作实现
   *
   * (Hadop,1)
   * (Flume,1)
   * (Sqoop,1)
   * (Spark,2)
   * (Hadoop,1)
   * */
   @Test
  def  wordCount(): Unit ={
     val strContent: RDD[String] = context.parallelize(Seq("Hadoop Spark", "Hadop Flume", "Spark Sqoop"))
     //context.textFile("")  HadoopRDD
     val wordTuple: RDD[(String, Int)] = strContent.flatMap(item => {
       val strings: Array[String] = item.split(" ")
       strings.map(it => (it, 1))
     }).reduceByKey((curr, agg) => curr + agg)
     println("打印逻辑执行图==="+wordTuple.toDebugString)
     //打印逻辑执行图===(6) ShuffledRDD[2] at reduceByKey at SourceAnalyse.scala:29 [].对应的是29行的，执行的是reduceByKey得到的shuffle
     // +-(6) MapPartitionsRDD[1] at flatMap at SourceAnalyse.scala:26 []  26行，执行map操作得到的是MapPartitionsRDD
     //    |  ParallelCollectionRDD[0] at parallelize at SourceAnalyse.scala:25 []  25行，执行parallelize得到ParallelCollectionRDD
     context.stop()
   }


 /**
  * 测试RDD分区窄依赖关系
  * 需求：求得两个RDD之间的迪卡尔集
  * */
  @Test
  def narrowDependency(): Unit ={
    val rdd1: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6))
    val rdd2: RDD[String] = context.parallelize(Seq("a", "b", "c"))
    //  求解rdd1.rdd2的笛卡尔积.cartesian可以用于求解笛卡尔积数据
    val rdd3: RDD[(Int, String)] = rdd1.cartesian(rdd2)
    rdd3.collect().foreach(println(_))
    context.stop()
  }
}
