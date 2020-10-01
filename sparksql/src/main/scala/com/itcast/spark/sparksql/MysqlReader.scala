package com.itcast.spark.sparksql

import java.util.Properties

import org.apache.spark.sql.{DataFrame, DataFrameReader, SaveMode, SparkSession}

/**
 * 读取mysql中的数据
 * */
object MysqlReader {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("mysql.example")
      // 集群模式下面是不需要设置master的，集群里面是有自己的master的。
      .master("local[6]")
      .getOrCreate()

    val prop=new Properties();
    prop.setProperty("user","spark")
    prop.setProperty("password","Spark123!")

    //spark.read.format("jdbc")
    //  .option("url", "jdbc:mysql://node01:3306/spark_test")
    //  .option("dbtable", "student")
    //  .option("user", "spark")
    //  .option("password", "Spark123!")
    //  .option("partitionColumn", "age")
    //  .option("lowerBound", 1)
    //  .option("upperBound", 60)
    //  .option("numPartitions", 10)
    //  .load()
    //  .show()

    //val predicates = Array(
    //  "age < 20",
    //  "age >= 20, age < 30",
    //  "age >= 30"
    //)
    //
    //val connectionProperties = new Properties()
    //connectionProperties.setProperty("user", "spark")
    //connectionProperties.setProperty("password", "Spark123!")
    //
    //spark.read
    //  .jdbc(
    //    url = "jdbc:mysql://node01:3306/spark_test",
    //    table = "student",
    //    predicates = predicates,
    //    connectionProperties = connectionProperties
    //  )
    //  .show()

    // spark.read.format("jdbc")
    //  .option("url", "jdbc:mysql://node01:3306/spark_test")
    //  .option("dbtable", "(select name, age from student where age > 10 and age < 20) as stu")
    //  .option("user", "spark")
    //  .option("password", "Spark123!")
    //  .option("partitionColumn", "age")
    //  .option("lowerBound", 1)
    //  .option("upperBound", 60)
    //  .option("numPartitions", 10)
    //  .load()
    //  .show()
    val df: DataFrame = spark.read
      //  url: String,
      //      table: String,
      //      columnName: String,
      //      lowerBound: Long,
      //      upperBound: Long,
      //      numPartitions: Int,
      //      connectionProperties: Properties
      .jdbc("jdbc:mysql://192.168.1.203:3306/spark_test", "student", prop)
      .toDF()
    df.show()
      spark.stop()
  }
}
