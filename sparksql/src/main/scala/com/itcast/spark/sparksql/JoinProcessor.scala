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
  import spark.implicits._
  val person = Seq((0, "Lucy", 0), (1, "Lily", 0), (2, "Tim", 2), (3, "Danial", 0))
    .toDF("id", "name", "cityId")
  val cities = Seq((0, "Beijing"), (1, "Shanghai"), (2, "Guangzhou"))
    .toDF("id", "name")
  person.createTempView("person")
  cities.createTempView("cities")

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

  @Test
  def   testInner(): Unit ={

    person.join(cities,person.col("cityId")===cities.col("id"),joinType = "inner").show()

    //+---+------+---------+
    //| id|  name|     name|
    //+---+------+---------+
    //|  0|  Lucy|  Beijing|
    //|  1|  Lily|  Beijing|
    //|  2|   Tim|Guangzhou|
    //|  3|Danial|  Beijing|
    //+---+------+---------+
    spark.sql("select p.id ,p.name,c.name  from person p inner join cities c on p.cityId=c.id")
      .show()
  }

  /**
   * 测试全外连接操作实现
   * 全外连接对应的是数据的记录操作实现的.包含了一部分无法连接的数据的。会将左右没有连接的数据显示出来的.包含左边没有连接上的。包含右边没有连接上的，以及连接上的。
   * */
  @Test
  def  testOuterJoin(): Unit ={
    //+----+------+------+---+---------+
    //|  id|  name|cityId| id|     name|
    //+----+------+------+---+---------+
    //|null|  null|  null|  1| Shanghai|
    //|   2|   Tim|     2|  2|Guangzhou|
    //|   0|  Lucy|     0|  0|  Beijing|
    //|   1|  Lily|     0|  0|  Beijing|
    //|   3|Danial|     0|  0|  Beijing|
    //+----+------+------+---+---------+
    person.join(cities,person.col("cityId")===cities.col("id"),joinType = "full_outer").show()
    //+----+------+------+---+---------+
    //|  id|  name|cityId| id|     name|
    //+----+------+------+---+---------+
    //|null|  null|  null|  1| Shanghai|
    //|   2|   Tim|     2|  2|Guangzhou|
    //|   0|  Lucy|     0|  0|  Beijing|
    //|   1|  Lily|     0|  0|  Beijing|
    //|   3|Danial|     0|  0|  Beijing|
    //+----+------+------+---+---------+
    spark.sql("select *  from  person p  full outer join cities c on p.cityId=c.id")
  }

  //+---+------+------+---+---------+
  //| id|  name|cityId| id|     name|
  //+---+------+------+---+---------+
  //|  0|  Lucy|     0|  0|  Beijing|
  //|  1|  Lily|     0|  0|  Beijing|
  //|  2|   Tim|     2|  2|Guangzhou|
  //|  3|Danial|     0|  0|  Beijing|
  //+---+------+------+---+---------+
  @Test
  def testLeftJoin(): Unit ={
    //  左外连接
    person.join(cities,person.col("cityId")===cities.col("id"),joinType = "left_outer").show()
    spark.sql("select *  from  person p left  join cities c on p.cityId=c.id").show()

     // 右外连接
    //+----+------+------+---+---------+
    //|  id|  name|cityId| id|     name|
    //+----+------+------+---+---------+
    //|   3|Danial|     0|  0|  Beijing|
    //|   1|  Lily|     0|  0|  Beijing|
    //|   0|  Lucy|     0|  0|  Beijing|
    //|null|  null|  null|  1| Shanghai|
    //|   2|   Tim|     2|  2|Guangzhou|
    //+----+------+------+---+---------+
    person.join(cities, person.col("cityId") === cities.col("id"), joinType = "right").show()
    spark.sql("select *  from  person p right  join cities c on p.cityId=c.id").show()
  }

  //+---+------+------+
  //| id|  name|cityId|
  //+---+------+------+
  //|  0|  Lucy|     0|
  //|  1|  Lily|     0|
  //|  2|   Tim|     2|
  //|  3|Danial|     0|
  //+---+------+------+
  //  anti显示左侧未连接的数据的，不显示右侧的数据的
  // semi显示的是左侧连接的数据的，不显示右侧的数据的
  @Test
  def testLeftAnti(): Unit = {
    //  semi 连接，显示的是左侧的数据的
    person.join(cities, person.col("cityId") === cities.col("id"), joinType = "left_semi").show()
    spark.sql("select *  from  person p left semi  join cities c on p.cityId=c.id").show()
    // anti显示的是右边匹配到的数据的
    person.join(cities, person.col("cityId") === cities.col("id"), joinType = "left_anti").show()
    spark.sql("select *  from  person p left anti  join cities c on p.cityId=c.id").show()
  }
}
