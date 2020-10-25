package com.itheima.stream.window

import java.util.{Date, UUID}

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.util.Random
import java.util.concurrent.TimeUnit

import org.apache.commons.lang3.time.FastDateFormat
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
/**
 * 添加和执行水印的操作实现
 * */
object WaterMarkDemo {

  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    // 创建流执行环境操作实现
    // 设置触发的时间对应的是eventTime执行窗口的计算操作实现
    //  水印时间是不会影响到eventTime的
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //  创建自定义的数据源,
    val sourceValue: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
         val order=Order(UUID.randomUUID().toString,Random.nextInt(3),Random.nextInt(101), new Date().getTime)
         ctx.collect(order)
         TimeUnit.SECONDS.sleep(1)
      }
      override def cancel(): Unit = {
      }
    })
    //  给数据增加流水信息数据，执行相关的信息操作
    val waterMarkDataStream: DataStream[Order] = sourceValue.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[Order]() {
      //  设置延时时间信息
      val delayTime = 2000
      // 当前的时间戳信息
      var currentTimestamp = 0L
      //  获取水印时间
      override def getCurrentWatermark: Watermark = {
        //设置得到水印时间信息
        val watermark = new Watermark(currentTimestamp - delayTime)
        val format: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
        println(s"水印时间:${format.format(watermark.getTimestamp)},事件事件:${format.format(currentTimestamp)},系统时间:${format.format(System.currentTimeMillis())}")
        watermark
      }
      // 抽取当前时间戳信息
      override def extractTimestamp(element: Order, previousElementTimestamp: Long): Long = {
        currentTimestamp = Math.max(element.timestamp, previousElementTimestamp)
        currentTimestamp
      }
    })
    // 进行数据分流操作实现
    // keyBy执行用户id分组操作实现
    //timeWindow 执行时间窗口操作实现
    // 执行reduce操作实现
    val value: DataStream[Order] = waterMarkDataStream.keyBy(_.userId).timeWindow(Time.seconds(5)).reduce((p1, p2) => Order(p1.orderId, p1.userId, p1.money + p2.money, 0L))
    value.print()
    env.execute()
  }
}

case class Order(orderId:String,userId:Int,money:Long,timestamp:Long)

