package com.itcast.spark.sparktest

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
//实现wordCount的分区操作实现
object WordCount {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().appName("wordCount")
      .master("local")
      .getOrCreate()
    val context: SparkContext = session.sparkContext
    val textRDD: RDD[String] = context
      .textFile("E:\\idea_works\\java\\zookeeper_demo\\dataset\\wordcount.txt", 1).cache()
    val value: RDD[Array[(String, Int)]] = textRDD.mapPartitions {
      iter =>
        for (it <- iter) yield {
          val allStr: Array[String] = it.split(" ")
          for (str <- allStr) yield (str, 1)
        }
    }
    //根据数值的降序排列操作实现
    val keyValue: RDD[(String, Int)] = value.flatMap(x => x.toList).reduceByKey(_ + _).sortBy(_._2,ascending =false )
    keyValue.collect().foreach(println(_))
    context.stop()
    session.close()
  }
}
