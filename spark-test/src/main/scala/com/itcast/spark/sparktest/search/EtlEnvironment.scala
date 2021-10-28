package com.itcast.spark.sparktest.search

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
 /* def getSparkConf(appName: String): SparkConf = {
   /* if (isCluster) {
      initCluster(appName)
    } else {
      init(appName)
    }*/
  }*/

  /**
   * spark-submit提交集群
   *
   * @param appName
   */
  def initCluster(conf: SparkConf) = {
     SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
  }

  /**
   * 开发环境调试
   *
   * @param appName
   */
  def init(conf: SparkConf) = {
      conf.setMaster("local")
      SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
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
     val  sparkConf:SparkConf=null
     if(null!=esTableName){
       esConf(conf(jobName),esTableName)
     }else{
       conf(jobName)
     }
    if(isCluster){
      initCluster(sparkConf)
    }else{
      init(sparkConf)
    }
  }

  def  esConf(sparkConf:SparkConf,esTableName:String):SparkConf={
      sparkConf.set("es.nodes","xc-online-es")
      sparkConf.set("es.port","9200")
      sparkConf.set("es.index.auto.create","true")
      sparkConf.set("es.resource",esTableName)
      sparkConf.set("es.name","docker-cluster")
  }

  def  conf(jobName:String):SparkConf={
     new SparkConf().setAppName(jobName)
  }
}
