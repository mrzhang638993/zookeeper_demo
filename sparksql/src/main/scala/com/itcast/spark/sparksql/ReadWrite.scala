package com.itcast.spark.sparksql

import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}
import org.junit.Test

/**
 * spark sql 文件读写操作实现
 * */
class ReadWrite {

  /**
   * 数据读写操作
   * */
   @Test
  def readAndWrite(): Unit ={
     val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
       .getOrCreate()
    //  第一种读取文件的形式
     spark.read
       // 设置文件的格式
       .format("csv")
       // 设置第一行对应的是header的数据的
       .option("header",true)
       //  需要指定数据类型.进行数据类型的推断
       .option("inferSchema",true)
       .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
       .show(10)

     //第二种方式的推断的
     spark.read
       // 设置第一行对应的是header的数据的
       .option("header",true)
       //  需要指定数据类型.进行数据类型的推断
       .option("inferSchema",true)
       // 指定对应的为csv文件
       .csv("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
       .show(10)
     spark.stop()
   }

  /**
   * 数据写入框架
   * */
  @Test
  def  testSparkWrite(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
      .getOrCreate()
    // 读取数据集写入到其他的位置的
    val df: DataFrame = spark.read
      .format("csv")
      .option("header", true)
      //.option("inferSchema",true)
      .load("file:///F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    // 将读取的文件的内容写入到json对象中的。
    //df.printSchema()
    //df.show(10)
    df.write
      .json("file:///F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\2.json")
    //  还可以使用如下的方式实现操作的
    //df.write.format("json").save("file:///F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\beijing_pm1.json")
   // spark.stop()
  }
}
