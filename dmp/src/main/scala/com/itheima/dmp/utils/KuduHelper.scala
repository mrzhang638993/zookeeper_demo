package com.itheima.dmp.utils

import java.util.Date

import com.typesafe.config.ConfigFactory
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.kudu.Schema
import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

/**
 * 将KuduHelper的相关的功能给SparkSession以及Dataset。增加SparkSession以及Dataset对应的功能实现的。
 **/
class KuduHelper {
  private val config = ConfigFactory.load("kudu")
  private var kuduContext: KuduContext = _
  private var sparkSession: SparkSession = _
  private var dataset: Dataset[_] = _

  def this(sparkSession: SparkSession) = {
    // 实现相关的方法的功能和实现操作
    this()
    this.sparkSession = sparkSession
    // val kuduMaster : scala.Predef.String, sc : org.apache.spark.SparkContext, val socketReadTimeoutMs : scala.Option[scala.Long]
    val MASTER = config.getString("kudu.master")
    this.kuduContext = new KuduContext(MASTER, sparkSession.sparkContext, Some(900000))
  }

  def this(df: Dataset[_]) = {
    this(df.sparkSession)
    this.dataset = df
  }

  /**
   * 创建表进行操作的
   * 1.通过隐式转换将spark转换成为KuduHelper，
   * 2.调用KuduHelper的createKuduTable创建表的。
   **/
  def createKuduTable(tableName: String, schema: Schema, keys: List[String]) = {
    //  需要调用kuduContext创建对象的。创建的时候需要kudu的master的地址的，需要超时时间的
    if (kuduContext.tableExists(tableName)) {
      kuduContext.deleteTable(tableName)
    }
    // tableName : scala.Predef.String, schema : org.apache.kudu.Schema, options : org.apache.kudu.client.CreateTableOptions
    // 创建表执行操作实现
    val options = new CreateTableOptions()
    import scala.collection.JavaConverters._
    options.setNumReplicas(config.getInt("kudu.table.factor"))
      .addHashPartitions(keys.asJava, 2)
      kuduContext.createTable(tableName, schema, options)
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
   * 数据保存到dataset中
   **/
  def saveToKudu(tableName: String): Unit = {
    // 判断本方式是从dataset上调用的。
    //  保存数据
    if (dataset == null) {
      throw new RuntimeException("请从dataset上面开始")
    }
    import org.apache.kudu.spark.kudu._
    dataset.write
      .option("kudu.master", kuduContext.kuduMaster)
      .option("kudu.table", tableName)
      // 设置写的模式为追加模式
      .mode(SaveMode.Append)
      .kudu
  }
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
  implicit def DataFrameToKuduHelper(df: Dataset[_]): KuduHelper = {
    new KuduHelper(df)
  }

  /**
   * 获取格式化之后的字符串信息
   * 将当前的时间格式化操作的
   * SimpleDateFomat
   **/
  implicit def getParseDateString(): String = {
    FastDateFormat.getInstance("yyyyMMdd").format(new Date())
  }
}
