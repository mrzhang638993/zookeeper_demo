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
     val value: RDD[String] = context.parallelize(Seq("Hadoop Spark", "Hadop Flume", "Spark Sqoop"))
     value.flatMap(item=>{
       val strings: Array[String] = item.split(" ")
       strings.map(it=>(it,1))
     }).reduceByKey((curr,agg)=>curr+agg).collect().foreach(println(_))
     context.stop()
   }
}
