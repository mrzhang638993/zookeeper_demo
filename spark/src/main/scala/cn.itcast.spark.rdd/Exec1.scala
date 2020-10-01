package cn.itcast.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class Exec1 {

  val testMaster: SparkConf = new SparkConf().setMaster("local[6]").setAppName("testMaster")
  val context = new SparkContext(testMaster)

  @Test
   def   exec1(): Unit ={
    val list: List[Int] = context.parallelize(List(1, 2, 3, 4, 5)).map(item => item * 10).collect().toList
    println(list)
    context.stop()
   }


  @Test
  def wordCount(): Unit ={
    val value: RDD[String] = context.textFile("file:///F:\\works\\hadoop1\\zookeeper-demo\\spark\\src\\main\\scala\\cn.itcast.spark.rdd\\word.txt")
    value.flatMap(item=>item.split(" ")).map(item=>(item,1)).reduceByKey((cur,agg)=>cur+agg).collect().foreach(item=>println(item))
    context.stop()
  }
}
