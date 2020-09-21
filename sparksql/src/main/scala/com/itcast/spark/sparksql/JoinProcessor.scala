package com.itcast.spark.sparksql

import org.apache.spark.sql.SparkSession
import org.junit.Test

/**
 * sparksql的连接操作和代码实现管理
 * */
class JoinProcessor {

  val spark: SparkSession =
    SparkSession.builder().
      master("local[6]").appName("AggrProcessor")
      .getOrCreate()
  // 导入隐式转换
  import  spark.implicits._

  /**
   * 多表联查操作。一次查询多张数据库的表，对应的查询操作实并行还是串行操作实现？
   * 多表联查对应的是并行操作的。
   * 连接：在一个查询中，同一个时间可以并发访问多条数据，称之为连接操作的。
   *  join操作语句
   * */
  @Test
  def  testJoin(): Unit ={
      // 测试连接操作
    val person = Seq((0, "Lucy", 0), (1, "Lily", 0), (2, "Tim", 2), (3, "Danial", 0))
        .toDF("id", "name", "cityId")
    val cities = Seq((0, "Beijing"), (1, "Shanghai"), (2, "Guangzhou"))
      .toDF("id", "name")
    // right: Dataset[_], usingColumn: String
    // +---+------+---------+
    //| id|  name|     name|
    //+---+------+---------+
    //|  0|  Lucy|  Beijing|
    //|  1|  Lily|  Beijing|
    //|  2|   Tim|Guangzhou|
    //|  3|Danial|  Beijing|
    //+---+------+---------+
    person.join(cities,person.col("cityId")===cities.col("id"))
      .select(person.col("id"),
       person.col("name"),cities.col("name")).show()
  }
}
