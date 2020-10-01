package com.itcast.spark.sparksql

import org.apache.spark.rdd.RDD
import org.junit.Test
import org.apache.spark.sql.{DataFrame, SparkSession}

class Exec1_ {

  @Test
  def  testWordCount(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("sql").getOrCreate()
    val sourceRdd: RDD[(String, Int)] = spark.sparkContext.textFile("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\wordcount.csv")
      .flatMap(_.split(" "))
      .map(item => (item, 1))
    val frameSource: DataFrame = spark.createDataFrame(sourceRdd).toDF("name", "count")
    frameSource.createOrReplaceTempView("data")
    val resultFrame: DataFrame = spark.sql("select name,count(name) from data group by name")
    resultFrame.show()
    spark.stop()
  }

  @Test
  def  testPerson(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("sql").getOrCreate()
    import  spark.implicits._
    val sourceFrame: DataFrame = Seq(People("zhangsan", 19), People("lisi", 19), People("liuye", 10)).toDF("name", "age")
    //sourceFrame.show()
    sourceFrame.createOrReplaceTempView("person")
    val resultFrame: DataFrame = spark.sql("select name,age  from  person group by age,name ")
    resultFrame.show()
    spark.stop()
  }
}

case class  People(name:String,age:Int)
