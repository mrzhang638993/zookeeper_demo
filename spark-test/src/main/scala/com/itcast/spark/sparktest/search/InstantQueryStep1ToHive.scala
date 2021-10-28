package com.itcast.spark.sparktest.search

import com.itcast.spark.sparktest.analysis.UserLeanOnline
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 数据保存到hive中
 * */
object InstantQueryStep1ToHive {
  def main(args: Array[String]): Unit = {
    //获取sparkSession对象信息
    val sparkSession: SparkSession = SparkSession.builder().appName(this.getClass.getName).master("local[2]")
      .getOrCreate()
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
    frame.createOrReplaceTempView("learnSource_tmp")
    //使用sparksql完成相关的变量的操作实现

  }
}
