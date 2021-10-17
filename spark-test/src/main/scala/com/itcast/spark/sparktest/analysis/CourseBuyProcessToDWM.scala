package com.itcast.spark.sparktest.analysis

import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

/**
 * 统计得到课程购买量指标数据
 * 备注：hive的动态分区操作对应的是根据hive建表时候的分区参数的配置
 * 以及我们提供的hive的数据记录相关的字段的值来进行分区操作的。
 * 最终体现的结果就是根据数据记录来实现数据分区操作实现。
 * */
object CourseBuyProcessToDWM {
  //根据课程维度统计得到课程的购买量指标数据
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().appName(this.getClass.getSimpleName)
      .master("local").getOrCreate()
    import  session.implicits._
    //根据字段之间的关联关系转换到对应的数据仓库中实现的。
    val dataDs: Dataset[CourseBuyDWM] = session.sql(
      s"""
         |select a.course_dim_id,
         |b.company_id  ,
         |b.mt  course_category_dim_id,
         |a.sales,
         |a.salesvolume,
         |a.years,
         |a.months,
         |a.days
         |from
         |(
         |  select course_dim_id,
         |   sum(prices) sales,
         |   count(course_dim_id) salesvolume,
         |   years,
         |   months,
         |   days
         |   from  data_course.course_by_fact  where concat_ws("-",years,months,days)='2019-09-23'
         |   group by course_dim_id,years,months,days
         |) as a
         |left  join data_dim.course_dim b on a.course_dim_id=b.course_dim_id
         |""".stripMargin).as[CourseBuyDWM]
    val frame: DataFrame = dataDs.toDF()
    //配置动态分区操作实现
    session.conf.set("hive.exec.dynamic.partition.mode","nonstrict")
    frame.repartition(1).
      write.mode(SaveMode.Overwrite)
      .insertInto("data_course.course_by_dwm")
    //检查数据是否保存成功
    session.sql("select * from data_course.course_by_dwm").show()
    session.close()
  }
}
