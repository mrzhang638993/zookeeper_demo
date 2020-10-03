package com.itcast.structured

import org.apache.spark.sql.SparkSession

object HDFSSource {

  def main(args: Array[String]): Unit = {
       //  创建sparkContext，对应的是sparkSession
       val spark: SparkSession = SparkSession.builder().appName("hdfs_source")
         .master("local[6]")
         .getOrCreate()
       //  读取数据

       //  输出结果

  }
}
