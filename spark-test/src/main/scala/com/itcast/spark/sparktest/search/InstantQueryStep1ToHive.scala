package com.itcast.spark.sparktest.search

import com.itcast.spark.sparktest.analysis.{DateUtils, UserLeanOnline}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 数据保存到hive中
 * */
object InstantQueryStep1ToHive {
  def main(args: Array[String]): Unit = {
    //获取sparkSession对象信息,同时增加hive数据操作支持。
    val sparkSession: SparkSession = SparkSession.builder().appName(this.getClass.getName).master("local[2]")
      .enableHiveSupport().getOrCreate()
    //获取SparkContext对应的上下文对象信息sc
    val sc: SparkContext = sparkSession.sparkContext
    //获取对应的时间信息
    val date_info="2019-11-11"
    //获取原始数据的rdd数据信息
    val originRdd: RDD[String] = sc.textFile(s"/user/hive/external/data_course/learning_course_online_source/${date_info}")
    //将原始数据转化成为bean对象执行数据管理操作实现
    val learningSourceRdd: RDD[UserLeanOnline] = originRdd.map(it => it.split("\t")).map(arr => {
      UserLeanOnline(arr(0), arr(1), arr(2), arr(3), arr(4), arr(5), arr(6).toLong)
    })
    //创建临时表进行数据关联操作和相关逻辑实现操作
    val frame: DataFrame = sparkSession.createDataFrame(learningSourceRdd)
    frame.createOrReplaceTempView("learn_source_tmp")
    //注册udf函数信息,可以进行中间数据的转换操作和实现逻辑的。
    sparkSession.udf.register("getUuid",()=>{
        StrUtils.getUuId()
    })
    //注册新的函数操作实现,将分钟信息转换成为day,hour以及minute信息
    sparkSession.udf.register("minConvertDayHourMin",(time:Long)=>{
       DateUtils.minConvertDayHourMin(time)
    })
    //查询和探测结果,其中使用count(1)的原因在于埋点数据是一分钟发送一次的,所以统计count可以有助于统计相关的指标数据的
    //得到中间表数据
    val resultDf: DataFrame = sparkSession.sql(
      s"""
         |select
         | getUuid() learning_course_online_id,
         | l.course_id,
         | c.name course_name,
         | cv.video_name video_name,
         |  l.user_id,
         |  u.name user_name,
         |  l.learn_time ,
         |  minConvertDayHourMin(l.learn_count_tmp) learn_count,
         |  from_unixtime(l.learn_time/1000,'yyyy-MM-dd')
         | from (
         |select user_id, course_id,course_video_id,user_session_id,
         |min(learn_time) learn_time,
         |count(1)  learn_count_tmp
         |from learn_source_tmp
         |group by user_id,course_id,course_video_id,user_session_id
         |) l
         |left join data_dimen.course_dim c  on c.course_dim_id=l.course_id
         |left join data_dimen.course_video_dim cv on cv.course_video_dim_id=l.course_video_id
         |left join data_dimen.user_dim u on u.user_dim_id=l.user_id
         |""".stripMargin)
    //指定hive参数,将数据保存到hive中
    sparkSession.conf.set("hive.exec.dynamic.partition.mode","nostrict")
    //定义中间表的名称信息
    val  tableName="data_course.learning_course_online_dwm"
    //数据保存到hive表中进行数据保存操作
    //需要注意的是overwrite模式的话,只能处理的是正确的数据的,错误的数据需要手动的进行操作处理实现
    resultDf.repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .insertInto(tableName)
    sparkSession.close()
  }
}
