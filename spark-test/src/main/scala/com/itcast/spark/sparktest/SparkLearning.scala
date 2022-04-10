package com.itcast.spark.sparktest

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//对应的执行相关的spark执行任务操作实现
object SparkLearning {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("sparkLearning")
    //SparkContext执行操作实现
    val context = new SparkContext(conf)
    //使用内部的数据集完成相关的数据集的操作实现
    /*val count: Array[Int] = Array(1, 2, 3, 4, 5)
    val localValue: RDD[Int] = context.parallelize(count)*/
    //完成数据的抽样操作实现和管理的。take对应的是取样的操作实现的。
    //println(localValue.take(4).mkString("$"))
    //使用外部的数据源进行数据的操作实现。可以指定最小的分区数目的。
    /*val wordValue: RDD[String] = context.textFile("spark-test/word.txt",1)
    val content: String = wordValue.collect().mkString("$$$")
    println(content)*/
    //读取多个小文件的api接口功能实现,对应的返回的是文件名和文件内容的信息的。
    val smallFile: RDD[(String, String)] = context.wholeTextFiles("spark-test/small",1)
    println(smallFile.map(_._2).collect().mkString("$"))
    //对于其他的操作可以使用如下的操作的
    //context.newAPIHadoopRDD()可以根据文件自定义格式和配置来实现输出的。
    /*val value: RDD[(String, Int)] = smallFile.map(fileContent => fileContent._2).map(arr => arr.mkString("")).flatMap(content => content.split("\n"))
      .map(key => (key, 1))*/
    //value.collect().foreach(keyCount=>print(keyCount._1+"==="+keyCount._2))

     /*val textValue: RDD[(String, Int)] = smallFile.map(fileContent => fileContent._2.mkString("")).flatMap(text => text.split("\n")).map(key => (key, 1))
    val tuples: Array[(String, Int)] = textValue.collect()
    tuples.foreach(keyCount=>{
      val key: String = keyCount._1
      val count: Int = keyCount._2
      println(key+"_"+count)
    })*/
    /*val keyCountRdd: RDD[(String, Int)] = textValue.reduceByKey((priv, next) => priv + next)
    val forthValue: String = keyCountRdd.take(4).mkString("$")
    print(forthValue)*/
    context.stop()
  }
}
