package com.itheima

import java.util
import java.util.concurrent.TimeUnit

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.hadoop.hive.ql.exec.vector.TimestampUtils


/**
 * flink checkpoint操作实现
 * */
object CheckPointDemo {
  def main(args: Array[String]): Unit = {

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
class MyWindowAndCheckPoint extends  WindowFunction[Msg,Long,Tuple,TimeWindow]  with ListCheckpointed[UdfState]{
  //  制定求和总数操作
  var total:Long=0L
  override def apply(key: Tuple, window: TimeWindow, input: Iterable[Msg], out: Collector[Long]): Unit = {
    var count=0
    for(msg<-input){
       count+=1
    }
    total+=count
    // 收集数据
    out.collect(total)
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

