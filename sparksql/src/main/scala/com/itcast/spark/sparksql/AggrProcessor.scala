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
   * */
  @Test
  def testMultipuleAggr(): Unit ={
     
  }
}
