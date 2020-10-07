package com.itheima.dmp.etl

import com.itheima.dmp.utils.KuduHelper
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql._

/**
 * ETL的处理操作实现
 **/
object ETLRunner {
  /**
   * ETL操作表示每天执行一次,需要时间分隔操作
   **/
  private val TARGET_TABLE_NAME = "ods_" + KuduHelper.getParseDateString()
  private val schema: Schema = new Schema(List(
    new ColumnSchemaBuilder("uuid", Type.STRING).nullable(false).key(true).build,
    new ColumnSchemaBuilder("sessionid", Type.STRING).nullable(true).key(false).build(),
    new ColumnSchemaBuilder("advertisersid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adorderid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adcreativeid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adplatformproviderid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("sdkversion", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adplatformkey", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("putinmodeltype", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("requestmode", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adprice", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adppprice", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("requestdate", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("ip", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("appid", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("appname", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("device", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("client", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("osversion", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("density", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("pw", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("ph", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("longitude", Type.FLOAT).nullable(true).key(false).build,
    new ColumnSchemaBuilder("latitude", Type.FLOAT).nullable(true).key(false).build,
    new ColumnSchemaBuilder("region", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("city", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("ispid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("ispname", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("networkmannerid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("networkmannername", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("iseffective", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("isbilling", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adspacetype", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adspacetypename", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("devicetype", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("processnode", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("apptype", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("district", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("paymode", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("isbid", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("bidprice", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("winprice", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("iswin", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("cur", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("rate", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("cnywinprice", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("imei", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("mac", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("idfa", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("openudid", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("androidid", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("rtbprovince", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("rtbcity", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("rtbdistrict", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("rtbstreet", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("storeurl", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("realip", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("isqualityapp", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("bidfloor", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("aw", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("ah", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("imeimd5", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("macmd5", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("idfamd5", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("openudidmd5", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("androididmd5", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("imeisha1", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("macsha1", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("idfasha1", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("openudidsha1", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("androididsha1", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("uuidunknow", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("userid", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("reqdate", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("reqhour", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("iptype", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("initbidprice", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adpayment", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("agentrate", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("lomarkrate", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("adxrate", Type.DOUBLE).nullable(true).key(false).build,
    new ColumnSchemaBuilder("title", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("keywords", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("tagid", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("callbackdate", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("channelid", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("mediatype", Type.INT64).nullable(true).key(false).build,
    new ColumnSchemaBuilder("email", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("tel", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("age", Type.STRING).nullable(true).key(false).build,
    new ColumnSchemaBuilder("sex", Type.STRING).nullable(true).key(false).build
  ).asJava)

  import scala.collection.JavaConverters._

  private val keys = List("uuid")

  /**
   * 执行具体的数据清洗的操作实现
   **/
  def main(args: Array[String]): Unit = {
    import com.itheima.dmp.utils.KuduHelper._
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
    //  3.执行数据操作,返回的是包含了经纬度的数据的。
    val ipClean: Dataset[Row] = IpProcessor.process(source)
    // 需要确保ipClean中的数据和下面的schema的是一样的
    import spark.implicits._
    val selectRows: Seq[Column] = Seq(
      'sessionid, 'advertisersid, 'adorderid, 'adcreativeid, 'adplatformproviderid,
      'sdkversion, 'adplatformkey, 'putinmodeltype, 'requestmode, 'adprice, 'adppprice,
      'requestdate, 'ip, 'appid, 'appname, 'uuid, 'device, 'client, 'osversion, 'density,
      'pw, 'ph, 'longitude, 'latitude, 'region, 'city, 'ispid, 'ispname, 'networkmannerid,
      'networkmannername, 'iseffective, 'isbilling, 'adspacetype, 'adspacetypename,
      'devicetype, 'processnode, 'apptype, 'district, 'paymode, 'isbid, 'bidprice, 'winprice,
      'iswin, 'cur, 'rate, 'cnywinprice, 'imei, 'mac, 'idfa, 'openudid, 'androidid,
      'rtbprovince, 'rtbcity, 'rtbdistrict, 'rtbstreet, 'storeurl, 'realip, 'isqualityapp,
      'bidfloor, 'aw, 'ah, 'imeimd5, 'macmd5, 'idfamd5, 'openudidmd5, 'androididmd5,
      'imeisha1, 'macsha1, 'idfasha1, 'openudidsha1, 'androididsha1, 'uuidunknow, 'userid,
      'reqdate, 'reqhour, 'iptype, 'initbidprice, 'adpayment, 'agentrate, 'lomarkrate,
      'adxrate, 'title, 'keywords, 'tagid, 'callbackdate, 'channelid, 'mediatype, 'email,
      'tel, 'age, 'sex
    )
    // 进行column展开操作，转化成为可变参数的形式的
    val result: DataFrame = ipClean.select(selectRows: _*)
    //  4.数据落地到kudu中的
    // 创建表
    result.createKuduTable(TARGET_TABLE_NAME, schema, keys)
    // 数据保存到表中
    result.saveToKudu(TARGET_TABLE_NAME)
  }
}
