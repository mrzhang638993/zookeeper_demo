package com.itcast.spark.sparktest

import com.esri.core.geometry.{Geometry, GeometryEngine}
import org.json4s.JsonAST.JObject
import org.json4s.NoTypeHints
import org.json4s.jackson.JsonMethods.{compact, render}
import org.json4s.jackson.Serialization

/**
 * json字符串转换成为FeatureCollection对象的数据的
 * */
object  FeatureExtraction {

    def  parseJson(json:String):FeatureCollection={
        // 导入隐式转换
      implicit  val  formats=Serialization.formats(NoTypeHints)
        // JSON-obj
      import org.json4s.jackson.Serialization.read
      val featureCollection: FeatureCollection = read[FeatureCollection](json)
      featureCollection
    }
}
/**
 * 对象
 * */
class Features()
/**
 * 获取Feature对象信息.获取Geometry对象信息
 * */
case class Feature(properties:Map[String, String], geometry:JObject){
  def getGeometry(): Geometry ={
      // json: String, importFlags: Int, `type`: Geometry.Type
     val mapGeo=GeometryEngine.geoJsonToGeometry(compact(render(geometry)),0,Geometry.Type.Unknown)
     mapGeo.getGeometry
  }
}
