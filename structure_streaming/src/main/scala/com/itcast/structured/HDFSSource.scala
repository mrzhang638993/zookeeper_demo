package com.itcast.structured

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object HDFSSource {

  def main(args: Array[String]): Unit = {
       // 设置系统的环境变量
      System.setProperty("hadoop.home.dir","D:\\software\\hadoop-2.7.5")
       //  创建sparkContext，对应的是sparkSession
       // 线程数不能低于2的
       val spark: SparkSession = SparkSession.builder().appName("hdfs_source")
         .master("local[6]")
         .getOrCreate()
       //  读取数据
       val schema = StructType(
         Seq(
           StructField("name", StringType),
           StructField("age", StringType)
         )
       )
       val source: DataFrame = spark.readStream
           .schema(schema)
         // 使用stream操作的话，只能读取文件夹而不是文件的
         .json("hdfs://node01:8020/dataset/dataset/")
       //  输出结果
    source.writeStream
      // 追加数据读取的，不进行中间状态的追加操作
      .outputMode(OutputMode.Append())
      // 指定输出路径
      .format("console")
      .start()
      .awaitTermination()
  }
}
