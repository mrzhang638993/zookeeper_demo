package com.itcast.structured

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
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
    val  schema=StructType(
      Seq(
         StructField("name",StringType),
        StructField("sex",StringType),
        StructField("time",IntegerType)
      )
    )
    val ds = spark.readStream
      .format("csv")
      .option("delimiter",",")
      //.option("kafka.bootstrap.servers", "node01:9092,node02:9092,node03:9092")
      // 对应的这个地方，还可以配置通配符进行操作的。streaming-test*
      //.option("subscribe", "internet")
      //.option("startingOffsets", "earliest")
      .schema(schema)
      .load("D:\\document\\works\\zookeeper-demo\\structure_streaming\\src\\main\\scala\\com\\itcast\\structured\\exec")
      .toDF("name", "sex", "time")
    // 数据过滤操作.根据key进行分组操作实现
    val value = ds.filter(item => item.getAs[String](1) == "female").filter(item => item.getInt(2) > 30)
    import  org.apache.spark.sql.functions._
    import  spark.implicits._
    val value1 = value
      .groupBy('name)
        .agg(sum('time))

    value1.writeStream
      .outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()
  }
}
