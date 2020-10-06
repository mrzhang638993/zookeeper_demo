package com.itcast.kudu

import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.client.KuduClient.KuduClientBuilder
import org.apache.kudu.client.{CreateTableOptions, KuduClient}
import org.apache.kudu.{Schema, Type}
import org.junit.Test

class KuduApi {
  /**
   * kudu 创建table
   * kudu对应的是表的形态的
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
      setRangePartitionColumns(List("key").asJava)
      .setNumReplicas(1)
    //  创建表
    // tablet是最终存储数据的，可以存在多个的。之间可以存在主从的概念的。
    // name: String, schema: Schema, builder: CreateTableOptions
    val tableName = "simple"
    val id: String = kuduClient.createTable(tableName, schema, options).getTableId
    println(id)
  }
}
