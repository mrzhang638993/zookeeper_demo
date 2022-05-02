package com.itcast.spark.sparktest

import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

/**
 * hive相关的学习操作实现
 * */
object HiveStudy {
  def main(args: Array[String]): Unit = {
    val warehouseLocation="E:\\idea_works\\java\\zookeeper_demo"
    //创建相关的SparkSession。
    val spark: SparkSession = SparkSession.builder().appName("hiveStudy")
      .master("local[2]")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    val textValue: Dataset[String] = spark.read.option("lineSep", ",").textFile("spark-test/person.txt")
    val value: Dataset[String] = textValue.map(text => {
      val strings: Array[String] = text.split(",")
      strings.mkString("\001")
    })
    //write option 配置,text仅支持一个列的，但是实际上是有两个列的数据的
    value.write.mode(SaveMode.Overwrite).text("spark-test/kv1")
    //创建hive相关的表结构信息
    spark.sql("drop table if  exists src1 ")
    //使用的是\001进行分割的,使用这个分割可以自动的按照位置进行加载的，如果使用其他的分割方式是否可以？
    spark.sql("CREATE TABLE IF NOT EXISTS src1 (name String,age INT) USING hive")
    spark.sql("LOAD DATA LOCAL INPATH 'spark-test/kv1' overwrite  INTO TABLE src1")
    val frame: DataFrame = spark.sql("select * from src1")
    frame.show()
  }
}

