package com.itcast.kudu

import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.junit.Test

/**
 * 使用spark执行kudu代码操作实现
 **/
class KuduSpark {

  @Test
  def testSparkKudu(): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[6]")
      .appName("kudu").getOrCreate()
    // val kuduMaster : scala.Predef.String, sc : org.apache.spark.SparkContext, val socketReadTimeoutMs : scala.Option[scala.Long]
    val masterAddress = "192.168.1.205:7051,192.168.1.206:7051,192.168.1.207:7051";
    val kuduContext = new KuduContext(masterAddress, spark.sparkContext)
    // 执行kuduContext
    // 判断表是否存在，存在就删除表
    val TABLE_NAME = "student"
    val exists: Boolean = kuduContext.tableExists(TABLE_NAME)
    //  创建一张kudu的表
    if (exists) {
      kuduContext.deleteTable(TABLE_NAME)
    } else {
      // tableName : scala.Predef.String, schema : org.apache.spark.sql.types.StructType, keys : scala.Seq[scala.Predef.String], options : org.apache.kudu.client.CreateTableOptions
      val schema = StructType(
        StructField("name", StringType, nullable = false) ::
          StructField("age", IntegerType, nullable = false) ::
          StructField("gpa", DoubleType, nullable = false) :: Nil
      )
      val keys = Seq("name")
      import scala.collection.JavaConverters._
      val options = new CreateTableOptions().setRangePartitionColumns(List("name").asJava)
        .setNumReplicas(1)
      val table: Any = kuduContext.createTable(TABLE_NAME, schema, keys, options)
      println(table)
    }
  }
}
