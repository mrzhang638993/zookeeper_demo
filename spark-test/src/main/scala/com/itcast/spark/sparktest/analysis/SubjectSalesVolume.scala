package com.itcast.spark.sparktest.analysis

import org.apache.spark.sql.SparkSession

/**
 * 根据周维度统计课程的销售量数据
 * */
object SubjectSalesVolumeByWeekBiz {
  def main(args: Array[String]): Unit = {
    //获取得到spark的操作代码和实现机制
    val spark: SparkSession = SparkSession.builder().appName(this.getClass.getName)
      .master("local")
      .getOrCreate()
    val date_info="2019-11-11"
    //获取得到相关的周期数据信息
    val weeks: Array[String] = DateUtils.getWeekRelativelyInterval(date_info).split(",")
    //对应的是上上周的数据
    val current=weeks(0)
    //对应的是上周的数据信息
    val last=weeks(1)
    println(current)
    println(last)
  }
}
