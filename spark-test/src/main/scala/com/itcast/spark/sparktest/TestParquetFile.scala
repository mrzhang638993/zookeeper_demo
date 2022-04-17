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
    //Text/CSV/JSON/ORC/Parquet 文件格式信息。都能够自动的发现和推断分区消息。load操作是可以推断出来相关的信息的。
    //分区键失效的情况如何设置。设置参数的时候设置basePath即可的,spark会自动识别分区信息的
    //.option("path","spark-test/person_bucket"),path/to/table/gender=male这种路径指定的话,gender是不会识别为分区键的
    // spark的模式合并操作。模式合并是一个非常昂贵的操作的,默认是关闭的。

  }
}
