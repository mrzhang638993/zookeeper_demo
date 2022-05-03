package com.itcast.spark.sparktest

import org.apache.spark.sql._

/**
 * hive相关的学习操作实现
 * */
object HiveStudy {
  def main(args: Array[String]): Unit = {
    val warehouseLocation="E:\\idea_works\\java\\zookeeper_demo"
    //创建相关的SparkSession。
    val spark: SparkSession = SparkSession.builder().appName("hiveStudy")
      .master("local[2]")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    val textValue: Dataset[String] = spark.read.option("lineSep", ",").textFile("spark-test/person.txt")
    val value: Dataset[String] = textValue.map(text => {
      val strings: Array[String] = text.split(",")
      strings.mkString("\001")
    })
    //write option 配置,text仅支持一个列的，但是实际上是有两个列的数据的
    value.write.mode(SaveMode.Overwrite).text("spark-test/kv1")
    //创建hive相关的表结构信息
    spark.sql("drop table if  exists src1 ")
    //使用的是\001进行分割的,使用这个分割可以自动的按照位置进行加载的，如果使用其他的分割方式是否可以？
    spark.sql("CREATE TABLE IF NOT EXISTS src1 (name String,age INT) USING hive")
    spark.sql("LOAD DATA LOCAL INPATH 'spark-test/kv1' overwrite  INTO TABLE src1")
   /* val frame: DataFrame = spark.sql("select * from src1")
    spark.sql("select  count(*)  from src1 ").show()*/
   /*val frame: DataFrame = spark.sql("select name,age from  src1 where age>30  order by name")
    //使用sql查询得到的结构是ROW的数据结构的。
    val value1: Dataset[String] = frame.map {
      case Row(name, age) => s"name: $name, Age: $age"
    }*/
    //value1.show()
    //使用集合的方式来创建相关的dataFrame的数据结构
   val frame: DataFrame = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
    frame.createOrReplaceTempView("records")
    spark.sql("select *  from  records r join src1 s on s.age=r.key ").show()
    //创建hive支持的parquet的格式表，spark支持的话,对应的是using hive命令来创建的
    spark.sql("CREATE TABLE if not exists hive_records(name String,age INT) STORED AS PARQUET")
    //获取spark的table的数据
    val frame1: DataFrame = spark.table("src1")
    frame1.write.mode(SaveMode.Overwrite).saveAsTable("hive_records")
    spark.sql("SELECT * FROM hive_records").show()
    //数据存储到对应的指定的数据目录之中进行存储
    val dataDir="spark-test/data"
    spark.range(10).write.mode(SaveMode.Overwrite).parquet(dataDir)
    spark.sql(s"CREATE EXTERNAL TABLE if not exists hive_bigints(id bigint) STORED AS PARQUET LOCATION '$dataDir'")
    spark.sql("SELECT * FROM hive_bigints").show()
    //设置spark相关的任务参数
    spark.sqlContext.setConf("hive.exec.dynamic.partition", "true")
    spark.sqlContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")
    frame1.write.partitionBy("name").format("hive").saveAsTable("hive_part_tbl")
    spark.sql("SELECT * FROM hive_part_tbl").show()
  }
}

case class Record(key: Int, value: String)

