package com.itheima.dmp.report

import com.itheima.dmp.etl.ETLRunner
import com.itheima.dmp.utils.KuduHelper
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.DataFrame

object AdsRegionReportProcessor extends ReportProcessor {
  /**
   * 数据处理的操作实现
   *
   * @param
   *
   **/
  override def process(dataFrame: DataFrame): DataFrame = {
    val sourceTable = sourceTableName()
    val kpiTableName: String = "kpiTableName"
    // 在省市的分布情况
    val kpiSQL: String =
      s"""
        select
         |s.region,s.city,sum(
         | case
         | when (s.requestmode = 1 and  s.processnode >= 1)  then  1
         | else 0
         | end
         |) as orginal_req_cnt,
        sum(
         | case
         | when (s.requestmode = 1 and  s.processnode >= 2)  then  1
         | else 0
         | end
         |) as valid_req_cnt,
         | sum(
         | case
         | when (s.requestmode = 1 and  s.processnode = 3)  then  1
         | else 0
         | end
         |) as ad_req_cnt,
         | sum(
         | case
         | when (
         | s.iseffective = 1
         | and  s.isbilling  = 1
         | and s.isbid = 1
         | and s.adorderid != 0
         | and s.adplatformproviderid >= 100000
         |)  then  1
         | else 0
         | end
         |) as  join_rtx_cnt,
         | sum(
         | case
         | when (
         | s.iseffective = 1
         | and  s.isbilling  = 1
         | and s.isbid = 1
         | and s.iswin = 1
         | and s.adplatformproviderid >= 100000
         |)  then  1
         | else 0
         | end
         |) as  success_rtx_cnt,
         |sum(
         | case
         | when (
         | s.requestmode = 2
         | and  s.iseffective = 1
         |)  then  1
         | else 0
         | end
         |) as  ad_show_cnt,
         |sum(
         | case
         | when (
         | s.requestmode = 3
         | and  s.iseffective = 1
         |)  then  1
         | else 0
         | end
         |) as  ad_click_cnt,
         sum(CASE
         |WHEN (s.requestmode = 2
         |AND s.iseffective = 1
         |AND s.isbilling = 1) THEN 1
         |ELSE 0
         |END) AS media_show_cnt,
         |sum(CASE
         |WHEN (s.requestmode = 3
         |AND s.iseffective = 1
         |AND s.isbilling = 1) THEN 1
         |ELSE 0
         |END) AS media_click_cnt,
         |sum(
         | case
         | when (
         | s.adplatformproviderid >= 100000
         | and  s.iseffective = 1
         | and s.isbilling = 1
         | and s.isbid = 1
         | and s.adorderid > 20000
         | and s.adcreativeid > 200000)
         | THEN floor(s.winprice / 1000)
         |ELSE 0
         |END) AS dsp_pay_money,
         |sum(
         | case
         | when (
         | s.adplatformproviderid >= 100000
         | and  s.iseffective = 1
         | and s.isbilling = 1
         | and s.isbid = 1
         | and s.adorderid > 20000
         | and s.adcreativeid > 200000)
         | THEN floor(s.adpayment / 1000)
         | ELSE 0
         | END) AS dsp_cost_money
         |from
         |$sourceTable  s
         |group by
         |region,city
         |""".stripMargin
    val rateSQL: String =
      s"""
         |SELECT t.*,
         |t.success_rtx_cnt / t.join_rtx_cnt AS success_rtx_rate,
         |t.media_click_cnt / t.ad_click_cnt AS ad_click_rate
         |FROM $kpiTableName t
         |WHERE t.success_rtx_cnt != 0
         |AND t.join_rtx_cnt != 0
         |AND t.media_click_cnt != 0
         |AND t.ad_click_cnt != 0
         |""".stripMargin
    // 下面执行sql语句执行操作
    dataFrame.createOrReplaceTempView(sourceTable)
    val kpiTable: DataFrame = dataFrame.sparkSession.sql(kpiSQL)
    kpiTable.createOrReplaceTempView(kpiTableName)
    val rateTable: DataFrame = kpiTable.sparkSession.sql(rateSQL)
    //  下面进行数据的统计操作实现
    rateTable
  }

  /**
   * 对外提供原表的表名称
   **/
  override def sourceTableName(): String = {
    ETLRunner.ODS_TABLE_NAME
  }

  /**
   * 返回目标表的名称
   **/
  override def targetTableName(): String = {
    "report_ads_region_" + KuduHelper.getParseDateString()
  }

  /**
   * 提供目标表的schema信息
   **/
  override def targetSchema(): Schema = {
    import scala.collection.JavaConverters._
    new Schema(
      List(
        new ColumnSchemaBuilder("region", Type.STRING).nullable(false).key(true).build(),
        new ColumnSchemaBuilder("city", Type.STRING).nullable(false).key(true).build(),
        new ColumnSchemaBuilder("orginal_req_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("valid_req_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("ad_req_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("join_rtx_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("success_rtx_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("ad_show_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("ad_click_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("media_show_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("media_click_cnt", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("dsp_pay_money", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("dsp_cost_money", Type.INT64).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("success_rtx_rate", Type.DOUBLE).nullable(true).key(false).build(),
        new ColumnSchemaBuilder("ad_click_rate", Type.DOUBLE).nullable(true).key(false).build()
      ).asJava
    )
  }

  /**
   * 提供目标表的分区键
   **/
  override def targetTableKeys(): List[String] = {
    List("region", "city")
  }
}
