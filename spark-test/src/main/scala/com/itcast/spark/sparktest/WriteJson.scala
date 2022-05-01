package com.itcast.spark.sparktest

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object WriteJson {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("sinkJson")
      .master("local[2]")
      .getOrCreate()
    val schema="name,age"
    val fields: Array[StructField] = schema.split(",").map(fieldName => fieldName match{
      case "name"=> StructField(fieldName, StringType, nullable = true)
      case "age"=>   StructField(fieldName, IntegerType, nullable = true)
    })
    val structType: StructType = StructType(fields)
    val frame: DataFrame = spark.read.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .schema(structType)
      .csv("spark-test/person.csv")
    frame.write
      //需要在输出端指定对应的输出时间格式信息的。
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .mode(SaveMode.Overwrite).json("spark-test/jsonTest")
  }
}
