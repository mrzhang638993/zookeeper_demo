package com.itcast.spark.sparktest.rt_fact

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import java.util.Properties

object KafkaProducerTest {
  def main(args: Array[String]): Unit = {
    val  props : Properties = new Properties()
    props.put("bootstrap.servers", ConfigConstants.kafkaBrokers)
    props.put("batch.size", ConfigConstants.batchSize.asInstanceOf[Integer])
    props.put("linger.ms", ConfigConstants.lingerMs.asInstanceOf[Integer])
    props.put("buffer.memory", ConfigConstants.bufferMemory.asInstanceOf[Integer])
    props.put("key.serializer",ConfigConstants.kafkaKeySer)
    props.put("value.serializer", ConfigConstants.kafkaValueSer)
    val  producer :  Producer[String, String] = new KafkaProducer[String, String](props)
    val startTime : Long  = System.currentTimeMillis()
    for ( i <- 1 to 100) {
      producer.send(new ProducerRecord[String, String](ConfigConstants.kafkaTopics, "Spark", Integer.toString(i)))
    }
    println("消耗时间：" + (System.currentTimeMillis() - startTime))
    producer.close()
  }
}
