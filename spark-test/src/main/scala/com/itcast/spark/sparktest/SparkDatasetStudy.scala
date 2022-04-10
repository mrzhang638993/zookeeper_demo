package com.itcast.spark.sparktest

import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

//对应的完成相关的dataset的视图查询操作实现的
object SparkDatasetStudy {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("testSql")
      .master("local[2]")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    /*val caseClassDS = Seq(Person("Andy", 32)).toDS()
    caseClassDS.show()
    val primitiveDS = Seq(1, 2, 3).toDS()
    primitiveDS.map(_ + 1).collect()*/
    //读取文件的内容转换成为person对象,读取json文件的时候json文件对应的是存在相关的属性信息的，单独的text文本是没有相关的信息的
    /*val path = "spark-test/person.txt"
    val peopleDS = spark.read.textFile(path)
    val personValue: Dataset[Person] = peopleDS
      .map(line => Person(line.split(",")(0), line.split(",")(1).toInt))
    personValue.show()*/
    //测试rdd交互操作实现
   /* import spark.implicits._
    val personDf: DataFrame = spark.read.textFile("spark-test/person.txt")
      .map(_.split(","))
      .map(arr => Person(arr(0), arr(1).toInt))
      .toDF()
    personDf.createOrReplaceTempView("person")*/
    //对应的获取得到青少年的数据信息,得到的是一个元祖类型的数据的。根据的是field的index来实现的
    //val teenager: DataFrame = spark.sql("select name,age from person where age between 13 and 19")
    //teenager.map(teen=>"Name:"+teen(0)).show()
    //对应的是根据field的name来实现相关的操作的。直接根据的是field的名称来实现相关的操作的。
    //teenager.map(teen=>"Name:"+teen.getAs[String]("name")).show()
    //下面使用隐式转换操作,将对应的数值转换成为对应的map类型的数据的,也是可以转换成为对应的map映射操作实现的。
    /*implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String,Any]]
    val value: Dataset[Map[String, Any]] = personDf.map(teenager => teenager.getValuesMap[Any](List("name", "age")))
    value.foreach(println(_))*/
    //程序指定schema的方式来实现相关的操作实现。
    val schema="name,age"
    val fields: Array[StructField] = schema.split(",").map(fieldName => fieldName match{
      case "name"=> StructField(fieldName, StringType, nullable = true)
      case "age"=>   StructField(fieldName, IntegerType, nullable = true)
    })
    val structType: StructType = StructType(fields)
    //使用row的时候需要执行如下的encoder的操作实现的。
    val coderSchema: ExpressionEncoder[Row] = RowEncoder(structType)
    import spark.implicits._
    val frame: DataFrame = spark.read.textFile("spark-test/person.txt")
      .map(_.split(","))
      .map(attr => Row(attr(0), attr(1).trim.toInt))(coderSchema).toDF()
    frame.createOrReplaceTempView("person")
    val sqlFrame: DataFrame = spark.sql("select name,age  from  person ")
    sqlFrame.foreach(att=>println("Name:"+att(0)))
    spark.close()
    //用户自定义的scala的functions，每一个记录对应的返回一个数值.聚合函数是多个数值返回一个数值的。
  }
}

case class Person(name:String,age:Long)
