package com.itcast.spark.sparksql

import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructField, StructType}
import org.junit.Test

/**
 * mysql的运行方式
 * 1.本地运行；
 * 2.提交到集群运行方式;
 * */
object MysqlWrite {

  def main(args: Array[String]): Unit = {
    //  执行spark操作源码和理解操作实现
    val spark: SparkSession = SparkSession.builder()
      .appName("mysql.example")
      // 集群模式下面是不需要设置master的，集群里面是有自己的master的。
      .master("local[6]")
      .getOrCreate()

    // 需要创建schema的信息的，原始的csv文件中是没有schema的信息的。
    var schema=StructType(List(
      StructField("name",StringType),
      StructField("age",IntegerType),
      StructField("gpa",FloatType)
    ))
     // 读取数据
    val df: DataFrame = spark.read
      .format("csv")
      .schema(schema)
      .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\studenttab10k")

    // 处理数据
    val resultDf: Dataset[Row] = df.where("age<30")
    resultDf.write
      .format("jdbc")
      .option("url","jdbc:mysql://192.168.1.203:3306/spark_test")
      .option("dbtable","student")
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","spark")
      .mode(SaveMode.Overwrite)
      .option("password","Spark123!")
      .save()
    // 执行相关的内存逻辑和数据操作
    spark.stop()
  }
}
