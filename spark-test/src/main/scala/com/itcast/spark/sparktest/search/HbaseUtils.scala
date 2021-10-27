package com.itcast.spark.sparktest.search

import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf

import scala.collection.mutable.ArrayBuffer

/**
 * <P>
 * TODO
 * </p>
 *
 */
class HbaseUtils extends Serializable {


  @transient
  var hBaseAdmin: HBaseAdmin = null;
  @transient
  var connection: Connection = null;

  // new HbaseUtils 时初始化
  init

  /**
   * 初始化hbase
   */
  def init = {
    if (connection == null && hBaseAdmin == null) {
      connection = ConnectionFactory.createConnection(getConf)
      hBaseAdmin = connection.getAdmin().asInstanceOf[HBaseAdmin]
    }
  }

  def getConf = {
    val conf = HBaseConfiguration.create()
    conf.set(HConstants.ZOOKEEPER_QUORUM, "xc-online-zk")
    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
    conf
  }

  /**
   * 获取任务配置
   *
   * @param tbName
   * @return
   */
  def getJobConf(tbName: String) = {
    val jobConf = new JobConf(getConf)
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, tbName)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf
  }

  /**
   * 创建表
   */
  def createTable(tbName: String, columnName: String) = {
    // 判断表是否存在
    if (!hBaseAdmin.tableExists(tbName)) {
      // 创建表描述
      val tbNameDescriptor = new HTableDescriptor(TableName.valueOf(tbName))
      // 创建列描述
      tbNameDescriptor.addFamily(new HColumnDescriptor(columnName))
      // 创建表
      hBaseAdmin.createTable(tbNameDescriptor)

    }
  }

  /**
   * 根据rowkey 查询
   *
   * @param hbaseTableName
   * @param rowKeys
   * @return
   */
  def getRowKeyList(hbaseTableName: String, rowKeys: ArrayBuffer[String]) = {
    import scala.collection.JavaConverters._
    val table = connection.getTable(TableName.valueOf(hbaseTableName))
    var keyBuffer = ArrayBuffer[Get]()
    for (rowKey <- rowKeys) {
      keyBuffer += new Get(Bytes.toBytes(rowKey))
    }
    val results = table.get(keyBuffer.asJava)
    var resultBuffer = ArrayBuffer[String]()

    for (result <- results) {
      var tmpBuffer = ArrayBuffer[String]()
      tmpBuffer += Bytes.toString(result.getRow)
      for (kv <- result.rawCells()) {
        val va = Bytes.toString(CellUtil.cloneValue(kv))
        tmpBuffer += va
      }
      resultBuffer += tmpBuffer.mkString(",") + "\t"
    }
    resultBuffer
  }


}
