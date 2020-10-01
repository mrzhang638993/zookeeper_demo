package com.itcast.spark.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.expressions.Window
import org.junit.Test

// 我们可能经常需要针对数据进行多维的聚合, 也就是一次性统计小计 ，试写出多维聚合的代码
// 默写一下课程中使用 SparkSQL 向 MySQL 中写入数据 的代码，写出简要步骤
// 使用代码读写 Parquet 文件 代码，写出简要框架即可
// 写出SparkSQL 的 DataFrameReader 读取mysql中spark_test数据库中student表中数据的代码
class Exec2 {
  //  需求1：试使用窗口函数统计 每个商品和此品类最贵商品之间的差值
  val spark = SparkSession.builder()
    .appName("window")
    .master("local[6]")
    .getOrCreate()
  import spark.implicits._
  import org.apache.spark.sql.functions._
  @Test
  def getExec1(): Unit ={
    // 提供数据
    val source = Seq(
      ("Thin", "Cell phone", 6000),
      ("Normal", "Tablet", 1500),
      ("Mini", "Tablet", 5500),
      ("Ultra thin", "Cell phone", 5000),
      ("Very thin", "Cell phone", 6000),
      ("Big", "Tablet", 2500),
      ("Bendable", "Cell phone", 3000),
      ("Foldable", "Cell phone", 3000),
      ("Pro", "Tablet", 4500),
      ("Pro2", "Tablet", 6500)
    ).toDF("product", "category", "revenue")
    // 定义窗口和函数操作的
    var window=Window.partitionBy('category)
      .orderBy('revenue.desc)
    val maxValue=max('revenue) over(window)
    //+----------+----------+-------+-----------------+
    //|   product|  category|revenue|revenueDifference|
    //+----------+----------+-------+-----------------+
    //|      Thin|Cell phone|   6000|                0|
    //| Very thin|Cell phone|   6000|                0|
    //|Ultra thin|Cell phone|   5000|             1000|
    //|  Bendable|Cell phone|   3000|             3000|
    //|  Foldable|Cell phone|   3000|             3000|
    //|      Pro2|    Tablet|   6500|                0|
    //|      Mini|    Tablet|   5500|             1000|
    //|       Pro|    Tablet|   4500|             2000|
    //|       Big|    Tablet|   2500|             4000|
    //|    Normal|    Tablet|   1500|             5000|
    //+----------+----------+-------+-----------------+
    source.select('product,'category,'revenue,maxValue-'revenue as 'revenueDifference)
      .show()
  }

  // 需求二:
  //+---+------+---------+
  //| id|  name|     city|
  //+---+------+---------+
  //|  0|  Lucy|  Beijing|
  //|  1|  Lily|  Beijing|
  //|  2|   Tim|Guangzhou|
  //|  3|Danial|  Beijing|
  //+---+------+---------+
  @Test
  def testExec2(): Unit ={
    import spark.implicits._
    val persons: DataFrame = Seq(
      (0, "Lucy", 0),
      (1, "Lily", 0),
      (2, "Tim", 2),
      (3, "Danial", 0)
    ).toDF("id", "name", "cityId")

    val cities: DataFrame = Seq(
      (0, "Beijing"),
      (1, "Shanghai"),
      (2, "Guangzhou")
    ).toDF("id", "name")
    import org.apache.spark.sql.functions._
    persons.join(cities,persons.col("cityId")===cities.col("id"),"left")
      .select(persons.col("id") as 'id,persons.col("name") as 'name,cities.col("name") as 'city)
      .show()
  }
}
