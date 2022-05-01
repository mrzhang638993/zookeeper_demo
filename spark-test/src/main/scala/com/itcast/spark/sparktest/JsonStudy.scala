package com.itcast.spark.sparktest

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * 对应的完成相关的json文件格式的学习操作
 * */
object JsonStudy {
  def main(args: Array[String]): Unit = {
    //对应的完成相关的spark的代码实现
    val spark: SparkSession = SparkSession.builder().appName("jsonStudy")
      .master("local[2]")
      .getOrCreate()
    val frame: DataFrame = spark.read
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .json("spark-test/jsonTest")
    frame.createOrReplaceTempView("people")
    val limitPerson: DataFrame = spark.sql("select *  from  people  where age between 30 and 40")
    limitPerson.show()
    //使用集合来创建相关的json的数据
    import spark.implicits._
    val jsonValue: Dataset[String] = spark.createDataset("""{"name":"baidu","age":46}""" :: Nil)
    val jsonCollection: DataFrame = spark.read.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(jsonValue)
    jsonCollection.show()
  }
}
