package com.itheima.stream.window

import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 翻滚窗口实现操作，不会不在重复数据的
 * 只会统计当前窗口的数据的，之前的数据是不会进行统计操作的。
 * */
object SlidingWindow {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    //   创建socketDataStream数据
    val socketDataStream: DataStream[String] = env.socketTextStream("cdh1", 9999)
    //  装换数据，认为数据的样本是这样的。1,2,2,2
    val countCars: DataStream[CountCar] = socketDataStream.map {
      line => {
        val arr: Array[String] = line.split(",")
        CountCar(arr(0).toInt, arr(1).toInt)
      }
    }
    //  根据红路灯进行分组操作
    val keyValue: KeyedStream[CountCar, Int] = countCars.keyBy(_.sen)
    // 划分时间窗口数据
    val windowValue: DataStream[CountCar] = keyValue.timeWindow(Time.seconds(5), Time.seconds(3)).sum(1)
    //  划分相关的数据
    windowValue.print()
    env.execute()
  }
}

/**
 * @param sen     传感器编号
 * @param carNum  车辆的数量
 * */
case class CountCar(sen:Int,carNum:Int)
