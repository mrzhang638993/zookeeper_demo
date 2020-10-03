package com.itcast.structured

import org.apache.spark.sql.{Dataset, SparkSession}


/**
 * 1、实时统计连续网购时间超过半个小时的女性网民信息。
 * 2、周末两天的日志文件第一列为姓名，第二列为性别，第三列为本次停留时间，单
 * 位为分钟，分隔符为“,”。
 **/
object Exec1 {

  /**
   * 统计网购的停留时间信息
   **/
  def main(args: Array[String]): Unit = {
    //  设置spark的数据
    val spark: SparkSession = SparkSession.builder().appName("hdfs_sink")
      .master("local[6]")
      .getOrCreate()
    // 从kafka中读取消息写入到hdfs中的
    import spark.implicits._
    val ds: Dataset[String] = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "node01:9092,node02:9092,node03:9092")
      // 对应的这个地方，还可以配置通配符进行操作的。streaming-test*
      .option("subscribe", "internet")
      .option("startingOffsets", "earliest")
      .load()
      .selectExpr("CAST(value as STRING)")
      .as[String]
    // 数据过滤操作.根据key进行分组操作实现
    ds.filter(item => {
      val arr: Array[String] = item.split(",")
      if (arr(0).equals("female")) {
        true
      } else {
        false
      }
      // 筛选超过30分钟的网购时间信息
    }).filter(item => {
      val arr: Array[String] = item.split(",")
      if (arr(2).toInt > 30) {
        true
      } else {
        false
      }
    })
      .map(item => {
        val arr: Array[String] = item.split(",")
        (arr(0).toString, arr(1).toString, arr(2).toInt)
      }).groupByKey(_._1)
      .count()
      .filter(item => item._2 > 120)
      .show()
  }
}
