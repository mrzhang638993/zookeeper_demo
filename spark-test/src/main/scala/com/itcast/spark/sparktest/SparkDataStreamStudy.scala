package com.itcast.spark.sparktest

import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

//对应的完成相关的dataStream的学习和实践操作
object SparkDataStreamStudy {
  def main(args: Array[String]): Unit = {
    //创建sparkSession对象信息
    val spark: SparkSession = SparkSession.builder().appName("testSql")
      .master("local[2]")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    //Scala的datasource的数据
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
      .map(att=>att.split(","))
      .map(attr=>Row(attr(0),attr(1).toInt))(coderSchema)
      .toDF()
    frame.write.save("spark-test/person.parquet")
  }
}
