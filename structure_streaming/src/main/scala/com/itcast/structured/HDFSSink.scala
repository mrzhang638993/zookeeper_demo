package com.itcast.structured

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object HDFSSink {
  /**
   * 文件入库到hdfs中实现操作
   **/
  def main(args: Array[String]): Unit = {
    //  设置spark的数据
    val spark: SparkSession = SparkSession.builder().appName("hdfs_sink")
      .master("local[6]")
      .getOrCreate()
    // 从kafka中读取消息写入到hdfs中的
    import spark.implicits._
    val source: Dataset[String] = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "node01:9092,node02:9092,node03:9092")
      // 对应的这个地方，还可以配置通配符进行操作的。streaming-test*
      .option("subscribe", "streaming-test_2")
      .option("startingOffsets", "earliest")
      .load()
      .selectExpr("CAST(value as STRING)")
      .as[String]
    // 1::Toy Story (1995)::Animation|Children's|Comedy
    // 处理csv的数据。将DATASet[String]转化成为DATASet(id,name,category)
    val df: DataFrame = source.map(line => {
      val arr: Array[String] = line.split("::")
      (arr(0).toInt, arr(1).toString, arr(2).toString)
    }).as[(Int, String, String)].toDF("id", "name", "category")
    df.writeStream
      .format("parquet")
      .option("path", "F:\\works\\hadoop1\\zookeeper-demo\\structure_streaming\\src\\main\\scala\\com\\itcast\\structured\\movies")
      .start()
      .awaitTermination()
  }
}
