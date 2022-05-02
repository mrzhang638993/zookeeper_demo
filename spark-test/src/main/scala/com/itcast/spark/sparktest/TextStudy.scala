package com.itcast.spark.sparktest

import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * 测试学习text文档相关的操作
 * */
object TextStudy {
  def main(args: Array[String]): Unit = {
    //构建相关的spark任务实现
    val spark: SparkSession = SparkSession.builder().appName("textStudy")
      .master("local[2]")
      .getOrCreate()
    //对应的开始实现相关的技术操作实现
    val path="spark-test/small"
    val textValue: Dataset[String] = spark.read
      .option("lineSep",",")
      //设置如下的option,这样的话,一个文件当做一行来进行输入。默认的是
      .option("wholetext","true")
      .textFile(path)
    //对应的是提个array的数组信息的
    val values: Array[String] = textValue.take(1)
    //数组的长度唯一,元素也是只有一个元素的,最终是可以得到相关的元素的切分操作的
    val lastValues: Array[String] = values.toSeq.head.split("\r\n")
    println(lastValues.seq)
  }
}
