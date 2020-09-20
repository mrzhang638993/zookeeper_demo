package com.itcast.spark.sparksql


import org.apache.spark.sql.{Column, DataFrameNaFunctions, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField, StructType}
import org.junit.Test


class NullProcessor {
  val spark: SparkSession = SparkSession.builder()
    .appName("typed")
    // 集群模式下面是不需要设置master的，集群里面是有自己的master的。
    .master("local[6]")
    .getOrCreate()

  import spark.implicits._

  /**
   * 对于NULL以及NAN类型的数据执行的是填充或者是丢弃的操作的
   * NAN：not  a Number
   * */
  @Test
  def nullAndNAN(): Unit ={
     // 导入数据集，
    /* val df = spark.read
       .format("csv")
       .option("header", true)
       // 执行类型推断操作实现。会将NAN推断为字符串的。
       .option("inferSchema",true)
       .csv("D:\\document\\works\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\beijingpm_with_nan.csv").toDF()*/
    //  读取为字符串，在后续的处理中使用那个map算子执行类型转化操作实现的。
    //  指定schema,不要自动类型推断操作
    val  schema=StructType(
      //name 和dataType类型的数据执行操作的
      List(
        StructField("id",LongType),
        StructField("year",IntegerType),
        StructField("month",IntegerType),
        StructField("day",IntegerType),
        StructField("hour",IntegerType),
        StructField("season",IntegerType),
        // double下面存在NAN的，对应的不是字符串的。
        StructField("pm",DoubleType)
       )
    )
    val df = spark.read
      .format("csv")
      .option("header", true)
      .schema(schema)
      .csv("D:\\document\\works\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\beijingpm_with_nan.csv").toDF()
    //df.show()
    // 丢弃数据执行丢弃操作，需要规则
    // 1.any:一行数据中有一个数据为NAN的行丢弃
    df.na.drop("any").show()
    // 不指定模式的话，对应的还是any操作的。
    df.na.drop().show()
    // 2.all：一行数据中所有的列都包含了NAN的话，丢弃的
    df.na.drop("all").show()
    // 3.某些列的规则：针对某些列包含了NAN的话，会执行数据的丢失操作的。
    df.na.drop("any",Array("year","month","day","season"))
    // 缺失值得填充操作实现。
    // 方式1：针对所有的列数据进行默认值的填充
    df.na.fill(0).show()
    // 方式2:针对特定列进行填充操作
    df.na.fill(0,Array("year","month","day")).show()
  }

  /**
   * 对于string类型的nan的数据处理操作实现的
   * */
  @Test
  def  testNullString(): Unit ={
    val df = spark.read
      .format("csv")
      .option("header", true)
      .csv("D:\\document\\works\\zookeeper-demo\\sparksql\\src\\main\\scala\\com\\itcast\\spark\\sparksql\\BeijingPM20100101_20151231.csv")
    //df.show()
    // 对于数据的处理操作和实现.选择PM_Dongsi不等于NA的数据进行操作
    import org.apache.spark.sql.functions._
    // 读取出来的是字符串的，不是NA对象的，所以，不能使用NA对象进行判断的。
    //  'PM_Dongsi   =!="NA" scala中的空格对应的对应的是一个对象的引用的。=!=对应的是一个方法的。
    //df.where(new Column("PM_Dongsi").=!=("NA")).show()
    // 也可以使用下面的语句的
    //df.where('PM_Dongsi   =!="NA").show()
    //  丢弃
    // 填充数据进行操作实现
    //  类似于mysql的case   when  otherwise的业务操作逻辑和实现
    df.select(
      'No as "id",'year,'month,'day,'hour,'season,
      when('PM_Dongsi ==="NA",Double.NaN)
        .otherwise('PM_Dongsi cast DoubleType)
        .as("pm")
    ).sort('pm.asc).show()
    // 要求原类型和转换之后的类型保持一致的操作的。字符串转化之后对应的还是字符串内容的
    df.na.replace("PM_Dongsi",Map("NA"->"NAN","NULL"->"null")).show()
  }
}
