package com.itcast.spark.sparktest.rt

import com.itcast.spark.sparktest.analysis.DateUtils
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream, MapWithStateDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}
import org.apache.spark.util.LongAccumulator

import java.util.Date
import scala.collection.immutable

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
    val partitions: immutable.Iterable[TopicPartition] = partitionToLong.map(part => part._1)
    val consumerStrategy: ConsumerStrategy[String, String] = ConsumerStrategies.Assign[String, String](partitions, kafkaParams, partitionToLong)
    //获取kafka中的流式数据信息
    val message: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](context, LocationStrategies.PreferConsistent, consumerStrategy)
    //处理数据信息,根据课程id,sessionId,ip去重,实现实时操作
    //根据课程id,sessionId,ip实现计数功能操作实现
    val userVisit: DStream[(String, Int)] = message.map(_.value()).map(_.split("\t")).map(arr => {
      //获取得到groupKey的操作的数据结果信息,对应的三个作为一组的话认为是一个相同的浏览人数的信息
      val groupkey: String = arr(0) + "_" + arr(2) + "+" + arr(3)
      (groupkey, 1)
    })
    //定义去重逻辑和操作实现,其中State[Int]对应的是处理分布式情况下面的数据处理逻辑操作
    //流式环境下面的操作需要的是state实现数据的协调操作实现的。
    val mapWithStateMethod=(word:String,count:Option[Int],state:State[Int])=>{
        //计数之间进行数据的更新操作和实现
        val sum=count.getOrElse(0)+state.getOption().getOrElse(0)
        //更新计数的数据信息
        state.update(sum)
        //返回更新之后的数据的,返回更新之后的数据操作结果来实现数据的更新操作和实现的
        (word,sum)
    }
    //对应的还是一个计数操作的
    val destUserVisit: MapWithStateDStream[String, Int, Int, (String, Int)] = userVisit.mapWithState(StateSpec.function(mapWithStateMethod))
    //共享变量,获取spark的共享变量实现操作
    val uv: LongAccumulator = spark.sparkContext.longAccumulator("uv")
    val keyInfo="visit_date_"+DateUtils.getDateStr(new Date().getTime,"yyyy-MM-dd")
    //对应的完成相关的数据统计操作和实现逻辑
    val str: String = redisUtil.getResultRedis(rt_cv_key, keyInfo)
    if(str!=null){
      uv.add(str.toLong)
    }
    //聚合进行计数操作
    //对于uv的数据进行聚合操作实现
    destUserVisit.foreachRDD(iter=>{
       //数量大于0的话执行聚合操作。
       if(iter.count()>0){
         uv.add(iter.count())
         redisUtil.storeOffsetRedis(rt_cv_key,keyInfo,uv.value)
       }
    })
  }
}
