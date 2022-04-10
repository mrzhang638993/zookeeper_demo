package com.itcast.spark.sparktest

import org.apache.spark.sql.{Dataset, SparkSession}

object Read {
  def main(args: Array[String]): Unit = {
    /*val spark: SparkSession = SparkSession.builder().master("local[2]").getOrCreate()
    val frame: DataFrame = spark.read.text("spark-test/readme.txt")
      .toDF()
    frame.show(4)
    spark.stop()*/
    val spark: SparkSession = SparkSession.builder().master("local[2]").appName("test1")
      .getOrCreate()
    val value: Dataset[String] = spark.read.textFile("spark-test/readme.txt")
    value.show(10)
    //获取记录的第一个
    val first=value.first()
    println(first)
    val count: Long = value.count()
    print(count)
    //执行算子的过滤操作和相关的语句实现
    val filterDataset: Dataset[String] = value.filter(content => content.contains("session"))
    println(filterDataset.count())
    filterDataset.show()
    spark.stop()
  }
}
