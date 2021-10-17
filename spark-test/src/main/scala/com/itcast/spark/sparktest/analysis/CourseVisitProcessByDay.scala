package com.itcast.spark.sparktest.analysis

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 课程根据日期进行统计求和处理操作实现
 * 统计一天的数据需要写代码执行操作吗？
 * 操作过程中不需要使用太多的中间结果的保存的,会占用大量的数据
 * 需要使用nifi将数据从mysql同步到hdfs中的。
 * */
object CourseVisitProcessByDay {
  def main(args: Array[String]): Unit = {
    //对应的创建相关的session数据处理操作实现
    val session: SparkSession = SparkSession.builder().master("local")
      .appName("CourseVisitProcessByDay")
      .getOrCreate()
    val frame: DataFrame = session.sql(s"select concat_ws('-',years,months,days) as dateInfo,years,months,days  from data_course.course_visit_fact   ")
    val frame1: DataFrame = frame.groupBy("dateInfo", "years", "month", "day").count()
    //数据保存到数据仓库中。
    val sortFrame: DataFrame = frame1.select("dateInfo", "count", "years", "months", "days")
    //配置动态分区操作结果
    session.conf.set("hive.exec.dynamic.partition.mode","nonstrict")
    sortFrame.repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .insertInto("data_course.course_visit_biz")
    session.close()
  }
}
