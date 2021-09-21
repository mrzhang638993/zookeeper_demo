package com.itheima.dmp.test

import okhttp3.{OkHttpClient, Request, Response, ResponseBody}
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.junit.Test


/**
 * 1.okHttp创建的过程包括2个步骤。
 * 步骤一:创建HttpClient;
 * 步骤二:创建HTTPRequst请求。
 * */
class HttpTest {
  val okHttpClient: OkHttpClient = new OkHttpClient()

  @Test
  def testHttp(): Unit = {
    val url = "https://restapi.amap.com/v3/geocode/regeo?key=f169a20c512862060b2c545548ed235c&location=116.481488,39.990464"
    val request: Request = new Request.Builder()
      .url(url)
      .get()
      .build()
    try {
      val response: Response = okHttpClient.newCall(request).execute()
      //  对应的请求发送成功的话
      if (response.isSuccessful) {
        // {"status":"1","regeocode":{"addressComponent":{"city":[],"province":"北京市","adcode":"110105","district":"朝阳区","towncode":"110105026000","streetNumber":{"number":"6号","location":"116.482005,39.990056","direction":"东南","distance":"63.2126","street":"阜通东大街"},"country":"中国","township":"望京街道","businessAreas":[{"location":"116.470293,39.996171","name":"望京","id":"110105"},{"location":"116.494356,39.971563","name":"酒仙桥","id":"110105"},{"location":"116.492891,39.981321","name":"大山子","id":"110105"}],"building":{"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"},"neighborhood":{"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"},"citycode":"010"},"formatted_address":"北京市朝阳区望京街道方恒国际中心B座"},"info":"OK","infocode":"10000"}
        // 获取请求体的数据
        val body: ResponseBody = response.body()
        println(body.string())
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }


  @Test
  def testJson(): Unit = {
    val json =
      """
        |{
        |  "info": "OK",
        |  "infocode": "10000",
        |  "status": "1",
        |  "regeocode": {
        |      "country": "中国",
        |      "township": "燕园街道",
        |      "businessAreas": [
        |        {
        |          "location": "116.303364,39.97641",
        |          "name": "万泉河",
        |          "id": "110108"
        |        },
        |        {
        |          "location": "116.314222,39.98249",
        |          "name": "中关村",
        |          "id": "110108"
        |        },
        |        {
        |          "location": "116.294214,39.99685",
        |          "name": "西苑",
        |          "id": "110108"
        |        }
        |      ]
        |  }
        |}
  """.stripMargin
    implicit val formats = Serialization.formats(NoTypeHints)
    val gaode: Gaode = Serialization.read[Gaode](json)
    println(gaode)
  }
}

case class Gaode(info: String, infocode: String, status: String, regeocode: Option[Regeocode])

/**
 * 可以避免对应的出现元素为空的时候无法解析的操作的
 * */
case class Regeocode(country: String, township: String, businessAreas: Option[List[BusinessArea]])

case class BusinessArea(location: String, name: String, id: String)
