package com.itheima.stream.exec1

import java.util.concurrent.TimeUnit
import java.util.{Date, UUID}

import com.itheima.stream.window.Order
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.Random

/**
 * 给数据增加水印操作实现
 * */
object Exec1 {
  // 编写代码, 计算5秒内，用户的订单总额
  //订单数据（订单ID——UUID、用户ID、时间戳、订单金额），要求 添加水印 来解决网络延迟问题
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置水印操作的时间管理
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    // 创建连接操作实现
    env.setParallelism(1)
    //  执行水印操作实现
    val sourceData: DataStream[Order] = env.addSource(new SourceFunction[Order] {
      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
        val order = Order(UUID.randomUUID().toString, Random.nextInt(3), Random.nextInt(101), new Date().getTime)
        ctx.collect(order)
        TimeUnit.SECONDS.sleep(1)
      }
      override def cancel(): Unit = {
      }
    })
    // 执行水印操作实现和管理
    val waterMarkValue: DataStream[Order] = sourceData.assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks[Order] {
      val delay = 5;
      var currentTimeStamp: Long = 0L;

      override def checkAndGetNextWatermark(lastElement: Order, extractedTimestamp: Long): Watermark = {
        //  执行水印操作实现
        //设置得到水印时间信息
        val watermark = new Watermark(currentTimeStamp - delay)
        val format: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
        println(s"水印时间:${format.format(watermark.getTimestamp)},事件事件:${format.format(currentTimeStamp)},系统时间:${format.format(System.currentTimeMillis())}")
        watermark
      }

      override def extractTimestamp(element: Order, previousElementTimestamp: Long): Long = {
        currentTimeStamp = Math.max(element.timestamp, previousElementTimestamp)
        currentTimeStamp
      }
    })
    // 执行分组聚合操作实现
    val orderValue: DataStream[Order] = waterMarkValue.keyBy(_.userId).timeWindow(Time.seconds(5)).reduce((p1, p2) => Order(p1.orderId, p1.userId, p1.money + p2.money, 0L))
    // 执行打印输出操作实现
    orderValue.print()
    env.execute()
  }
}
