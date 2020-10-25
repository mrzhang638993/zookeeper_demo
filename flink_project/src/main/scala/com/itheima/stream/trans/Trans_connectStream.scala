package com.itheima.stream.trans

import java.util.concurrent.TimeUnit

import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.source.SourceFunction

object Trans_connectStream {

  /**
   * connect  对应的可以连接2个datastream的数据进行操作实现的。
   * */
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    //  构建lang型数字的数据源信息
    val longValue: DataStream[Long] = env.addSource(new MyLongSource())
    //构建字符串数据源信息
    val stringValue: DataStream[String] = env.addSource(new MyStrSource())
    val value: ConnectedStreams[Long, String] = longValue.connect(stringValue)
    val transValue: DataStream[Any] = value.map(
      line1 => line1,
      line2 => line2
    )
    transValue.print()
    env.execute()
  }
}


class MyLongSource extends  SourceFunction[Long]{
  var isRunning=true;
  var  count=0L

  override def run(sourceContext: SourceFunction.SourceContext[Long]): Unit = {
      while(isRunning){
        count+=1
        sourceContext.collect(count)
        TimeUnit.SECONDS.sleep(1)
      }
  }

  override def cancel(): Unit = {
    isRunning=false
  }
}

class MyStrSource extends  SourceFunction[String]{
  var isRunning=true;
  var  count=0L
  override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
    while(isRunning){
      count+=1
      sourceContext.collect("str_"+count)
      TimeUnit.SECONDS.sleep(1)
    }
  }

  override def cancel(): Unit = {
    isRunning=false
  }
}