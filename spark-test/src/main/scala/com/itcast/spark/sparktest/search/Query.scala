package com.itcast.spark.sparktest.search

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.sparkContextFunctions

import scala.collection.mutable.ArrayBuffer

/**
 * query:对应的实现相关es数据的查询操作和实现
 * */
object Query {
  def main(args: Array[String]): Unit = {
    val esTabeleName:String="demo/doc"
    val spark:SparkSession=EtlEnvironment.getSparkSession(this.getClass.getName,esTabeleName)
    val sc: SparkContext = spark.sparkContext
    val query=
      s"""
         |{"query":{"match":{"name":"张三"}}}
         |
         |""".stripMargin
    val esValue: RDD[(String, collection.Map[String, AnyRef])] = sc.esRDD(esTabeleName, query)
    val rowKeys:ArrayBuffer[String]=ArrayBuffer[String]
    esValue.foreach(es=>{
      rowKeys+=es._2.get("id").get
    })
    //根据rowkey列表集合查询数据
    val hbase:HbaseUtils = new HbaseUtils
    //得到对应的values的数据信息。对应的是hbase中的数据列表
    val values: ArrayBuffer[String] = hbase.getRowKeyList(esTabeleName, rowKeys)
    //执行数据打印操作
    for(str<-values){
      println(str)
    }
    spark.close()
  }
}
