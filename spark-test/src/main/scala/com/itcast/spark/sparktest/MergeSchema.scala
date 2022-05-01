package com.itcast.spark.sparktest

import org.apache.spark.sql.SparkSession

//对应的执行模式合并操作实现
object MergeSchema {
  def main(args: Array[String]): Unit = {
    //执行模式合并的操作实现
    val spark: SparkSession = SparkSession.builder().appName("mergeSchema")
      .master("local[2]")
      .getOrCreate()
    //创建模式1
    import spark.implicits._
    val squaresDF = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
    squaresDF.write.parquet("data/test_table/key=1")
    //创建模式2
    val cubesDF = spark.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube")
    cubesDF.write.parquet("data/test_table/key=2")
    //进行模式合并操作，可以探测同一个basePath目录下面的多个子目录对应的schema的合并信息的。
    val mergedDF = spark.read.option("mergeSchema", "true").parquet("data/test_table")
    mergedDF.printSchema()
  }
}
