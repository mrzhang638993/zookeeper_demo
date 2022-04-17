package com.itcast.spark.sparktest

import org.apache.spark.sql.{DataFrame, SparkSession}

//测试对应的parquet的文件信息
object TestParquetFile {
  def main(args: Array[String]): Unit = {
    /**
     * parquet对应的是一中列式的数据结构。读取的时候，所有的字段全部的转化成为nullable的结构的
     * */
    val spark: SparkSession = SparkSession.builder().appName("testParquet")
      .master("local[2]")
      .getOrCreate()
    //加载parquet文件
    val frame: DataFrame = spark.read.parquet("spark-test/load.parquet")
    //对应的执行相关的操作实现机制。
    frame.createOrReplaceTempView("people")
    spark.sql("select * from people").show()
    //查询相关的nameDs的数据操作
    val nameDs: DataFrame = spark.sql("select name,age from people  where age between 30 and 50")
    //实现相关的查询操作实现
    import  spark.implicits._
    //执行相关的数据查询操作实现
    nameDs.map(attr=>"Name:"+attr(0)).show()
  }
}
