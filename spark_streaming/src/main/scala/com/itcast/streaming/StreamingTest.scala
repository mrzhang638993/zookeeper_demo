package com.itcast.streaming

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingTest {
  def main(args: Array[String]): Unit = {
    //sparkStreaming底层对应的就是sparkCore的核心机制。
    //local 模式下面需要设置2个以上的操作,否则无法获取资源的。
    val conf: SparkConf = new SparkConf().setAppName("StreamingTest").setMaster("local[2]")
    val context: SparkContext = new SparkContext(conf)
    //60秒钟执行一次运算
    val streamingContext = new StreamingContext(context, Seconds(2))
    streamingContext.sparkContext.setLogLevel("WARN")
    //sparkStreaming对应的版本变化是不会很大的,对应的使用流处理技术的话,使用的是不多的操作的
    //对应的获取数据源,执行数据源的操作实现和管理。
    val socketStringValue: ReceiverInputDStream[String] = streamingContext.socketTextStream("10.1.1.1", 8888,StorageLevel.MEMORY_AND_DISK)
   /* val value: DStream[(String, Int)] = socketStringValue.flatMap(_.split(",")).map((_, 1)).reduceByKey(_ + _)
    value.print()*/
    socketStringValue.print()
    //必须要启动机制,否则整个程序是不启动的。
    streamingContext.start()
    streamingContext.awaitTermination()
    //对应的后续java版本的代码的编写操作实现和管理逻辑处理。
    //Dstream的核心是time以及RDD列表的一种映射关系数据的,这个就是其本质的代码编程和实现管理。
  }
}
