package com.itcast.spark.sparksql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.{UserDefinedFunction, Window}
import org.junit.Test

class TestUDF {
  /**
   * 测试udf函数操作实现
   * */
    @Test
  def testUdf(): Unit = {
    val spark = SparkSession.builder()
      .appName("window")
      .master("local[6]")
      .getOrCreate()
    import spark.implicits._
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
    // 需求一: 聚合每个类别的总价
    // 1. 分组, 2. 对每一组的数据进行聚合
    //+----------+------------+
    //|  category|sum(revenue)|
    //+----------+------------+
    //|Cell phone|       23000|
    //|    Tablet|       20500|
    //+----------+------------+
    import org.apache.spark.sql.functions._
    source.groupBy("category").agg(sum("revenue")).show()
    // 将名称变为小写的操作实现
    source.select(lower('product)).show()
    // 将数字转换为字符串进行操作的
    val toStrUdf: UserDefinedFunction = udf(toStr _)
    // 处理相关的字符串的需求处理实现
    //+----------+----------+------------+
    //|   product|  category|UDF(revenue)|
    //+----------+----------+------------+
    //|      Thin|Cell phone|          6K|
    //|    Normal|    Tablet|          1K|
    //|      Mini|    Tablet|          5K|
    //|Ultra thin|Cell phone|          5K|
    //| Very thin|Cell phone|          6K|
    //|       Big|    Tablet|          2K|
    //|  Bendable|Cell phone|          3K|
    //|  Foldable|Cell phone|          3K|
    //|       Pro|    Tablet|          4K|
    //|      Pro2|    Tablet|          6K|
    //+----------+----------+------------+
    // 使用自定义的udf函数完成相关的需求的实现操作机制的。
    source.select('product, 'category, toStrUdf('revenue)).show()
  }

  def toStr(revenue: Long): String = {
    revenue / 1000 + "K"
  }
  /**
   *
   * 使用窗口函数解决topN的问题实现的。
   * 求解topN的语句。求解每一个分组前面的两个数据的
   *  求解产品某一种类型的前面的topN的数据
   * 窗口函数是解决topn的思路和实现相关的操作的
   * +----------+----------+------------+
   * |   product|  category|UDF(revenue)|
   * +----------+----------+------------+
   * |      Thin|Cell phone|          6K|
   * |    Normal|    Tablet|          1K|
   * |      Mini|    Tablet|          5K|
   * |Ultra thin|Cell phone|          5K|
   * | Very thin|Cell phone|          6K|
   * |       Big|    Tablet|          2K|
   * |  Bendable|Cell phone|          3K|
   * |  Foldable|Cell phone|          3K|
   * |       Pro|    Tablet|          4K|
   * |      Pro2|    Tablet|          6K|
   * +----------+----------+------------+
   * */
    @Test
  def testWindowsFun(): Unit ={
    val spark = SparkSession.builder()
      .appName("window")
      .master("local[6]")
      .getOrCreate()
    import spark.implicits._
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
    // spark的窗口函数的参数实现
    var window=Window.partitionBy('category).orderBy('revenue.desc)
    import org.apache.spark.sql.functions._
    //  运行分组函数，得到topn的问题解决方案和实现
      //+----------+----------+----+
      //|   product|  category|rank|
      //+----------+----------+----+
      //|      Thin|Cell phone|   1|
      //| Very thin|Cell phone|   1|
      //|Ultra thin|Cell phone|   2|
      //|      Pro2|    Tablet|   1|
      //|      Mini|    Tablet|   2|
      //+----------+----------+----+
    source.select('product,'category,dense_rank() over window as 'rank)
      .where('rank<=2)
      .show()
    // 还可以使用sql函数完成相关的窗口函数的操作和实现
    source.createOrReplaceTempView("productRevenue")
      // +----------+----------+-------+
      //|   product|  category|revenue|
      //+----------+----------+-------+
      //|      Thin|Cell phone|   6000|
      //| Very thin|Cell phone|   6000|
      //|Ultra thin|Cell phone|   5000|
      //|      Pro2|    Tablet|   6500|
      //|      Mini|    Tablet|   5500|
      //+----------+----------+-------+
    spark.sql("select  product,category,revenue  from  " +
      " (select *,dense_rank() over" +
      " (partition by category order by revenue desc) as rank  from productRevenue)" +
      "    where rank<=2  ").show()
  }

  /**
   * 测试窗口函数执行操作实现
   * 窗口函数首先执行分区，然后执行排序操作实现操作的
   * dense_rank对应的是在执行的时候才对于数据进行处理的，其他的时候对于数据是不怎么进行处理的。
   * dense_rank() over (partition by category order by revenue desc) as rank
   * (partition by category order by revenue desc) 定义了一个窗口的。dense_rank对应的定义了一个函数的。
   *
   *
   * 统计每一个商品和最贵的商品之间的差距操作的
   * */
  @Test
  def testWindowFunc2(): Unit ={
    val spark = SparkSession.builder()
      .appName("window")
      .master("local[6]")
      .getOrCreate()
    import spark.implicits._
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
    //  创建对应的数据集进行数据集的操作实现
    //  步骤1：定义窗口函数查找最贵的商品的。根据产品分类找到最贵的商品数据的
    val window=Window.partitionBy('category)
      .orderBy('revenue.desc)
    import org.apache.spark.sql.functions._
    val maxPrice=max('revenue) over window
    // 求解每一组的最大值和最小值的操作实现的
    // +----------+----------+-------+-----------------+
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
    source.select('product,'category,'revenue,maxPrice-'revenue as 'revenueDifference)
      .show()
  }

  /**
   * 在窗口上面支持了很多的窗口函数以及对应的聚合函数进行操作的。
   * */
}
