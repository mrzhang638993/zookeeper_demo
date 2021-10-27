package com.itcast.spark.sparktest.search

import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/**
 * 对应的可以使用相关的hbase实现即席查询操作
 * */
object Save {
  case class Person(id:String,name:String,age:String)
  def main(args: Array[String]): Unit = {
     val esTabeleName:String="demo/doc"
     val spark:SparkSession=EtlEnvironment.getSparkSession(this.getClass.getName,esTabeleName)
     val sc: SparkContext = spark.sparkContext
     val  hbaseName:String="demo"
     val  hbaseColumnName:String="demo_col1"
     val hbaseUtils=new HbaseUtils
     //创建hbase的tableName的数据信息
     hbaseUtils.createTable(hbaseName,hbaseColumnName)
     //获取写入hbase的配置信息
    val conf: JobConf = hbaseUtils.getJobConf(tbName = hbaseName)
    //准备基础数据
    val rdd=sc.parallelize(Array(Person("a1","张三","30岁"),Person("a2","李四","29岁"),Person("a3","王五","21岁")))
    //执行基础数据的保存操作
    spark.close()
  }
}
