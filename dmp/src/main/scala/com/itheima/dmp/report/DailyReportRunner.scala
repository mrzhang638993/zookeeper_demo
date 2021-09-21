package com.itheima.dmp.report

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 公共流程处理的类
 * */
object DailyReportRunner {

  def main(args: Array[String]): Unit = {
    //  创建sparkSession
    // 外部可以访问的ODS_TABLE_NAME
    import com.itheima.dmp.utils.KuduHelper._
    import com.itheima.dmp.utils.SparkConfigHelper._
    // 创建sparksession
    val spark: SparkSession = SparkSession.builder()
      .master("local[6]")
      .appName("daily  report runner")
      .loadConfig()
      .getOrCreate()
    //   创建容器,放置所有的Processor数据
    val processors = List[ReportProcessor](
      NewRegionReportProcessor,
      AdsRegionReportProcessor
    )
    //   循环容器，拿到每一个的processor,执行每一个processor,
    //   每一个processor对应的都代表了一个报表的处理过程。
    //   数据处理
    //   数据落地
    for (processor <- processors) {
      val source: Option[DataFrame] = spark.readKuduTable(processor.sourceTableName())
      if (source.isEmpty) return
      val processorDf: DataFrame = processor.process(source.get)
      spark.createKuduTable(processor.targetTableName(), processor.targetSchema(), processor.targetTableKeys())
      processorDf.saveToKudu(processor.targetTableName())
    }
  }
}
