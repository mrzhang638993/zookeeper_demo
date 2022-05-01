package com.itcast.spark.sparktest

import org.apache.spark.sql.SparkSession

/***
 * 测试相关的orc文件特性
 *
 */
object OrcTest {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("orcTest")
      .master("local[2]")
      .getOrCreate()

  }
}
