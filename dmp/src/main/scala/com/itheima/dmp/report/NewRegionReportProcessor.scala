package com.itheima.dmp.report

import com.itheima.dmp.etl.ETLRunner
import com.itheima.dmp.utils.KuduHelper
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.DataFrame

object NewRegionReportProcessor extends ReportProcessor {
  /**
   * 对外提供原表的表名称
   * */
  override def sourceTableName(): String = {
    ETLRunner.ODS_TABLE_NAME
  }

  /**
   * 数据处理的操作实现
   *
   * @param
   *
   * */
  override def process(dataFrame: DataFrame): DataFrame = {
    import dataFrame.sparkSession.implicits._
    import org.apache.spark.sql.functions._
    dataFrame.groupBy("region", "city")
      .agg(count('region) as 'count)
      .select("region", "city", "count")
  }

  /**
   * 返回目标表的名称
   * */
  override def targetTableName(): String = {
    "report_data_region_" + KuduHelper.getParseDateString()
  }

  /**
   * 提供目标表的schema信息
   * */
  override def targetSchema(): Schema = {
    import scala.collection.JavaConverters._
    new Schema(
      List(
        new ColumnSchemaBuilder("region", Type.STRING).nullable(false).key(true).build(),
        new ColumnSchemaBuilder("city", Type.STRING).nullable(false).key(true).build(),
        new ColumnSchemaBuilder("count", Type.INT64).nullable(false).key(false).build()
      ).asJava
    )
  }

  /**
   * 提供目标表的分区键
   * */
  override def targetTableKeys(): List[String] = {
    List("region", "city")
  }
}
