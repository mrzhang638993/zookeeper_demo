package com.itcast.streaming

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object  StreamingWordCount {

  /**
   * streaming统计的是每一个批次的数据的。
   *
   * spark  streaming对应的是基于spark core的
   * spark streaming对应的是sparkStreaming的
   * */
  def main(args: Array[String]): Unit = {
        // 初始化环境，读取对应的数据
        val conf: SparkConf = new SparkConf().setAppName("streamingWordCount")
          .setMaster("local[6]")
    // val context = new SparkContext(conf)
    //  conf ，batchDuration对应的是按照时间点的批次数据进行处理的。下面指定的是每一批对应的是1秒
    //  创建了sparkContext之后继续创建StreamingContext的数据的
    val streaming = new StreamingContext(conf, Seconds(5))
    streaming.sparkContext.setLogLevel("WARN")
    // DStream  可以处理为RDD操作的。
    // socketTextStream  创建DStream，监听socket的输入，当做文本来处理
    //  sparkContext.textFile创建rdd操作的。Dstream来自于sprak Streaming
    // Dstream 是RDD的母集的。内部流转的是RDD的
    val lines: ReceiverInputDStream[String] = streaming.socketTextStream("192.168.1.104", 9999, storageLevel=StorageLevel.MEMORY_AND_DISK_SER)
       // 数据的处理
      //  1.数据的转换，句子拆分为单词
      val words: DStream[String] = lines.flatMap(_.split(" "))
    val value: DStream[(String, Int)] = words.map((_, 1)).reduceByKey((priv, next) => priv + next)
    // 打印Dstream中的所有的内容的。
    value.print()
    // 程序执行操作
    streaming.start();
    streaming.awaitTermination()
      //  转换单词
    // 单词统计操作实现
    // 显示启动。
  }
}
