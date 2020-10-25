package com.itheima.stream.trans

import org.apache.flink.streaming.api.scala.{DataStream, SplitStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

/**
 * split操作可以实现将单个的数据流切换成为多个独立的数据流信息执行操作实现。
 *
 * */
object SplitAndSelect {
  /**
   * 测试split以及select操作函数
   * */
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    //  创建int类型的数据源环境
    val intValue: DataStream[Int] = env.fromCollection(List(1, 2, 3, 4, 5, 6))
    //  执行split操作，切分数据
    val destValue: SplitStream[Int] = intValue.split {
      (num: Int) => {
        num % 2 match {
          case 0 => List("even")
          case 1 => List("odd")
        }
      }
    }
    /**
     * 获取偶数进行分流操作实现
     * */
    val evenValue: DataStream[Int] = destValue.select("even")
    /**
     * 获取奇数流信息
     * */
    val oddValue: DataStream[Int] = destValue.select("odd")
    /**
     * 获取所有的流信息
     * */
    val allValue: DataStream[Int] = destValue.select("odd", "even")
    /**
     * 执行循环打印消息
     * */
    evenValue.print()
    print("==========3========")
    oddValue.print()
    print("==========2========")
    allValue.print()

    env.execute()
  }
}
