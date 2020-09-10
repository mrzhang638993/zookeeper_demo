package com.itcast.spark.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext, sql}
import org.junit.Test

class Intro {

  @Test
  def  rddIntro(): Unit ={
     val  conf=new SparkConf().setMaster("local[6]").setAppName("intro")
     val sc=new SparkContext(conf)
      sc.textFile("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\wordcount.txt")
        .flatMap(item=>{
          //  切割形成多个元素
          val values: Array[String] = item.split(" ")
           values.map(it=>(it,1))
        }).reduceByKey((curr,agg)=>curr+agg).collect().foreach(println(_))
      sc.stop()
  }

  /**
   * sparksession中包括常见的多种sparkContext的数据的。
   * 原有的sparkContext只是支持textfile的数据集的，引入新的数据集需要更多的对象的。
   * sparksql需要引入更多的数据源，加入更多的数据源的写入。创建一整套的读写的体系操作，需要兼容旧的内容的。
   * */
  @Test
  def  testSparkSql(): Unit ={
    //  获取sparkSession对象。
    val sparkSql:SparkSession = new sql.SparkSession.Builder().master("local[6]").appName("intro").getOrCreate()
    // 对应的导入的是sparkSql的对象信息的。导入隐式转换操作的内容
    import sparkSql.implicits._
    val rdd: RDD[Person] = sparkSql.sparkContext.parallelize(Seq(Person("zhangsan", 15), Person("lisi", 20), Person("wangwu", 30)))
    val personDs: Dataset[Person] = rdd.toDS()
    // 执行一系列的操纵。直接根据对象的name进行查询操作的
    val dataShow: Dataset[String] = personDs.where("age>10")
      .where("age<20")
      .select("name")
      .as[String]
    // 执行数据展示操作
    dataShow.show()
  }

  /**
   * 使用声明式的api进行操作的.sql语句的
   * */
  @Test
  def dfIntro(): Unit ={
    //  获取sparkSession对象。
    val sparkSql:SparkSession = new sql.SparkSession.Builder().master("local[6]").appName("intro").getOrCreate()
    // 对应的导入的是sparkSql的对象信息的。导入隐式转换操作的内容
    import sparkSql.implicits._
    // 无法完成数据的转换操作实现
    val rdd: RDD[Person] = sparkSql.sparkContext.parallelize(Seq(Person("zhangsan", 15), Person("lisi", 20), Person("wangwu", 30)))
    val frame: DataFrame = rdd.toDF()
    // 使用frame的话，需要创建临时表的
    frame.createOrReplaceTempView("person")
    val nameFrame: DataFrame = sparkSql.sql("select name from  person where age>10 and age<20")
    nameFrame.show()
  }
}

/**
 * 访问权限控制符,错误解决问题
 * */
case class Person(name:String,age:Int)
