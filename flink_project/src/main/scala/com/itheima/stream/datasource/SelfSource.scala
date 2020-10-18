package com.itheima.stream.datasource

import java.util.UUID
import java.util.concurrent.TimeUnit

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.util.Random

/**
 * 自定义source数据源进行操作实现
 **/
object SelfSource {
  def main(args: Array[String]): Unit = {
    // 创建流的处理环境信息
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    // 处理数据进行操作。
    val customerDataStream: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      override def run(ctx: SourceFunction.SourceContext[Order]) = {
        // 循环一千次执行操作
        for (i <- 0 until 1000) {
          val id = UUID.randomUUID().toString
          //  获取0到2的数据
          val userId = Random.nextInt(3)
          // 生成订单金额
          val money: Int = Random.nextInt(100)
          //  获取时间
          val time: Long = System.currentTimeMillis()
          // 收集数据
          val order: Order = Order(id, userId, money, time)
          ctx.collect(order)
          // 睡眠.每隔1秒钟执行一次循环操作实现
          TimeUnit.SECONDS.sleep(1)
        }
      }

      override def cancel() = {
      }
    })
    customerDataStream.print()
    env.execute("order_job")
  }
}

/**
 * 创建订单样例类数据
 **/
case class Order(id: String, userId: Int, money: Int, time: Long)
