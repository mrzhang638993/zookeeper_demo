package com.itcast.spark.sparktest

import org.apache.spark.sql.{Dataset, Encoders, SaveMode, SparkSession}

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
    //val value: Dataset[Seq[Char]] = textValue.map(text => text.toSeq)(Encoders.kryo[Seq[Char]])
    val value: Dataset[String] = textValue.flatMap(text => text.split("\r\n"))(Encoders.STRING)
    //value.show()
    //数据保存操作
    value.write.mode(SaveMode.Overwrite)/*option("compression", "gzip")*/.text("spark-test/textOut")
    spark.stop()
  }
}
