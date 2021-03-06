package com.itheima.dmp.area

import com.typesafe.config.{Config, ConfigFactory}
import okhttp3.{OkHttpClient, Request, Response}
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization

object HttpUtils {
  private val config: Config = ConfigFactory.load("common.conf")
  private val key: String = config.getString("amap.key")
  private val baseUrl: String = config.getString("amap.baseurl")
  private val okHttpClient: OkHttpClient = new OkHttpClient()

  def getLocationInfo(longitude: Double, latitude: Double): Option[String] = {
    //  步骤一：确定url实现url操作实现
    val url = s"$baseUrl/v3/geocode/regeo?key=$key&location=$longitude,$latitude"
    //  步骤二：发送请求
    val request: Request = new Request.Builder()
      .url(url)
      .get()
      .build()
    try {
      val response: Response = okHttpClient.newCall(request).execute()
      //  对应的请求发送成功的话
      if (response.isSuccessful) {
        // 获取请求体的数据
        val str: String = response.body().string()
        Some(str)
      } else {
        None
      }
    } catch {
      case e: Exception => None
    }
  }

  def parseJson(json: String): AMapLocation = {
    implicit val format = Serialization.formats(NoTypeHints)
    Serialization.read[AMapLocation](json)
  }
}

case class AMapLocation(regeocode: Option[Regeocode])

case class AddressComponent(businessAreas: Option[List[BusinessArea]])

case class BusinessArea(name: String)

case class Regeocode(addressComponent: Option[AddressComponent])
