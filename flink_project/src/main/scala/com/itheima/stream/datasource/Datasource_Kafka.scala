package com.itheima.stream.datasource

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.kafka.clients.CommonClientConfigs

/**
 * 使用kafka作为数据源读取数据
 **/
object Datasource_Kafka {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //  读取数据.指定kafka的相关信息，创建kafka执行操作实现
    val kafakCluster = "cdh1:9092,cdh2:9092,cdh3:9092"
    val kafkaTopic = "kafkatopic"
    // topic: String, valueDeserializer: DeserializationSchema[T], props: Properties
    val prop = new Properties()
    prop.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafakCluster)
    val kafkaConsumer = new FlinkKafkaConsumer010[String](kafkaTopic, new SimpleStringSchema(), prop)
    // 设置数据源
    import org.apache.flink.api.scala._
    val kafkaDataStr: DataStream[String] = env.addSource(kafkaConsumer)
    kafkaDataStr.print()
    env.execute("kafka_consumer")
  }
}
