package com.itcast.spark.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, DataFrameReader, SaveMode, SparkSession}
import org.junit.Test

/**
 * spark sql 文件读写操作实现
 * */
class ReadWrite {

  /**
   * 数据读写操作
   * */
   @Test
  def readAndWrite(): Unit ={
     val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
       .getOrCreate()
    //  第一种读取文件的形式
     spark.read
       // 设置文件的格式
       .format("csv")
       // 设置第一行对应的是header的数据的
       .option("header",true)
       //  需要指定数据类型.进行数据类型的推断
       .option("inferSchema",true)
       .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
       .show(10)

     //第二种方式的推断的
     spark.read
       // 设置第一行对应的是header的数据的
       .option("header",true)
       //  需要指定数据类型.进行数据类型的推断
       .option("inferSchema",true)
       // 指定对应的为csv文件
       .csv("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
       .show(10)
     spark.stop()
   }

  /**
   * 数据写入框架
   * Task failed while writing rows 需要hadoop的底层的环境的配置和支持操作的。
   * spark的write涉及到了hdfs的操作的。
   * */
  @Test
  def  testSparkWrite(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
      .getOrCreate()
    // 读取数据集写入到其他的位置的
    val df: DataFrame = spark.read
      .format("csv")
      .option("header", value = true)
      .option("inferSchema",value = true)
      .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    // 将读取的文件的内容写入到json对象中的。
     //df.printSchema()
     //df.show(10)
    df.write
      .json("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\2.json")
    //  还可以使用如下的方式实现操作的
    //df.write.format("json").save("file:///F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\beijing_pm1.json")
   // spark.stop()
  }

  @Test
  def  testWriter(): Unit ={
    //  spark的write依赖于底层的hdfs的，需要注意spark的依赖处理操作的。
    System.setProperty("hadoop.home.dir","F:\\software\\hadoop2.7.5");
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
      .getOrCreate()
    //  需要设置相关的数据集然后执行数据的操作的。
    val df: DataFrame = spark.read.format("csv").option("header", true).load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    //df.write.json("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\3")

    // 测试方式之二：
    df.write.format("json").json("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\4")
    spark.stop()
  }

  /**
   *  测试使用parquet的操作实现
   * */
  @Test
  def  testParquet(): Unit ={
      // 读取csv文件的数据
      val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
        .getOrCreate()
    val df: DataFrame = spark.read
      .option("header", true)
      .format("csv")
      .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    //  将数据细微parquet的数据格式.spark默认的写入格式是parquet的默认的写入格式的。
    df.write
      .format("parquet")
      //  默认是文件存在的话就报错的 ErrorIfExists
      .mode(SaveMode.Overwrite)
      .save("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\5")
    //  重新读取文件进行操作实现.默认的spark读取文件的格式是spark的文件格式的
    //  是可以读取出来文件夹的。读取文件夹对应的是可以读取到对应的parquet的数据的。
    spark.read
      .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\5")
      .show(10)
    // 会生成文件夹的操作的原因在于，针对于每一个分区都会生成一个文件的。多个分区操作之后会生成一个文件夹的。
  }

  /**
   * 测试表分区的操作实现
   * 表分区的操作不仅在于parquet对象上有的，在其他的格式方面也是可以存在的。
   * spark会进行自动的分区发现的。
   * */
  @Test
  def  tablePartition(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
      .getOrCreate()
    val df: DataFrame = spark.read
      .format("csv")
      .option("header", true)
      .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    // 指定分区操作
    df.write
      .mode(SaveMode.Overwrite)
      .partitionBy("year","month")
      .save("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\6")

    // 重新读取文件夹执行操作,分区之后读取的时候对应的分区列不会包含在生成的文件中的。
    //  读取的时候是获取不了分区列的字段的，分区信息以及对应的列胡丢失的，需要进行处理的。
    //  读取的时候需要读取的是最顶级的文件夹的，其他的不用管的。
    val df1: DataFrame = spark.read
      .format("parquet")
      .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\6").toDF()
    df1.show(10)
    spark.stop()
  }

  /**
   * 如何读写json格式的文件
   * 生成的不是标准格式的json文件的。每一个记录对应的是以恶json记录的。
   * 业务系统之中需要大量的使用json文件的，使用json解析操作的时候，对应的可以使用spark的json解析操作的。
   * gson，fastjson等的工具的，需要的是每一行的数据记录对应的是一个单个的json文件的。
   * 书写json的时候没有指定schema的话，给定的数据都是字符串形式的数据的。
   * 需要给定schema的约束的。
   * */
    @Test
  def  writeJson(): Unit ={
      val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
        .getOrCreate()
      val df: DataFrame = spark.read
        .option("header", true)
        .format("csv")
        .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv").toDF()

      df.write
        .format("json")
        .mode(SaveMode.Overwrite)
        .save("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\7")

      /**
       * json文件的读取操作
       * */
      val json: DataFrame = spark.read
        .json("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\7").toDF()
      json.show(10)
      spark.stop()
  }

  /**
   * json文件读取的小技巧的
   * toJson对应的可以转化为json形式的dataFrame数据的
   * 可以直接从rdd读取json的dataFrame的数据的。
   * 场景信息:消息发送到消息队列里面进行操作处理的。
   * 从消息队列中读取json形式的数据，需要使用spark进行操作处理的。
   * */
    @Test
  def   jsonTicks(): Unit ={
      val spark: SparkSession = SparkSession.builder().master("local[6]").appName("readAndWrite")
        .getOrCreate()
      val df: DataFrame = spark.read
        .option("header", true)
        .format("csv")
        .load("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
      //将dataFrame中的数据转化为json对象给别的系统进行处理操作的。
       //df.toJSON.show(10) toJson可以将DataSet[object]转化为DataSet[jsonString]执行操作的
      //   对应的直接从rdd中读取json执行操作的
       val rdd: RDD[String] = df.toJSON.rdd
      // 直接从rdd中读取数据执行操作处理实现的。
      val frame: DataFrame = spark.read.json(rdd)  //  RDD[jsonString]转化为RDD[Object]
      frame.show()
    }

  /**
   * spark sql访问hive操作的
   * 一个系统访问另外的一个系统的
   * spark sql整合hive操作的
   * hive对应的是通过表的概念映射了一个或者是多个hdfs中的文件的。
   * hive的sql语句对应的会翻译成为mr程序的，然后执行的。
   * 需要整合的话，整合的是hive的metastore的数据的。
   * */


}
