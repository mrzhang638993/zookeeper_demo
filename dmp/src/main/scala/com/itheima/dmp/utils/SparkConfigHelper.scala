package com.itheima.dmp.utils


import com.typesafe.config.{ConfigFactory, ConfigValue}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.SparkSession

import java.util.Map
import scala.collection.mutable

/**
 * 隐式转换:将spark转换成为SparkConfigHelper，增加loadConfig的功能。
 * 使用scala的隐式转换增加额外的功能实现的。
 * */
class SparkConfigHelper(builder: SparkSession.Builder) {
  val config = ConfigFactory.load("spark.conf")

  def loadConfig(): SparkSession.Builder = {
    //  spark的conf文件是可以修改的。存在多次修改的可能的
    //  对应的是key和value的相关的操作的。配置文件的修改会导致文件的修改的。需要自动的加载所有的配置,而不是手动的加载的
    //  需要获取文件中所有的键值对信息。遍历键值对,获取其中所有的key和所有的value的数据的。
    //  将所有的key以及value返回给键值对的数据的。
    import scala.collection.JavaConverters._
    val entrySet: mutable.Set[Map.Entry[String, ConfigValue]] = config.entrySet().asScala
    for (entry <- entrySet) {
      // 处理所有的键值对的数据
      val key: String = entry.getKey
      val value: String = entry.getValue.unwrapped().asInstanceOf[String]
      //  需要判断数据的来源.只保留自己的配置选项数据
      val origin = entry.getValue.origin().filename()
      if (StringUtils.isNoneEmpty(origin)) {
        builder.config(key, value)
      }
    }
    builder
  }
}

/**
 * 伴生对象
 * */
object SparkConfigHelper {

  implicit def sparkToConfig(builder: SparkSession.Builder): SparkConfigHelper = {
    new SparkConfigHelper(builder)
  }
}


