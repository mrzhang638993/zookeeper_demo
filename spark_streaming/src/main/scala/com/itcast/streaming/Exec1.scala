package com.itcast.streaming

import org.apache.spark.SparkConf
import org.apache.spark.api.java.StorageLevels
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Exec1 {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("exec1")
      .setMaster("local[6]")
    // 创建streamingContext对象
    val context = new StreamingContext(conf, Seconds(5))
    context.sparkContext.setLogLevel("WARN")
    //  设置检查点
    context.checkpoint("F:\\works\\hadoop1\\zookeeper-demo\\spark_streaming\\src\\main\\scala\\checkpoint")
    // 监听服务端的服务，获取数据。获取数据源的输入流
    val stream: ReceiverInputDStream[String] = context.socketTextStream("192.168.1.104", 9999, StorageLevels.MEMORY_AND_DISK_SER)
    //  获取到的是整个的统计的结果的数据的
    //  -------------------------------------------
    //Time: 1601622710000 ms
    //-------------------------------------------
    //(baby,1)
    //(world,1)
    //(hello,1)
    //(test,1)
    //(many,1)
    //(good,2)
    val wordCount: DStream[(String, Int)] = stream.flatMap(_.split(" ")).map((_, 1))
      .updateStateByKey((values: Seq[Int], state: Option[Int]) => {
        var newValue = state.getOrElse(0)
        for (value <- values) {
          newValue += value
        }
        Option(newValue)
      })
    wordCount.print()
    context.start()
    context.awaitTermination()
  }
}
