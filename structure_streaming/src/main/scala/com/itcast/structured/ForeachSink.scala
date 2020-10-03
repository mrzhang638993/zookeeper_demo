package com.itcast.structured

import java.sql.{Connection, DriverManager, Statement}

import org.apache.spark.sql._

object ForeachSink {
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
    /* df.writeStream
       .format("parquet")
       .option("path", "D:\\document\\works\\zookeeper-demo\\structure_streaming\\src\\main\\scala\\com\\itcast\\structured\\movies")
       .option("checkpointLocation","checkpoint")
       .start()
       .awaitTermination()*/
    // 数据落地到mysql数据库中实现操作

    df.writeStream
      .foreach(new MySqlWriter)
      .start()
      .awaitTermination()
  }
}

class MySqlWriter extends ForeachWriter[Row] {
  private val driver = "com.mysql.jdbc.Driver"
  private val url = "jdbc:mysql://192.168.1.203:3306/streaming-movies-result"
  private var collection: Connection = _
  private var stateMent: Statement = _
  private  val user="root"
  private val password="123456"

  override def open(partitionId: Long, version: Long): Boolean = {
    //  创建mysql的数据库连接
    try {
      Class.forName(driver)
      collection = DriverManager.getConnection(url,user,password)
      stateMent = collection.createStatement()
      true
    } catch {
      case e: Exception => false
    }
  }

  override def process(value: Row): Unit = {
    var  sql="insert into movies(id,name,category) values(";
    sql+=value.getAs[Int](0)+","
    sql+="'"+value.getString(1).replaceAll("\\(","").replaceAll("\\)","").replaceAll("'","’")+"'"+","
    sql+="'"+value.getString(2).replaceAll("'","’")+"'"
    sql+=")"
    println(sql)
    stateMent.executeUpdate(sql)
  }

  override def close(errorOrNull: Throwable): Unit = {
    collection.close()
  }
}
