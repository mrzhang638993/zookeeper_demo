package com.itheima.spark.master

import java.util.Date

import akka.actor.Actor
import com.itheima.spark.common.{RegisterSuccessMessage, WorkInfo, WorkRegisterMessage, WorkerHeartBeatMessage}

import scala.collection.mutable

object MasterActor extends  Actor{

  override def preStart(): Unit = {
     //1.执行心跳检查
    //  导入时间单位隐式转换操作
     import scala.concurrent.duration._
    import  context.dispatcher
    // 2.过滤大于超时时间的worker
    context.system.scheduler.schedule(0 seconds,ConfigUtil.`master.check.heartbeat.interval` seconds) {
      val outdateMap: mutable.Map[String, WorkInfo] = regWorkerMap.filter {
        keyval => {
          val workInfo: WorkInfo = keyval._2
          //  获取最后一次更新的心跳检测的事件
          val time: Long = workInfo.lastHeartBeatTime
          val time1: Long = new Date().getTime
          if (time1 - time >= ConfigUtil.`master.check.heartbeat.timeout` * 1000) {
            true
          } else {
            false
          }
        }
      }
      // 3.移除超时的worker
      if(!outdateMap.isEmpty){
        // 表示根据key进行移除操作
        regWorkerMap --=outdateMap.map(_._1)
        // 4.对于现有的worker按照内存降序排序
        val workList: List[WorkInfo] = regWorkerMap.map(_._2).toList
        // 按照内存降序排序后的worker列表，根据内存降序排列
        val reverseList: List[WorkInfo] = workList.sortBy(_.mem).reverse
        //  需要对应的根据key来实现排序操作的
        println("心跳超时之后移除worker之后的list"+reverseList)
      }
    }
  }

  private val regWorkerMap=collection.mutable.Map[String,WorkInfo]()
  override def receive: Receive ={
    //  master的主要操作
    case WorkRegisterMessage(workId,cpu,mem)=>{
      // 保存注册信息
      println(s"masterActor 收到注册信息.${workId}:${cpu}:${mem}")
      regWorkerMap += workId->WorkInfo(workId,cpu,mem,new Date().getTime)
      sender ! RegisterSuccessMessage()
    }
    //  接收worker的心跳消息
    case WorkerHeartBeatMessage(workId,cpu,mem)=>{
      println(s"接收到workActor:${workId}的心跳消息")
      // 更新心跳的最新的时间
      regWorkerMap+=workId->WorkInfo(workId,cpu,mem,new Date().getTime)
      println(regWorkerMap)
    }
  }
}
