package com.itcast.spark.sparktest.search

import com.itcast.spark.sparktest.analysis.LearningCourseOnlineDwm
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.{Dataset, SparkSession}
import org.elasticsearch.spark.sql.EsSparkSQL

/**
 *数据保存到hbase以及es中进行数据保存操作实现
 *hbase中保存的是完整的数据的,后续的话可以实现根据数据来进行数据的索引操作实现的
 *es中存在的是rowkey的,hbase中存在的是完整的数据备份的。
 * 第一步创建和建立中间的表数据信息,后续操作从中间表读物数据来写入到es以及hbase中完成相关的操作的。
 * */
object InstantQueryStep2ToHbaseAndES {
  def main(args: Array[String]): Unit = {
      val sparkSession: SparkSession = SparkSession.builder().enableHiveSupport()
        .appName(this.getClass.getName)
        .master("local[2]").getOrCreate()
      val hbaseUtils=new HbaseUtils
      val hbaseTableName="LearningOnlineDetail"
      val hbaseColumnName="learning_online_detail"
      val esTabeleName:String="demo/doc"
      //创建表,如果表不存在的话
      hbaseUtils.createTable(hbaseTableName,hbaseColumnName)
      //得到相关的jobConf的相关的配置信息。hbase写入需要使用到相关的jobConf操作机制的
      val  conf=hbaseUtils.getJobConf(hbaseTableName)
      //表的信息
      val hiveTableName="data_course.learning_course_online_dwm"
      //日期信息
      val date_info="2019-11-11"
      //执行sql操作逻辑和实现
      import sparkSession.implicits._
      val learnDs: Dataset[LearningCourseOnlineDwm] = sparkSession.sql(
          s"""
             | select *  from  ${hiveTableName} where date_info=${date_info}
             |""".stripMargin).as[LearningCourseOnlineDwm]
      //数据保存到es中
      EsSparkSQL.saveToEs(learnDs,esTabeleName)
      //对应的进行put操作转换,对应的只有Rdd格式才是可以保存的,其他的格式是无法保存和实现的
      //hbase只是一个应用层的软件的,数据最终还是需要写入到hdfs文件系统之中的.
      learnDs.map(p => {
          val id: String = p.learning_course_online_id
          val rowKey: Array[Byte] = Bytes.toBytes(id)
          val put: Put = new Put(rowKey)
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("course_id"), Bytes.toBytes(p.course_id))
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("course_name"), Bytes.toBytes(p.course_name))
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("video_name"), Bytes.toBytes(p.video_name))
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("user_id"), Bytes.toBytes(p.user_id))
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("learn_time"), Bytes.toBytes(p.learn_time))
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("learn_count"), Bytes.toBytes(p.learn_count))
          put.addColumn(Bytes.toBytes(hbaseColumnName), Bytes.toBytes("date_info"), Bytes.toBytes(p.date_info))
          (new ImmutableBytesWritable, put)
      }).rdd.saveAsHadoopDataset(conf)
      sparkSession.close()
  }
}
