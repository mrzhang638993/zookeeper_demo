package com.itcast.spark.sparktest.rt

import com.itcast.spark.sparktest.analysis.DateUtils
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.util.LongAccumulator

import java.util.Date
import scala.collection.immutable

/**
 * 实时的统计新增的购买量数据
 * */
object RealTimeBuy {
  //实时的购买量的数据统计操作和实现
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName(this.getClass.getName)
      .master("local")
      .getOrCreate()
    val bootstrapServers = "xc-online-kafka:9092"
    val groupId = "course_buy"
    val topicName = "course_buy"
    val rt_cv_key = "today::course_buy::current"
    val rt_cv_offset = "today::course_buy::offset"
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
    //配置sparkStreaming的检查点的数据操作，后续的数据处理会根据检查点实现相关的检查操作实现的。
    context.checkpoint("D:\\checkpoints\\courseBuy")
    //配置和使用kafka参数信息
    val kafkaParams = collection.mutable.HashMap.empty[String, Object]
    //配置bootstrapServers
    kafkaParams += ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers
    //配置groupId
    kafkaParams += ConsumerConfig.GROUP_ID_CONFIG -> groupId
    //配置k,v的序列化方式
    kafkaParams += ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    kafkaParams += ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    //创建spark的kafka消费者获取消费信息
    //需要指定spark  streaming
    //需要指定保存的一致性的配置
    //指定kafka的参数配置
    val partitions: immutable.Iterable[TopicPartition] = partitionToLong.map(part => part._1)
    val consumerStrategy: ConsumerStrategy[String, String] = ConsumerStrategies.Assign[String, String](partitions, kafkaParams, partitionToLong)
    //获取kafka中的流式数据信息
    val message: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](context, LocationStrategies.PreferConsistent, consumerStrategy)
    /**
     * 计算新增用户量的操作实现
     * 还是需要相关的手段来区分得到当天的数据信息的？当前的数据是不对的。
     * 对应的是新增用户量的代码操作实现需要进行关注的。
     *
     * 新增用户量保存到redis中，推荐使用的是redis的set的数据结构的,并且redis还需要设置过期时间的。
     *
     * */
      /**
       * 根据订单id实时的计算今日的购买量指标数据的。
       * */
    val courseBuy: DStream[(String, Int)] = message.map(_.value()).map(x => (x, 1))
    val uv: LongAccumulator = spark.sparkContext.longAccumulator("course_buy")
    val keyInfo = "course_buy_date_" + DateUtils.getDateStr(new Date().getTime, "yyyy-MM-dd")
    val str: String = redisUtil.getResultRedis(rt_cv_key, keyInfo)

    /**
     * 下面的这段代码从逻辑上是存在问题的。使用redis的set数据结构是可以解决问题的。
     * redis在指定的时间过期的设置需要学习一下。这个是很有用的操作的。
     * 将kafka的分区的offset保存到redis中是可以充分的发挥作用的,需要进行关注和实现操作
     * 需要关注的是sparkStreaming的共享变量的使用的。这个是一个很重要的技能的。
     * */
    if (str != null) {
      uv.add(str.toLong)
    }
    courseBuy.foreachRDD(iter => {
      if (iter.count() > 0) {
        uv.add(iter.count())
        redisUtil.storeOffsetRedis(rt_cv_key, keyInfo, uv.value)
      }
    })
    //将kafka的offset保存到redis中,用户后续的数据消费记录的。
    message.foreachRDD(rdd => {
      //判断rdd是否有数值
      if (!rdd.isEmpty()) {
        //获取rdd对应的分区信息
        val ranges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        //对分区数据进行遍历操作
        ranges.foreach(range => {
          val topic_partition_key = range.topic + "_" + range.partition
          //获取分区相关的数值,组装hash数值,
          redisUtil.storeOffsetRedis(rt_cv_offset, topic_partition_key, range.untilOffset)
        })
      }
    })
    //开启sparkStreaming的事务操作
    context.start()
    //等待程序结束操作。
    context.awaitTermination()
    context.stop(false, true)
  }
}
