package com.itcast.spark.sparktest

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

//处理维度数据
object DimensionObject {
  //区域
  case class AreaCode(sheng:String,shi:String,qu:String,shengcode:String,shiCode:String,xianCode:String)
  //经纬度数据
  case class Region(sheng:String,shi:String,qu:String,jin:String,wei:String)
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().appName("dimension")
      .master("local").getOrCreate()
    val context: SparkContext = session.sparkContext
    //使用数据持久化可以提高处理的速度的。
    val araeCode: RDD[String] = context.textFile("D:\\data\\area_code.txt").persist()
    val region: RDD[String] = context.textFile("D:\\data\\region.txt").persist()
    //怎么实现相关的数据的匹配操作实现的
    val areaCodeBean: RDD[AreaCode] = araeCode.map {
      x => {
        val str: Array[String] = x.split(",")
        AreaCode(str(0).split(" ")(0), str(1).split(" ")(0), str(2).split(" ")(0),str(0).split(" ")(1),str(1).split(" ")(1),str(2).split(" ")(1))
      }
    }
    val regionBean: RDD[Region] = region.map {
      x => {
        val regions: Array[String] = x.split(",")
        Region(regions(0), regions(1), regions(2), regions(3), regions(4))
      }
    }
    val areaFrame: DataFrame = session.createDataFrame(areaCodeBean)
    val regionFrame: DataFrame = session.createDataFrame(regionBean)
    //推荐使用spark的sql实现更加复杂的操作的,对应的api使用的是比较的丰富的
    regionFrame.createOrReplaceTempView("region")
    //存在问题，写入到单个的分区会存在性能问题的,不建议这么做的。
    val structType: StructType = areaFrame.schema.add(StructField("id", LongType))
    val indexArea: RDD[(Row, Long)] = areaFrame.rdd.zipWithIndex()
    val rowRDD: RDD[Row] = indexArea.map(tp => Row.merge(tp._1, Row(tp._2)))
    val frame2: DataFrame = session.createDataFrame(rowRDD,structType)
    frame2.createOrReplaceTempView("tmp_area")
    //存在乱码的问题,怎么解决乱码问题
    val frame1: DataFrame = session.sql(
      s"""
        |select
        |t.id as area_dim_id ,t.shengcode as province,t.shiCode as city,t.xianCode as country,
        |r.jin as latitude,r.wei as longitude,
        |r.sheng as province_name,r.shi as city_name,r.qu as country_name
        |from tmp_area t left join region r on t.shi=r.shi and t.qu=r.qu
        |""".stripMargin)

    /***不存在乱码的问题的,使用excel打开确实存在乱码的问题的。但是使用notepad是不存在乱码问题的。
     * 推荐使用notepad打开就行了。其他的方法不要测试了。浪费时间。
     */
    frame1.repartition(1)
      .write.option("delimeter","\\t")
      .option("header","true")
      .mode(SaveMode.Overwrite)
      .csv("D:\\data\\result")
    context.stop()
    session.close()
  }
}

