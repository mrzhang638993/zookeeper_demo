package com.itheima.dmp.tags

import ch.hsr.geohash.GeoHash
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
//  创建可变集合，直接创建的是不可变的集合的
import scala.collection.mutable


/**
 * 执行打标签的操作
 **/
object TargetRunner {

  import com.itheima.dmp.utils.KuduHelper._
  import com.itheima.dmp.utils.SparkConfigHelper._

  private val ODS_TABLE_NAME: String = "ods_20201007";
  private val BUSINESS_TABLE_NAME: String = "BUSINESS_AREA_1"

  def main(args: Array[String]): Unit = {
    // 创建sparksession
    // 1.创建sparksession
    val spark: SparkSession = SparkSession.builder()
      .appName("tag")
      .master("local[6]")
      .loadConfig()
      .getOrCreate()
    // 读取数据
    val odsOption: Option[DataFrame] = spark.readKuduTable(ODS_TABLE_NAME)
    //  问题：表无法识别？怎么切换到不同的数据库执行操作的
    val areaOption: Option[DataFrame] = spark.readKuduTable(BUSINESS_TABLE_NAME)
    // 标签生成
    if (odsOption == None || areaOption == None) return
    // 没有geoHash参数的。
    import org.apache.spark.sql.functions._
    import spark.implicits._
    val geoHash: UserDefinedFunction = udf(getGeoHash _)
    val df: DataFrame = odsOption.get.withColumn("geoHash", geoHash('longitude, 'latitude))
    val areaOds: DataFrame = df.join(areaOption.get, df.col("geoHash") === areaOption.get.col("geoHash"), joinType = "left")
    //下面是执行打标签的操作实现的。打标签的操作是对应的给数据增加标签操作实现的。
    val tagValues: Dataset[IdsWithTags] = areaOds.map(createTags(_))
    // 保存数据
    tagValues.show(3)
  }

  def getGeoHash(longitude: Double, latitude: Double): String = {
    GeoHash.withCharacterPrecision(latitude, longitude, 8).toBase32
  }

  /**
   * 执行打标签的操作实现
   * row--->mainId,ids,tags
   * 执行row对象转化成为IdsWithTags对象的。
   **/
  def createTags(row: Row): IdsWithTags = {
    // 生成标签数据
    val tags = mutable.Map[String, Int]()
    // 生成广告标识
    tags += ("AD" + row.getAs[Long]("adspacetype") -> 1)
    // 渠道信息
    tags += ("CH" + row.getAs[String]("channelid") -> 1)
    //  关键词 ,关键词之间的间隔是，间隔的
    row.getAs[String]("keywords").split(",")
      .map("KW" + _ -> 1)
      .foreach(tags += _)
    //  省市标签
    tags += "PN" + row.getAs[String]("region") -> 1
    tags += "CN" + row.getAs[String]("city") -> 1
    //   性别标签
    tags += "GD" + row.getAs[String]("sex") -> 1
    //  年龄标签
    tags += "AG" + row.getAs[String]("age") -> 1
    //  商圈标签.正常的情况下，需要在这个地方需要生成hash数据的。
    //  优化写法:直接join操作，将商圈信息直接添加进来的，然后直接取值的。
    row.getAs[String]("area").split(",")
      .map("A" + _ -> 1)
      .foreach(tags += _)
    //  生成mainId
    val ids: Map[String, String] = genIdMap(row)
    val mainId: String = getMainId(ids)
    IdsWithTags(mainId, ids, tags.toMap)
  }

  def getMainId(ids: Map[String, String]): String = {
    val keyList = List(
      "imei", "imeimd5", "imeisha1", "mac", "macmd5", "macsha1", "openudid",
      "openudidmd5", "openudidsha1", "idfa", "idfamd5", "idfasha1"
    )
    // option=>option.isDefined 判断是有数据的
    keyList.map(key => ids.get(key)).filter(option => option.isDefined)
      .map(option => option.get)
      .head
  }

  def genIdMap(row: Row): Map[String, String] = {
    val keyList = List(
      "imei", "imeimd5", "imeisha1", "mac", "macmd5", "macsha1", "openudid",
      "openudidmd5", "openudidsha1", "idfa", "idfamd5", "idfasha1"
    )
    keyList.map(key => key -> row.getAs[String](key))
      .filter(kv => StringUtils.isNotBlank(kv._2))
      .toMap
  }
}

case class IdsWithTags(mainId: String, ids: Map[String, String], tags: Map[String, Int])
