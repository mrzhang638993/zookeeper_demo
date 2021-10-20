package com.itcast.spark.sparktest.analysis

import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

/**
 * 用户选课原始数据清洗到用户事实表数据中
 * 其本质是通过sparksql完成相关的数据操作实现，使用dataFrame完成数据的操作实现。
 *
 * spark的集群启动模式需要配置master的url信息的。连接到对应的集群的。
 * */
object UserLearnSourceToFact {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("course_category")
      .master("local")
      .getOrCreate()
     //根据课程维度得到课程分类维度的数据。
    var comMap=Map[String,String]()
    import spark.implicits._
    spark.sql("select *  from  data_dimen.course_dim").as[CourseDimen].collect().
      foreach(x =>{
        comMap +=x.course_dim_id->x.mt
      })
    //获取用户选课原始数据
    val date_info="2019-11-11"
    //用到的时候才需要引入的，否则默认的会自动的删除不需要使用的导入操作的
    import  spark.implicits._
    val frame: Dataset[LearningCourseSource] = spark.sql(
      s"""
         |select *  from data_course.learning_course_source  where  from_unixtime(choosetime,'yyyy-MM-dd')=${date_info}

         |""".stripMargin)
      .as[LearningCourseSource]
    //封装事实表的数据
    val factValue: DataFrame = frame.map(obj => {
      //获取课程维度的id数据
      val catId: String = comMap.getOrElse(obj.course_id, "")
      //时间维度的数据
      val timeId: String = DateUtils.getDateStr(obj.choose_time.toLong * 1000, "yyyy-MM-dd-HH")
      val ymd: Array[String] = timeId.split("-")
      LearningCourseFact(obj.learning_course_id, catId, obj.course_id, obj.user_id, timeId, obj.status, ymd(0), ymd(1), ymd(2))
    }).toDF()
    //保存到数据仓库中的
    spark.conf.set("hive.exec.dynamic.partition.mode","nonstrict")
    factValue.
      repartition(1).
      write
      .mode(SaveMode.Overwrite)
      .insertInto("data_course.course_visit_fact")
    spark.close()
  }
}
