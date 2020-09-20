package com.itcast.spark.sparksql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField, StructType}
import org.junit.Test

/**
 * spark sql的聚合操作实现
 * */
class AggrProcessor {

  val spark: SparkSession =
    SparkSession.builder().
      master("local[6]").appName("AggrProcessor")
    .getOrCreate()
  // 导入隐式转换
  import  spark.implicits._

  /**
   * 数据读取,清洗操作
   * */
   @Test
  def testGroupBy(): Unit ={
      // groupBy根据指定的列进行分组操作实现
     // 读取数据执行操作实现
     val  schema=StructType(
       //name 和dataType类型的数据执行操作的
       List(
         StructField("id",LongType),
         StructField("year",IntegerType),
         StructField("month",IntegerType),
         StructField("day",IntegerType),
         StructField("hour",IntegerType),
         StructField("season",IntegerType),
         // double下面存在NAN的，对应的不是字符串的。
         StructField("pm",DoubleType)
       )
     )
     val df = spark.read
       .format("csv")
       .option("header",value = true)
       .schema(schema)
       .csv("D:\\document\\works\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\beijingpm_with_nan.csv")
     //  Double.NaN对应的是一个特殊的类型操作的。
     val filterDf = df.where('pm =!= Double.NaN)
     //
     import org.apache.spark.sql.functions._
     // 按照月份聚合，求解聚合值操作实现
     //  avg操作返回的是单个的列的数据的。
     val groupDf = filterDf.groupBy("year", "month")
     groupDf.agg(avg("pm").as("pm_avg"))
       .orderBy('pm_avg.desc)
       .show()
     //  聚合方式之二:使用groupDataSet的api完成聚合操作实现
     groupDf.avg("pm")
       .select($"avg(pm)" as "pm_avg" )
       .orderBy("pm_avg")
       .show()
     //  执行sum操作实现和管理
     groupDf.sum("pm")
       .select($"sum(pm)".as("pm_avg"))
       .orderBy("pm_avg")
       .show()
   }

  /**
   * 执行多维聚合操作实现
   * 对应的执行相关的聚合操作实现
   * groupby对应的是单个维度的聚合操作实现的
   * 多维聚合：不是特别常见的操作实现的。多维聚合怎么使用普通的spark sql的代码完成多维聚合的操作的
   *
   *多维聚合操作：在一个结果集中包含了总计和小计的数据操作的。同时还需要从不同维度进行聚合操作实现
   *
   * 多维聚合操作：从多个维度进行聚合操作。将每一个维度的计算结果执行union操作接口的
   * */
  @Test
  def testMultipuleAggr(): Unit ={
    val df = spark.read
      .format("csv")
      .option("header", true)
      .csv("D:\\document\\works\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\pm_final.csv")
    //df.show()
    //不同年的，不同来源的pm的数据的.
    // +-------+----+------------------+
    //| source|year|                pm|
    //+-------+----+------------------+
    //| dongsi|2013|  93.2090724784592|
    //| dongsi|2014| 87.08640822045773|
    //| dongsi|2015|  87.4922056770591|
    //| dongsi|null| 89.15443876736389|
    //|us_post|2010|104.04572982326042|
    //|us_post|2011|  99.0932403834184|
    //|us_post|2012| 90.53876763535511|
    //|us_post|2013|101.71110855035722|
    //|us_post|2014| 97.73409537004964|
    //|us_post|2015| 82.78472946356158|
    //|us_post|null| 95.90424117331851|
    //+-------+----+------------------+
    import org.apache.spark.sql.functions._
    val postAndYearDf = df.groupBy("source", "year")
      .agg(avg("pm") as "pm")
      .orderBy($"pm".desc)
   // 在整个的数据集中，按照不同的来源统计pm值得平均数信息
    // +-------+-----------------+
    //| source|               pm|
    //+-------+-----------------+
    //|us_post|95.90424117331851|
    //| dongsi|89.15443876736389|
    //+-------+-----------------+
    val portDf = df.groupBy("source")
      .agg(avg("pm") as "pm")
      //  将null作为一个新的列执行操作的
      .select('source,lit(null) as 'year,'pm)
    // union操作的逻辑是不对的。可以从source以及year进行排序操作的
    // +-------+----+------------------+
    //| source|year|                pm|
    //+-------+----+------------------+
    //| dongsi|null| 89.15443876736389|
    //| dongsi|2013|  93.2090724784592|
    //| dongsi|2014| 87.08640822045773|
    //| dongsi|2015|  87.4922056770591|
    //|us_post|null| 95.90424117331851|
    //|us_post|2010|104.04572982326042|
    //|us_post|2011|  99.0932403834184|
    //|us_post|2012| 90.53876763535511|
    //|us_post|2013|101.71110855035722|
    //|us_post|2014| 97.73409537004964|
    //|us_post|2015| 82.78472946356158|
    //+-------+----+------------------+
    postAndYearDf.union(portDf)
      .sort('source,'year.asc_nulls_last,'pm)
      .show()
  }

  /**
   *  多维聚合操作。rollup实现操作实现的.实现多维聚合操作实现和管理
   * */
  @Test
  def  testRollUp(): Unit ={
    import org.apache.spark.sql.functions._
    val sales = Seq(
      ("Beijing", 2016, 100),
      ("Beijing", 2017, 200),
      ("Shanghai", 2015, 50),
      ("Shanghai", 2016, 150),
      ("Guangzhou", 2017, 50)
    ).toDF("city", "year", "amount")
    // +---------+----+------+
    //|     city|year|amount|
    //+---------+----+------+
    //|  Beijing|2016|   100|
    //|  Beijing|2017|   200|
    //|  Beijing|null|   300|
    //|Guangzhou|2017|    50|
    //|Guangzhou|null|    50|
    //| Shanghai|2015|    50|
    //| Shanghai|2016|   150|
    //| Shanghai|null|   200|
    //|     null|null|   550|
    //+---------+----+------+
    // 1.每个城市，每年的销售额
    //  rollup 对应的滚动分组操作。a分组，b分组，null分组的。对应的rollup其实还是一个分组的操作的。
     sales.rollup("city","year")
      .agg(sum("amount") as "amount")
      .sort('city.asc_nulls_last,'year.asc_nulls_last).show()
    // 3.整个公司在整个城市的销售额。总的销售额

  }
}
