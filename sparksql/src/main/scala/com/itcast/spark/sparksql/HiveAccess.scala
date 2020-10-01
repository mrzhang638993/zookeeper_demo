package com.itcast.spark.sparksql

import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructField, StructType}
import org.junit.Test

object HiveAccess {

  /**
   * sparkContext：早期使用spark的操作的话，对应的是sparkContext执行操作的
   * sqlContext执行的是spark的sql语句的编写操作实现的。
   * hiveContext执行的是spark对于hiveContext的支持操作的。
   * */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      //  提交到集群上面的，不需要指定对应的集群进行操作的
      .appName("hive.example")
      //  使用hive的操作的，需要支持如下的2个配置参数的。配置文件中可以读取到的。
      // 这个参数是一定需要配置的
      .config("spark.sql.warehouse.dir", "hdfs://node01:8020/dataset/hive")
      // 这个参数是不能修改的。thrift对应的也是一个rpc框架的。
      .config("hive.metastore.uris", "thrift://node03:9083")
      .enableHiveSupport()
      .getOrCreate()

    // 读取数据
       // 对应的数据文件需要上传到hdfs中，才可以在集群中进行操作的。对应的是一个外部系统的。
       // 使用dataFrame读取数据的。dataFrame需要结构信息的，需要配置结构信息的。
       // name: String,
       //    dataType: DataType,
       //    nullable: Boolean = true,
       //    metadata: Metadata = Metadata.empty
       var schema=StructType(List(
         StructField("name",StringType),
         StructField("age",IntegerType),
         StructField("gpa",FloatType)
       ));

    val df: DataFrame = spark.read
      .format("csv")
      //指定csv文件的分隔符信息
      .option("delimiter", "\t")
      // 指定schema约束信息
      .schema(schema)
      .load("hdfs://node01:8020/dataset/studenttab10k")
    //
    val resultDf: Dataset[Row] = df.where("age>50")

    //  数据写入到hive中的。
    resultDf.write
      .mode(SaveMode.Overwrite)
      .saveAsTable("spark03.student")
    spark.stop()
  }
  // 操作执行成功之后对应的原始数据保存到了mysql数据库的，真实的数据保存到了hive的数据库的
  // bin/spark-submit  --master spark://node01:7077  --class com.itcast.spark.sparksql.HiveAccess --executor-memory 512M --total-executor-cores 2  sparksql-1.0-SNAPSHOT.jar  集群上面运行操作
  //  hive
  // use  spark03
  // select *  from  student limit 10;
}
