package com.itheima.dmp.report

import com.itheima.dmp.utils.KuduHelper
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.{DataFrame, SparkSession}

object RegionReportProcessor {
  /**
   * 访问外部的成员变量
   * */
  val keys = List("region", "city")
  /**
   * 由于本地时间和阿里云服务器同步的时间存在差异的，所以对应的时间计算的话存在问题的
   * 这个地方就写死了
   * */
  private val SOURCE_TABLE_NAME = "ods_20201007"

  import scala.collection.JavaConverters._

  private val TARGET_TABLE_NAME = "report_data_region_" + KuduHelper.getParseDateString()
  var schema: Schema = new Schema(
    List(
      new ColumnSchemaBuilder("region", Type.STRING).nullable(false).key(true).build(),
      new ColumnSchemaBuilder("city", Type.STRING).nullable(false).key(true).build(),
      new ColumnSchemaBuilder("count", Type.INT64).nullable(false).key(false).build()
    ).asJava
  )

  def main(args: Array[String]): Unit = {
    // 外部可以访问的ODS_TABLE_NAME
    import com.itheima.dmp.utils.KuduHelper._
    import com.itheima.dmp.utils.SparkConfigHelper._
    // 创建sparksession
    val spark: SparkSession = SparkSession.builder()
      .master("local[6]")
      .appName("etl")
      .loadConfig()
      .getOrCreate()
    //  读取原来的kudu的数据 ods_20201007
    val sourceData: Option[DataFrame] = spark.readKuduTable(SOURCE_TABLE_NAME)
    //   按照省市进行分组求取结果
    if (!sourceData.isEmpty) {
      import org.apache.spark.sql.functions._
      import spark.implicits._
      val df: DataFrame = sourceData.getOrElse(None).asInstanceOf[DataFrame]
      val cityRegion: DataFrame = df.groupBy("region", "city")
        .agg(count('region) as 'count)
        .select("region", "city", "count")
      //   落地到report表中。
      spark.createKuduTable(TARGET_TABLE_NAME, schema, keys)
      cityRegion.saveToKudu(TARGET_TABLE_NAME)
    }
  }
}
