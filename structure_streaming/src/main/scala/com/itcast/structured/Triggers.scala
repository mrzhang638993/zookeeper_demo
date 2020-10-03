package com.itcast.structured

import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Triggers {
  /**
   * 不指定批次的话，上一批次的数据完成之后，下一批次的数据立马执行的。
   * 可以指定固定的时间间隔划分批次数据的。
   **/
  def main(args: Array[String]): Unit = {
    // 1.创建数据源
    val spark: SparkSession = SparkSession.builder().appName("triggers")
      .master("local[6]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    //  2.简单处理
    val source: DataFrame = spark.readStream
      .format("rate")
      .load()
    val result = source
    //  3.数据落地
    source.writeStream
      .format("console")
      //  使用complete，对应的必须要聚合操作的。没有聚合操作的话，执行的是append的操作的
      .outputMode(OutputMode.Append())
      // 指定批次间隔时间
      //.trigger(Trigger.ProcessingTime("20 seconds"))
      //  对应的只执行一次的操作
      .trigger(Trigger.Once())
      .start()
      .awaitTermination()
  }
}
