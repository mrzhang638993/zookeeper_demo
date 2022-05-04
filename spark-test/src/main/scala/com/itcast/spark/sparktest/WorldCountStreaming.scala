package com.itcast.spark.sparktest

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * world  count的streaming版本
 * */
object WorldCountStreaming {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("worldCountStreaming")
      .master("local[2]")
      .getOrCreate()
    //spark的streaming的编程操作,使用9999端口实现相关的tcp编程操作实现。
    val lines: DataFrame = spark.readStream.format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()
    import spark.implicits._
    //对应的完成相关的数据切割操作实现。
    val words: Dataset[String] = lines.as[String].flatMap(_.split(" "))
    //单行数据的话,默认的是value作为列名称的,对应的是基于spark sql实现操作的。
    val wordCounts: DataFrame = words.groupBy("value").count()
    //streaming模式的话，必须要对应的start模式的
    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()
    query.awaitTermination()
  }
}
