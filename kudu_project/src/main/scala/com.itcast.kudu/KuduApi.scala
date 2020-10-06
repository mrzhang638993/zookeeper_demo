package com.itcast.kudu

import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.client.KuduClient.KuduClientBuilder
import org.apache.kudu.client._
import org.apache.kudu.{Schema, Type}
import org.junit.Test

class KuduApi {
  /**
   * kudu 创建table
   * kudu 创建表的话，对应的只需要和master进行关联操作即可实现的。
   **/
  @Test
  def createTable(): Unit = {
    // 创建表
    // masterAddresses: String
    val masterAddress = "192.168.1.205:7051";
    // 连接到kube的时候对应的是返回的是一个集群的cluster的。根据master连接到对应的server进行数据处理操作的
    val kuduClient: KuduClient = new KuduClientBuilder(masterAddress).build()
    // 创建表的schema
    // columns: List[ColumnSchema]
    // ColumnSchema
    val columnsList = List(
      new ColumnSchemaBuilder("key", Type.STRING).key(true).build(),
      new ColumnSchemaBuilder("value", Type.STRING).key(false).build()
    )
    import scala.collection.JavaConverters._
    val schema = new Schema(columnsList.asJava)
    // 指定分区.指定分区操作操作.默认的分区数是3的。这里只要一个key的
    val options: CreateTableOptions = new CreateTableOptions().
      //  kudu不需要指定分区方式进行操作的。
      setRangePartitionColumns(List("key").asJava)
      .setNumReplicas(1)
    //  创建表
    // tablet是最终存储数据的，可以存在多个的。之间可以存在主从的概念的。
    // name: String, schema: Schema, builder: CreateTableOptions
    val tableName = "simple"
    val id: String = kuduClient.createTable(tableName, schema, options).getTableId
    println(id)
  }

  /**
   * 数据的操作和插入请求
   * 写入数据的时候，对应的返回相关的server的。同时需要对应的能够解析出来相关的IP地址的。
   * 需要在host文件中增加如下的配置的：192.168.1.206  cdh2.itcast.cn
   * 192.168.1.207  cdh3.itcast.cn
   **/
  @Test
  def insertRecord(): Unit = {
    // masterAddresses: String
    val masterAddress = "192.168.1.205:7051";
    // 连接到kube的时候对应的是返回的是一个集群的cluster的。根据master连接到对应的server进行数据处理操作的
    val kuduClient: KuduClient = new KuduClientBuilder(masterAddress).build()
    //  可以得到表对象表示对于表进行操作实现和管理的
    val table: KuduTable = kuduClient.openTable("simple")
    val insert: Insert = table.newInsert()
    val row: PartialRow = insert.getRow
    row.addString(0, "A")
    row.addString(1, "1")
    // 创建session对象进行操作实现
    val session: KuduSession = kuduClient.newSession()
    session.apply(insert)
    session.close()
  }

  /**
   * 菲关系型的数据库，查询称之为扫描器的。
   **/
  @Test
  def testSearch(): Unit = {
    // masterAddresses: String
    val masterAddress = "192.168.1.205:7051";
    // 连接到kube的时候对应的是返回的是一个集群的cluster的。根据master连接到对应的server进行数据处理操作的
    val kuduClient: KuduClient = new KuduClientBuilder(masterAddress).build()
    val table: KuduTable = kuduClient.openTable("simple")
    val columns = List("key", "value")
    import scala.collection.JavaConverters._
    val scanner: KuduScanner = kuduClient.newScannerBuilder(table).
      // 可以设置多个列的形式的。
      setProjectedColumnNames(columns.asJava)
      .build()
    // 开始设置扫描的条件执行扫描观察操作实现的
    while (scanner.hasMoreRows) {
      // 一次获取的是一整个的tablet中的数据的
      val tabletIter: RowResultIterator = scanner.nextRows()
      //  针对于获取到的整个的tablet的数据的
      while (tabletIter.hasNext) {
        val result: RowResult = tabletIter.next()
        println(result.getString(0), result.getString(1))
      }
    }
  }
}
