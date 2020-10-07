package com.itheima.dmp.utils

import com.typesafe.config.ConfigFactory
import org.apache.kudu.Schema
import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class KuduHelper {
  private val config = ConfigFactory.load("kudu")
  private var kuduContext: KuduContext = _
  private var sparkSession: SparkSession = _

  def this(sparkSession: SparkSession) = {
    // 实现相关的方法的功能和实现操作
    this()
    this.sparkSession = sparkSession
    // val kuduMaster : scala.Predef.String, sc : org.apache.spark.SparkContext, val socketReadTimeoutMs : scala.Option[scala.Long]
    val MASTER = config.getString("kudu.master")
    this.kuduContext = new KuduContext(MASTER, sparkSession.sparkContext, Some(900000))
  }

  def this(df: Dataset[Any]) = {
    this(df.sparkSession)
  }

  /**
   * 创建表进行操作的
   * 1.通过隐式转换将spark转换成为KuduHelper，
   * 2.调用KuduHelper的createKuduTable创建表的。
   **/
  def createKuduTable(tableName: String, schema: Schema, keys: List[String], options: CreateTableOptions) = {
    //  需要调用kuduContext创建对象的。创建的时候需要kudu的master的地址的，需要超时时间的
    if (kuduContext.tableExists(tableName)) {
      kuduContext.deleteTable(tableName)
    } else {
      // tableName : scala.Predef.String, schema : org.apache.kudu.Schema, options : org.apache.kudu.client.CreateTableOptions
      // 创建表执行操作实现
      val options = new CreateTableOptions()
      import scala.collection.JavaConverters._
      options.setNumReplicas(config.getInt("kudu.table.factor"))
        .addHashPartitions(keys.asJava, 2)
      kuduContext.createTable(tableName, schema, options)
    }
  }

  /**
   * 读取kudu的数据
   **/
  def readKuduTable(tableName: String): Option[DataFrame] = {
    import org.apache.kudu.spark.kudu._
    // 需要考虑表不存在的情况下如何进行操作处理实现的
    if (kuduContext.tableExists(tableName)) {
      val kudu: DataFrame = sparkSession.read
        .option("kudu.master", kuduContext.kuduMaster)
        .option("kudu.table", tableName)
        .kudu
      Some(kudu)
    } else {
      None
    }
  }

  /**
   *
   **/
  //def
}

object KuduHelper {
  //  隐式转换,sparkSession--->KuduHelper DataFrame---->KuduHelper
  //  具体功能的开发
  //  1.创建表
  //  2.读取表
  //  3.写入数据
  implicit def sparkSessionToKuduHelper(spark: SparkSession): KuduHelper = {
    new KuduHelper(spark)
  }

  /**
   * DataFrame 转化成为KuduHelper
   **/
  implicit def DataFrameToKuduHelper(df: Dataset[Any]): KuduHelper = {
    new KuduHelper(df)
  }
}
