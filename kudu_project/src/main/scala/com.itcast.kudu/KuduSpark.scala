package com.itcast.kudu

import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.junit.Test

/**
 * 使用spark执行kudu代码操作实现
 **/
class KuduSpark {

  @Test
  def testSparkKudu(): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[6]")
      .appName("kudu").getOrCreate()
    // val kuduMaster : scala.Predef.String, sc : org.apache.spark.SparkContext, val socketReadTimeoutMs : scala.Option[scala.Long]
    val masterAddress = "192.168.1.205:7051,192.168.1.206:7051,192.168.1.207:7051";
    val kuduContext = new KuduContext(masterAddress, spark.sparkContext)
    // 执行kuduContext
    // 判断表是否存在，存在就删除表
    val TABLE_NAME = "student"
    val exists: Boolean = kuduContext.tableExists(TABLE_NAME)
    //  创建一张kudu的表
    if (exists) {
      kuduContext.deleteTable(TABLE_NAME)
    } else {
      // tableName : scala.Predef.String, schema : org.apache.spark.sql.types.StructType, keys : scala.Seq[scala.Predef.String], options : org.apache.kudu.client.CreateTableOptions
      val schema = StructType(
        StructField("name", StringType, nullable = false) ::
          StructField("age", IntegerType, nullable = false) ::
          StructField("gpa", DoubleType, nullable = false) :: Nil
      )
      val keys = Seq("name")
      import scala.collection.JavaConverters._
      val options = new CreateTableOptions().setRangePartitionColumns(List("name").asJava)
        .setNumReplicas(1)
      val table: Any = kuduContext.createTable(TABLE_NAME, schema, keys, options)
      println(table)
    }
  }

  /**
   * 对数据的增删改查询操作
   **/
  @Test
  def crud(): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[6]")
      .appName("kudu").getOrCreate()
    // val kuduMaster : scala.Predef.String, sc : org.apache.spark.SparkContext, val socketReadTimeoutMs : scala.Option[scala.Long]
    val masterAddress = "192.168.1.205:7051,192.168.1.206:7051,192.168.1.207:7051";
    val kuduContext = new KuduContext(masterAddress, spark.sparkContext)
    // 增
    val TABLE_NAME = "student"
    // data : org.apache.spark.sql.DataFrame, tableName : scala.Predef.String
    // 隐式转换，可以快速的转换获取到df的
    import spark.implicits._
    val df: DataFrame = Seq(
      Student("zhangsan", 15, 60.1),
      Student("lisi", 10, 50.6)
    ).toDF()
    kuduContext.insertRows(df, TABLE_NAME)
    // 删
    // kudu中是不允许key重复的。kudu对应的key是唯一的
    kuduContext.deleteRows(df.select("name"), TABLE_NAME)
    //  增改，增改查询操作实现
    kuduContext.upsertRows(df, TABLE_NAME)
    // 改
    kuduContext.updateRows(df, TABLE_NAME)
  }

  /**
   * dataframe如何写入到kudu中的
   **/
  @Test
  def dfWrite(): Unit = {
    //  需要指定schema的信息。否则会进行类型推断的，对应的和kudu的类型不匹配的。
    val spark: SparkSession = SparkSession.builder().master("local[6]")
      .appName("kudu").getOrCreate()
    val masterAddress = "192.168.1.205:7051,192.168.1.206:7051,192.168.1.207:7051";
    // 读取数据到df中
    val schema = StructType(
      StructField("name", StringType, nullable = false) ::
        StructField("age", IntegerType, nullable = false) ::
        StructField("gpa", DoubleType, nullable = false) :: Nil
    )
    val source: DataFrame = spark.read
      .format("csv")
      .option("header", false)
      .option("delimiter", "\t")
      .schema(schema)
      .load("F:\\works\\hadoop1\\zookeeper-demo\\kudu_project\\src\\main\\scala\\com.itcast.kudu\\studenttab10k")
      .toDF("name", "age", "gpa")
    source.show(2)
    // 写入数据到kudu表中
    val TABLE_NAME = "student"
    import org.apache.kudu.spark.kudu._
    source.write
      .option("kudu.table", TABLE_NAME)
      .option("kudu.master", masterAddress)
      // 当前的版本只支持append操作的
      .mode(SaveMode.Append)
      .kudu
  }

  /**
   * 从kudu中读取数据
   **/
  @Test
  def dfRead(): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[6]")
      .appName("kudu").getOrCreate()
    val masterAddress = "192.168.1.205:7051,192.168.1.206:7051,192.168.1.207:7051";
    val TABLE_NAME = "student"
    import org.apache.kudu.spark.kudu._
    // 读取数据
    val kuduDf: DataFrame = spark.read
      .option("kudu.table", TABLE_NAME)
      .option("kudu.master", masterAddress)
      .kudu
    kuduDf.createOrReplaceTempView("kudu_students")
    val projectDf: DataFrame = spark.sql("" +
      "select name from kudu_students  where gpa>2")
    // action操作的话，才会真正的执行的
    projectDf.show()
  }
}

case class Student(name: String, age: Int, gpa: Double)