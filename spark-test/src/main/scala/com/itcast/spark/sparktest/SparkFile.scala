package com.itcast.spark.sparktest

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 基于通用文件的相关的操作实现的。
 * */
object SparkFile {
  def main(args: Array[String]): Unit = {
    //创建相关的sparkSession的数据。
    val spark: SparkSession = SparkSession.builder().appName("sparkFile")
      .master("local[2]")
      .getOrCreate()
    //常见的支持的文件的格式是如下的:parquet, orc, avro, json, csv, text.
    /**
     * spark.sql.files.ignoreCorruptFiles=true 忽略损坏的文件。默认是
     * spark.sql.files.ignoreMissingFiles  忽略丢失的文件。
     * pathGlobFilter  读取文件的时候，读取符合条件的文件
     * recursiveFileLookup  递归读取文件信息
     * modifiedBefore以及modifiedAfter 仅仅适用于spark的batch query查询操作实现。对于stream模式的是不支持的
     */
    spark.sql("set spark.sql.files.ignoreCorruptFiles=true")
    spark.sql("set spark.sql.files.ignoreMissingFiles=true")
    //可以指定多个parquet文件的路径的
    val frame: DataFrame = spark.read
      //使用通配符来过滤相关的文件
      .option("pathGlobFilter","*.parquet")
      //递归查找文件，默认的是false
      .option("recursiveFileLookup", "true")
      //过滤时间范围的数据，对应的是在指定的时间之间的数据的
      .option("modifiedBefore", "2022-04-17T17:30:00")
      //规律时间范围数据,对应的是在指定的时间范围之外的数据的。
      .option("modifiedAfter", "2022-04-17T08:30:00")
      .parquet("spark-test/load.parquet", "spark-test/person.parquet")
    frame.show()
  }
}
