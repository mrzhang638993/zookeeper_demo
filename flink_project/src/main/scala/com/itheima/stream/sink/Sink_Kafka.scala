package com.itheima.stream.sink

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}
import java.util.Properties

import com.itheima.stream.datasource.MysqlSource
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010
import org.apache.kafka.clients.CommonClientConfigs

/**
 * 数据落地到kafka消息队列中
 * */
object Sink_Kafka {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    //设置mysql作为读取的数据源
    val mysqlDataStream: DataStream[(Int, String, String, String)] = env.addSource(new MysqlSource())
    val stringData: DataStream[String] = mysqlDataStream.map(line => line._1 + "." + line._2 + "." + line._3 + "." + line._4)
    val kafakCluster = "cdh1:9092,cdh2:9092,cdh3:9092"
    val kafkaTopic = "kafkatopic1"
    val kafkaProducer = new FlinkKafkaProducer010[String](kafakCluster, kafkaTopic, new SimpleStringSchema())
    stringData.addSink(kafkaProducer)

    env.execute()
  }
}
