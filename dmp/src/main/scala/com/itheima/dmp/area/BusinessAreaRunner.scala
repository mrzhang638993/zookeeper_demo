package com.itheima.dmp.area


import ch.hsr.geohash.GeoHash
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.{DataFrame, SparkSession}


object BusinessAreaRunner {

  import com.itheima.dmp.utils.KuduHelper._
  import com.itheima.dmp.utils.SparkConfigHelper._
  import org.apache.spark.sql.functions._

  import scala.collection.JavaConverters._

  private val AREA_TABLE_NAME = "BUSINESS_AREA_1"
  private val keys = List("geoHash")
  private val schema = new Schema(
    List(
      new ColumnSchemaBuilder("geoHash", Type.STRING).nullable(false).key(true).build(),
      new ColumnSchemaBuilder("area", Type.STRING).nullable(false).key(false).build()
    ).asJava
  )
  private var result: DataFrame = _

  /**
   * 更具经纬度获取商圈信息
   **/
  def main(args: Array[String]): Unit = {
    // 1.创建sparksession
    val spark: SparkSession = SparkSession.builder()
      .appName("business_area")
      .master("local[6]")
      .loadConfig()
      .getOrCreate()
    // 2.读取数据
    val source: Option[DataFrame] = spark.readKuduTable("ods_20201007")
    if (source == null) return
    // 保存原有的商圈信息
    val cleanValue: DataFrame = source.get
    // 3. 获取经纬度数据，调用高德的api接口，获取商圈信息
    //  数据处理的时候存在如下的问题的：数据可能是流式的，也可能是批量的数据的。不能每一次都读取批量的数据来生成标签的，应该尽可能的减少查询的次数的。
    //  读取ods的数据，生成每条数据所在地理位置的范围，尽可能的使用自己本地的商圈数据的。
    val areaDf: Option[DataFrame] = spark.readKuduTable(AREA_TABLE_NAME)
    // 获取经度和纬度数据。
    // 注册自定义的udf函数信息
    spark.udf.register("geoHash", locationToGeoHash _)
    spark.udf.register("fetchArea", fetchArea _)
    if (source.isDefined && areaDf.isEmpty) {
      // 商圈信息不存在的话，查询高德的api创建数据的。
      result = cleanValue.select("longitude", "latitude")
        .selectExpr("geoHash(longitude,latitude) as geoHash1", "fetchArea(longitude,latitude) as area")
      spark.createKuduTable(AREA_TABLE_NAME, schema, keys)
    }
    if (source.isDefined && areaDf.isDefined) {
      // 两个数据都存在的话。商圈表的数据也是存在的。
      // 先进行差集操作，去掉已经有的高德的数据，然后查询对应的高德的api数据的
      val areaInfo: DataFrame = areaDf.get
      val frame: DataFrame = cleanValue.selectExpr("geoHash(longitude,latitude) as geoHash1", "longitude", "latitude")
      // lit中配置的是默认值.增加一列数据执行操作
      result = frame.withColumn("area", lit(null))
        .join(areaInfo, frame.col("geoHash1") === areaInfo.col("geoHash"), joinType = "left")
        .where(areaInfo.col("area") isNull)
        .selectExpr("geoHash1 as geoHash", "fetchArea(longitude, latitude) as area")
      // 去掉原来就存在的数据，执行差集操作。
      //.where(areaInfo.col("area") isNull)
      //  请求高德地图数据
    }
    result.show(3)
    if (result != null) {
      result.saveToKudu(AREA_TABLE_NAME)
    }
  }

  /**
   * 根据经纬度生成范围信息
   **/
  def locationToGeoHash(longitude: Double, latitude: Double): String = {
    //  借助于geoHash可以将一定范围内的经纬度对应的都指定为一个hash的，从而实现范围的概念的。
    GeoHash.withCharacterPrecision(latitude, longitude, 8).toBase32
  }

  def fetchArea(longitude: Double, latitude: Double): String = {
    //  获取到json string
    val gaoDeInfo: Option[String] = HttpUtils.getLocationInfo(longitude, latitude)
    // option上面执行map操作是很安全的
    val regionCode: Option[Regeocode] = gaoDeInfo.map(json => HttpUtils.parseJson(json)).map(item => item.regeocode).getOrElse(None)
    if (regionCode != None) {
      val addressComponent: Option[AddressComponent] = regionCode.map(item => item.addressComponent).getOrElse(None)
      val businessArea: Option[Option[List[BusinessArea]]] = addressComponent.map(item => item.businessAreas)
      val maybeAreas: Option[List[BusinessArea]] = businessArea.getOrElse(None)
      if (maybeAreas != null && maybeAreas != None) {
        val value: List[BusinessArea] = maybeAreas.get
        return value.map(_.name).mkString(",")
      }
    }
    ""
  }
}
