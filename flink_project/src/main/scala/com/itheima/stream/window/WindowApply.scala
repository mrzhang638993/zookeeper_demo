package com.itheima.stream.window

import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
/**
 * 自定义实现window的apply方法,实现更加复杂的数据。
 * */
object WindowApply {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    //   创建socketDataStream数据
    val socketDataStream: DataStream[String] = env.socketTextStream("cdh1", 9999)
    //  接受单词采用空格的方式实现操作管理实现
    val wordsValue: DataStream[(String, Int)] = socketDataStream.flatMap {
      text => {
        text.split(" ").map(item => (item, 1))
      }
    }
    val keyValue: KeyedStream[(String, Int), String] = wordsValue.keyBy(_._1)
    // 每3秒执行一次操作实现
    val windowStream: WindowedStream[(String, Int), String, TimeWindow] = keyValue.timeWindow(Time.seconds(3))
    //  执行操作实现
    val result: DataStream[(String, Int)] = windowStream.apply(new WindowFunction[(String, Int), (String, Int), String, TimeWindow] {
      override def apply(key: String, window: TimeWindow, input: Iterable[(String, Int)], out: Collector[(String, Int)]): Unit = {
        // 实现聚合计算操作
        val tuple: (String, Int) = input.reduce {
          (p1, p2) => (p1._1, p2._2 + p1._2)
        }
        // 进行元素的收集操作
        out.collect(tuple)
      }
    })
    result.print()
    env.execute()
  }
}
