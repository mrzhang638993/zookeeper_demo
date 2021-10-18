package com.itcast.spark.sparktest.analysis

import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}


/**
 * 课程购买量根据时间进行数据统计操作
 * 根据天为单位对购买量进行数据统计操作
 * 后续的可以将数据从dwd层数据导入到mysql执行数据操作的。
 * 将最终的数据同步到mysql数据库执行数据操作和实现管理的。
 * 企业中一般的将dwd层的数据同步到高斯数据库,从而实现业务层的数据查询操作的。
 * 从数据采集到最终的入库mysql的操作，完全可以通过任务调度的方式来实现操作的。从而保存数据。
 * 高效的代码:可以使用队列的方式来保存需要处理的数据的，这样的话，后续只需要从队列中读取数据执行就可以了。
 * */
object CourseBuyProcessByBiz {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()
    val day="2019-09-26"
    import spark.implicits._
    //cast 数据类型的强制转换操作。是一个很好的功能实现的。
    val buyDs: Dataset[CourseBuyBiz] = spark.sql(
      s"""
         |select   cast(sum(salesvolume) as int) as count,
         |concat_ws('-',years,months,days) as data_info,
         |years,months,days
         |from data_course.course_by_dwm
         |group by years,months,days
         |""".stripMargin).as[CourseBuyBiz]
    buyDs.repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      //直接保存到hive的table中进行操作实现的。
      .insertInto("data_course.course_by_biz")
    spark.conf.set("hive.exec.dynamic.partition.mode","nonstrict")
    spark.close()
  }
}
