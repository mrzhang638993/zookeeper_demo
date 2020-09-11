package com.itcast.spark.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
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
  /**
   * 测试数据集的数据操作
   * dataset中存放的是结构化的数据的。可以使用类似sql的方式实现查询的
   * dataset是存在类型的。dataset对应的还存在schema的。类型安全的容器。
   * */
  @Test
  def  testDataSet(): Unit ={
     //  创建全局的sparkSession
     val spark = new SparkSession.Builder().master("local[6]").appName("dataset1").getOrCreate()
    import spark.implicits._
    // 演示dataset的操作结果
    val rdd: RDD[Person] = spark.sparkContext.parallelize(Seq(Person("zhangsan", 15), Person("lisi", 20), Person("wangwu", 30)))
    val dataSF: Dataset[Person] = rdd.toDS()
    //  支持强类型的语言查询的
    val filterDateSf: Dataset[Person] = dataSF.filter(item => item.age > 10)
    //  支持弱类型的查询
    //  可以直接编写sql的表达式进行查询操作
    dataSF.filter("age>10").show()
    dataSF.filter($"age">10).show()
  }
  /**
   * 执行物理的RDD的优化操作
   * */
  @Test
  def   testRdd(): Unit ={
    val spark = new SparkSession.Builder().master("local[6]").appName("dataset1").getOrCreate()
    import spark.implicits._
    // 演示dataset的操作结果，创建的方式还是比较的古老的
   /* val rdd: RDD[Person] = spark.sparkContext.parallelize(Seq(Person("zhangsan", 15), Person("lisi", 20), Person("wangwu", 30)))
    val dataset: Dataset[Person] = rdd.toDS()*/
    //
    val  dataset=spark.createDataset(Seq(Person("zhangsan", 15), Person("lisi", 20), Person("wangwu", 30)))
    /**
     * 查看物理执行计划的本质操作
     * 将生成的物理计划转化为可执行的RDD操作。
     * 物理执行计划经过处理之后对应的数据都会转化为InteralRow的数据的
     * 将dataset的类型保留下来的，形成一个同样的数据类型的。
     * */
      // 直接获取到已经分析过的物理执行计划，得到对应的分析过的rdd的数据的
    val resultRdd: RDD[InternalRow] = dataset.queryExecution.toRdd
    //  获取dataset中的rdd数据的。对应的是从dataset的InternalRow通过decode中获取数据转化成为RDD[Person]的。可以获取到底层的rdd，从而进行rdd的操作的
    val rdd: RDD[Person] = dataset.rdd

    println("物理执行计划的rdd:"+resultRdd.toDebugString)
    println("dataset的rdd的数据:"+rdd.toDebugString)
  }
  /**
   * 测试dataFrame的操作的
   * dataframe借鉴的是pandas的datframe的数据结构的
   * */
  @Test
  def  testDataFrame(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("testFramme").getOrCreate()
     import spark.implicits._
    val frame: DataFrame = Seq(Person("zhangsan", 20), Person("lisi", 25), Person("wangwu", 30)).toDF()
    frame.where("age>20")
      .select("name")
      .show()

  }
}

/**
 * 访问权限控制符,错误解决问题
 * */
case class Person(name:String,age:Int)
