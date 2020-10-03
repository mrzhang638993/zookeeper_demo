package com.itcast.structured

import org.apache.spark.sql.{DataFrame, SparkSession}

object KafkaSource {
  /**
   * 读取kafka的数据执行操作
   * */
  def main(args: Array[String]): Unit = {
    //  设置spark的数据
    val spark: SparkSession = SparkSession.builder().appName("kafka_source")
      .master("local[6]")
      .getOrCreate()
    // 读取kafka的数据,读取数据的时候需要指定schema信息的。包含topic，partition分区信息，以及offset信息
    val df: DataFrame = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "node01:9092,node02:9092,node03:9092")
      // 对应的这个地方，还可以配置通配符进行操作的。streaming-test*
      .option("subscribe", "streaming-test")
      .option("startingOffsets", "earliest")
      .load()
    //  读取数据执行操作实现
    df.printSchema()
  }
}
