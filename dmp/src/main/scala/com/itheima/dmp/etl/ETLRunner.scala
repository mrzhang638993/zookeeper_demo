package com.itheima.dmp.etl

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * ETL的处理操作实现
 **/
object ETLRunner {
  /**
   * 执行具体的数据清洗的操作实现
   **/
  def main(args: Array[String]): Unit = {
    import com.itheima.dmp.utils.SparkConfigHelper._
    // 1.创建sparksession
    val spark: SparkSession = SparkSession.builder()
      .master("local[6]")
      .appName("etl")
      .loadConfig()
      .getOrCreate()
    // 2.读取数据集
    val source: DataFrame = spark.read
      .json("F:\\works\\hadoop1\\zookeeper-demo\\dmp\\src\\main\\scala\\com\\itheima\\dmp\\utils\\pmt.json")
    source.show()
    //  3.执行数据操作
    // 对于不同的数据操作的话，需要将数据的操作放置在这个位置的?
    //  4.数据落地
  }
}
