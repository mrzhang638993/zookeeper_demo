package com.itcast.spark.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.encoders.RowEncoder
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
   *
   * 创建dataFrame的方式:toDF() , createDataFrame,DataFrameReader方式创建。可以通过读取文件的方式实现
   * */
  @Test
  def  testDataFrame(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("testFramme").getOrCreate()
    //  导入了隐式转换的对象的rddToDatasetHolder方法的
    import spark.implicits._
    // toDF最终调用的是 sparkSession.createDataset(data)
    // 调用隐式转换对象的DF方法的。
    // new Dataset[Row](sparkSession, queryExecution, RowEncoder(schema))
    // 根据类型匹配自动调用SQLImplicits的 implicit def rddToDatasetHolder[T : Encoder](rdd: RDD[T]): DatasetHolder[T] = {
    //    DatasetHolder(_sqlContext.createDataset(rdd))
    //  }
    val frame: DataFrame = Seq(Person("zhangsan", 20), Person("lisi", 25), Person("wangwu", 30)).toDF()
    frame.where("age>20")
      .select("name")
      .show()
  }


  @Test
  def  testDataFrame2(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("testFramme").getOrCreate()
    import spark.implicits._
    val frame: DataFrame = Seq(Person("zhangsan", 20), Person("lisi", 25), Person("wangwu", 30)).toDF()
    frame.where("age>20")
      .select("name")
      .show()
  }


  @Test
  def  testDataFrame3(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("testFramme").getOrCreate()
    import spark.implicits._
    val persons = Seq(Person("zhangsan", 20), Person("lisi", 25), Person("wangwu", 30))
    //  创建DF的方式之一
    val frame: DataFrame = persons.toDF()
    val frame1: DataFrame = spark.sparkContext.parallelize(persons).toDF()
    // 创建DataFrame
    val frame2: DataFrame = spark.createDataFrame(persons)
    // 读取文件创建dataFrame数据结构
    val frame3: DataFrame = spark.read.csv("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    frame3.show()
  }


  @Test
  def  testDataFrame4(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("testFrame").getOrCreate()
    import spark.implicits._
    //  需要处理第一行的头信心
    val frame3: DataFrame = spark.read
      //  option 选项指定header是存在的
      .option("header",value=true)
      // 读取数据的时候是否可以读取部分的数据.csv文件的options对应的参考文件CSVOptions获取到诸多的options选项
      // https://blog.csdn.net/OldDirverHelpMe/article/details/106120312?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~first_rank_v2~rank_v25-20-106120312.nonecase&utm_term=spark%E8%AF%BB%E5%8F%96%E6%8C%87%E5%AE%9A%E5%88%97 对应的也可以参考文档执行处理的
      .csv("F:\\works\\hadoop1\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    // 打印schema信息。dataFrame中是存在结构信息的
    //frame3.printSchema()
    //  下面使用命令式的操作的
    /*frame3.select("year","month","PM_Dongsi")
      // 不等于的符号 =!=
      .where("PM_Dongsi !='NA'")
      .groupBy("year","month")
      // 求得每一组的个数的
      .count()
      // 执行action操作
      .show()*/
    // 下面直接使用sql语句执行操作的
    frame3.createOrReplaceTempView("pm")
    val frame: DataFrame = spark.sql("select year,month,count(PM_Dongsi) from pm where PM_Dongsi!='NA' group by year,month")
    frame.show()
    spark.stop()
    // type DataFrame = Dataset[Row] 对应的dataFrame对应的是dataSet的别名的。
    //frame3.show()
  }

  /**
   * 测试dataset以及dataFrame的差异性
   * */
  @Test
  def  testDataFrame5(): Unit ={
    val spark: SparkSession = SparkSession.builder().master("local[6]").appName("testFrame").getOrCreate()
    import spark.implicits._
    val persons = Seq(Person("zhangsan", 15), Person("lisi", 20))
    // 操作，转化成为DataFrame对象。
    val frame: DataFrame = persons.toDF()
    // dataFrame操作的是row对象的
    frame.map((row:Row)=>Row(row.get(0),row.getAs[Int](1)*2))(RowEncoder.apply(frame.schema)).show()
    // 转化为dataset
    val ds: Dataset[Person] = persons.toDS()
    // 操作ds，ds中存放的是对象的内容的。
    ds.map((person:Person)=>Person(person.name,person.age*2)).show()
    spark.stop()
  }
  /**
   * 测试使用dataframe的row对象的
   * */
  @Test
  def  row(): Unit ={
    val zhangsan: Person = Person("zhangsan", 15)
    val person: Row = Row("lisi", 20)
    //  获取到row对象的第一列和第二列
    person.getString(0)
    person.getInt(1)
    //
    person match {
      case Row(name,age)=>println(name+"===="+age)
    }
  }
}

/**
 * 访问权限控制符,错误解决问题
 * */
case class Person(name:String,age:Int)
