package com.itcast.spark.sparktest

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

//对应的执行相关的操作实现机制？
object SparkSqlStudy {
  def main(args: Array[String]): Unit = {
    //创建SparkSession对象信息,对应的是使用spark sql的相关的语句和实现操作
    val spark: SparkSession = SparkSession.builder().appName("testSql")
      .master("local[2]")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    //查询相关的文档信息
    /*val df: DataFrame = spark.read.textFile("spark-test/word.txt").toDF("value")
    val valueDf: DataFrame = df.select("value")
    valueDf.show(1)*/
    //结构性的语言查询操作实现
    val collections = List(("zhangsan", 30), ("lisi", 40), ("wangwu", 50))
    //构建schema约束信息。对应的转换成为有schema约束的df的数据结构信息。
    val schema = StructType(Array(StructField("name", StringType, false), StructField("age", IntegerType, false)))
    val rows: List[Row] = collections.map(row => Row(row._1, row._2))
    val value: RDD[Row] = spark.sparkContext.parallelize(rows)
    val frame: DataFrame = spark.createDataFrame(value, schema)
    frame.printSchema()
    frame.select("name").show(2)
    //执行多行表达式语句操作实现。可以实现+1等的表达式的操作实现的。
    frame.selectExpr("name","age + 1").show(3)
    frame.filter("age>40").show(3)
    //下面执行分组操作实现,对应的是分组实现逻辑
    //根据age进行数据的分组操作实现
    frame.groupBy("age").count().show(3)
    frame.show(1)
    //将对应的sql语句转换成为spark的执行程序运行起来
    //temp view对应的是session-scope级别的,当对应的session销毁的时候,对应的任务中断了
    frame.createOrReplaceTempView("collection")
    val sql="select name,age from collection"
    spark.sql(sql).show()
    //创建跨越多个session之间的数据共享的机制，这个类型的session对应的是应用级别的,应用关闭的话,对应的view中断的,是一种全局的视图的
    frame.createGlobalTempView("global")
    //其中global_temp对应的是预留的database的global_temp
    spark.sql("select *  from global_temp.global").show(1)
    //跨视图查询
    spark.newSession().sql("select *  from global_temp.global").show(2)
  }
}
