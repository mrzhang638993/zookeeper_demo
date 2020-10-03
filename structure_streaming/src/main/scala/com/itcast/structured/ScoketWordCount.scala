package com.itcast.structured

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object ScoketWordCount {
  /**
   * 完成structured streaming的操作实现的
   * structured streaming 是基于dataset实现操作的
   * */
  def main(args: Array[String]): Unit = {
      //  创建spark session
     val spark=SparkSession.builder()
      // 要求线程的个数至少大于1的
      .master("local[6]")
      .appName("socket_structured")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
      //  数据集的生成，数据读取
      // 流式处理的时候使用的是readStream,批处理的时候采用的是read执行操作的
    // readStream
      val source = spark.readStream
        .format("socket")
        .option("host", "192.168.1.104")
        .option("port", 9999)
        .load()
      //  数据的处理
    import  spark.implicits._
    val sourceDs: Dataset[String] = source.as[String]
      //  结果集的生成和输出,
    //  中间的操作对应的是dataset的处理的
      val words: Dataset[(String, Long)] = sourceDs.flatMap(_.split(" ")).map((_, 1)).groupByKey(_._1)
        .count()
      words.writeStream
      .outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()
  }
}
