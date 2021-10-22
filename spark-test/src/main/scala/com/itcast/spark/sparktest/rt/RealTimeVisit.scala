package com.itcast.spark.sparktest.rt

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{KafkaUtils, LocationStrategy}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 实时访问量数据统计操作实现
 *
 * */
object RealTimeVisit {

  def main(args: Array[String]): Unit = {
    //获取spark的相关的机制
    val spark: SparkSession = SparkSession.builder().master("local")
      .appName(this.getClass.getName)
      .getOrCreate()
    val bootstrapServers="xc-online-kafka:9092"
    val groupId="visit_topic"
    val topicName="visit_topic"
    val  rt_cv_key="today::course_visit::current"
    val rt_cv_offset="today::course_visit::offset"
    //使用redis的工具类实现相关的代码的实现和操作
    //初始化redis的初始化数据信息
    val redisUtil = new RedisUtils
    redisUtil.initRedisPool()
    /**
     * 使用redis获取分区的offset信息。需要判断数值是否存在
     * 存储的数据结构如下的：key:rt_cv_offset  field:topicName + "_" + partition  对应的整个的构成了整个的对象的。
     * */
    val partitionToLong: Map[TopicPartition, Long] = redisUtil.getLastCommittedOffsets(rt_cv_offset, topicName, 1)
    //创建流式编程环境,对应的参数是sparkContext数据的。整个是很关键的信息的。对应的间隔持续的时间是5秒钟的时间间隔的操作的
    val context = new StreamingContext(spark.sparkContext, Seconds(5))
    //配置和使用kafka参数信息
    val  kafkaParams=collection.mutable.HashMap.empty[String,Object]
    //配置bootstrapServers
    kafkaParams +=ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->bootstrapServers
    //配置groupId
    kafkaParams +=ConsumerConfig.GROUP_ID_CONFIG->groupId
    //配置k,v的序列化方式
    kafkaParams +=ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG->classOf[StringDeserializer]
    kafkaParams +=ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG->classOf[StringDeserializer]
    //创建spark的kafka消费者获取消费信息
    //需要指定spark  streaming
    //需要指定保存的一致性的配置
    //指定kafka的参数配置
    KafkaUtils.createDirectStream[String,String](context,LocationStrategy)
  }
}
