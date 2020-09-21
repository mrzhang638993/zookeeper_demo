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
    val df = person.join(cities, person.col("cityId") === cities.col("id"))
      .select(person.col("id"),
        person.col("name"), cities.col("name") as "city")
    val table = df.createTempView("person")
    // 同一个时间访问了2张数据库的表进行联查操作的
    // +---+------+-------+
    //| id|  name|   city|
    //+---+------+-------+
    //|  0|  Lucy|Beijing|
    //|  1|  Lily|Beijing|
    //|  3|Danial|Beijing|
    //+---+------+-------+
    val dfResults = spark.sql("select id ,name,city  from person where city='Beijing'")
    dfResults.show()
  }
  /**
   * 连接查询操作实现
   * 交叉连接：crossJoin操作
   * crossjoin对应的是全连接的操作的。
   * */
  @Test
  def  testCross(): Unit ={
     // 测试cross操作实现
    val person = Seq((0, "Lucy", 0), (1, "Lily", 0), (2, "Tim", 2), (3, "Danial", 0))
       .toDF("id", "name", "cityId")
    val cities = Seq((0, "Beijing"), (1, "Shanghai"), (2, "Guangzhou"))
      .toDF("id", "name")
    //+---+------+------+---+---------+
    //| id|  name|cityId| id|     name|
    //+---+------+------+---+---------+
    //|  0|  Lucy|     0|  0|  Beijing|
    //|  1|  Lily|     0|  0|  Beijing|
    //|  2|   Tim|     2|  2|Guangzhou|
    //|  3|Danial|     0|  0|  Beijing|
    //+---+------+------+---+---------+
    person.crossJoin(cities)
      .where(person.col("cityId")===cities.col("id"))
    // 对应的执行相关的技术操作实现
    person.createTempView("person")
    cities.createTempView("cities")
    //+---+------+
    //| id|  name|
    //+---+------+
    //|  0|  Lucy|
    //|  1|  Lily|
    //|  2|   Tim|
    //|  3|Danial|
    //+---+------+
    spark.sql("select u.id,u.name from person u cross join cities c where  u.cityId=c.id")
      .show()
  }
}
