package com.itheima

import java.util
import java.util.concurrent.TimeUnit

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
/**
 * flink checkpoint操作实现
 * */
//1. 流处理环境
//2. 开启checkpoint,间隔时间为6s
//3. 设置checkpoint位置
//4. 设置处理时间为事件时间
//5. 添加数据源
//6. 添加水印支持
//7. keyby分组
//8. 设置滑动窗口,窗口时间为4s
//9. 指定自定义窗口
//10. 打印结果
//11. 执行任务
object CheckPointDemo {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置6秒钟进行一次checkpoint检查操作实现
    env.enableCheckpointing(6000)
    // 设置checkpoit的位置
    env.setStateBackend(new FsStateBackend("file:///E:\\idea_works\\java\\zookeeper_demo\\flink_project\\src\\main\\scala\\com\\itheima\\checkpoint"))
    // 设置处理时间
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    // 增加数据源操作
    val sourceValue: DataStream[Msg] = env.addSource(new MySourceFunction)
    //  增加水印操作
    val waterValue: DataStream[Msg] = sourceValue.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[Msg] {
      override def getCurrentWatermark: Watermark = {
        new Watermark(System.currentTimeMillis())
      }
      override def extractTimestamp(element: Msg, previousElementTimestamp: Long): Long = {
        System.currentTimeMillis()
      }
    })
    // 执行分组操作实现
    val tupleValue: KeyedStream[Msg, Tuple] = waterValue.keyBy(0)
    // 窗口时间为4秒，滑动时间为1秒。
    val windowValue: WindowedStream[Msg, Tuple, TimeWindow] = tupleValue.timeWindow(Time.seconds(4), Time.seconds(1))
    //  执行window的复杂小左
    val applyValue: DataStream[Long] = windowValue.apply(new MyWindowAndCheckPoint)
    // 执行打印输出
    applyValue.print()
    env.execute()
  }
}
// 1. 自定义样例类(id: Long, name: String, info: String, count: Int)
//2. 自定义数据源,继承RichSourceFunction
//3. 实现run方法, 每秒钟向流中注入10000个样例类
case class  Msg(id:Long,name:String,info:String,count:Int)
// 自定义数据源,创建数据
class MySourceFunction extends  RichSourceFunction[Msg]{
  var isRunning=true;
  override def run(ctx: SourceFunction.SourceContext[Msg]): Unit = {
     while(isRunning){
        for(i<- 0 until 10000){
          val msg: Msg = Msg(1L, "name_" + i, "test_info", 1)
          ctx.collect(msg)
        }
       TimeUnit.SECONDS.sleep(1)
     }
  }
  override def cancel(): Unit = {
    isRunning=false
  }
}
// 1. 继承Serializable
//2. 为总数count提供set和get方法
// 自定义状态。
//  定义状态类，体现的是维护的状态数据的
class UdfState extends  Serializable{
  private var count=0L
   def setState(s:Long){
      count=s
  }
  def  getState(): Long ={
     count
  }
}

// 1. 继承WindowFunction
//2. 重写apply方法,对窗口数据进行总数累加
//3. 继承ListCheckpointed
//4. 重写snapshotState,制作自定义快照
//5. 重写restoreState,恢复自定义快照
// 自定义window he检查点数据
// IN, OUT, KEY, W <: Window
//  使用ListCheckpointed 来维护状态数据操作的.
class MyWindowAndCheckPoint extends  WindowFunction[Msg,Long,Tuple,TimeWindow]  with ListCheckpointed[UdfState]{
  //  制定求和总数操作
  var total:Long=0L
  override def apply(key: Tuple, window: TimeWindow, input: Iterable[Msg], out: Collector[Long]): Unit = {
    var count=0
    for(msg<-input){
       count+=1
    }
    //  维护状态数据，定期将状态数据写入到snapshotState中实现的。
    total+=count
    // 收集数据
    out.collect(count)
  }
  //自定义快照数据
  override def snapshotState(checkpointId: Long, timestamp: Long): util.List[UdfState] = {
    val udfState=new util.ArrayList[UdfState]()
    //  确定快照的数据
    val state = new UdfState
    // 将total数据写入到checkpoint中的
    state.setState(total)
    udfState.add(state)
    udfState
    // 返回数据
  }
  // 恢复快照数据
  override def restoreState(state: util.List[UdfState]): Unit = {
    val state1: UdfState = state.get(0)
    // 从checkpoint中恢复数据给total的。
    total=state1.getState()
  }
}

