package com.itcast.spark.sparktest.search

import com.itcast.spark.sparktest.analysis.StrUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.util.Properties

/**
 * <P>
 * build spark config
 * </p>
 *
 */
object EtlEnvironment {

  /**
   * 根据配置文件判断使用那种模式
   *
   * @param appName
   */
  def getSparkConf(appName: String): SparkConf = {
    if (isCluster) {
      initCluster(appName)
    } else {
      init(appName)
    }
  }

  /**
   * spark-submit提交集群
   *
   * @param appName
   */
  def initCluster(appName: String) = {
    new SparkConf()
      .setAppName(StrUtils.getClassName(appName))
  }

  /**
   * 开发环境调试
   *
   * @param appName
   */
  def init(appName: String) = {

    // 本地模式， 集群不进行计算
    val master = "local"
    // spark配置
    new SparkConf()
      .setMaster(master)
      // 根据类名设置appName
      .setAppName(StrUtils.getClassName(appName))
  }

  /**
   * 获取是否使用集群
   */
  def isCluster = {
    val properties = new Properties()
    // 获取配置文件
    val inputStream = this.getClass.getClassLoader.getResourceAsStream("application.properties")
    // properties加载数据
    properties.load(inputStream)
    // 获取值, 如果没有获取到, 设置为false
    properties.getProperty("useingCluster", "false").toBoolean
  }

  def  getSparkSession( jobName:String, esTableName:String): SparkSession ={
     //具体的sparkSession的代码改造实现
    val conf: SparkConf = init(jobName)
    conf.set("es.index.auto.create","true")
    conf.set("es.nodes","192.168.47.100:9200")
    //是否需要配置相关的estable的数据信息的？
    SparkSession.builder().config(conf).getOrCreate()
  }

}
