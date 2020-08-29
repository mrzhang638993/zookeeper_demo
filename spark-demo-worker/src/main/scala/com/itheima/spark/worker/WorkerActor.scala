package com.itheima.spark.worker

import java.util.UUID

import akka.actor.{Actor, ActorSelection}
import com.itheima.spark.common.{RegisterSuccessMessage, WorkRegisterMessage, WorkerHeartBeatMessage}

import scala.util.Random

object WorkerActor extends  Actor{
  /**
   * 创建成员变量,masterActor
   * */
  private var  masterActor:ActorSelection=_
  /**
   * 创建成员变量，对应的是当前worker的id信息
   * */
  private var workId:String=_
  /**
   *  对应的是当前的worker对应的cpu的信息
   * */
  private  var cpu:Int=_
  /**
   * 对应的是当前的worker的内存信息
   * */
  private var  mem:Int=_
  /**
   * cpu核数列表
   * */
  private val cpu_list=List(1,2,3,4,6,8)
  /**
   * 内存列表
   * */
  private val mem_list=List(512,1024,2048,4096)
  /**
   * 在actor启动之后就会执行的代码的
   * */
  override def preStart(): Unit = {
    //  获取masterActor的引用。
    masterActor = context.actorSelection("akka.tcp://actorSystem@127.0.0.1:7000/user/masterActor")
    workId=UUID.randomUUID().toString
    val random = new Random()
    cpu=cpu_list(random.nextInt(cpu_list.size))
    mem=mem_list(random.nextInt(mem_list.size))
    val message: WorkRegisterMessage = WorkRegisterMessage(workId,cpu,mem)
    //  发送消息给masterActor进行注册操作
    masterActor ! message
  }
  override def receive: Receive = {
    case RegisterSuccessMessage()=>{
      println("workerActor 接收到注册成功消息")
      //  发行定时消息给master，需要定时任务的
      //  导入时间单位隐式转换操作
      import scala.concurrent.duration._
      //  隐式导入执行上下文
      import  context.dispatcher
      val random = new Random()
      cpu=cpu_list(random.nextInt(cpu_list.size))
      mem=mem_list(random.nextInt(mem_list.size))
      context.system.scheduler.schedule(0 seconds,ConfigUtil.`worker.heartbeat.interval` seconds){
        masterActor ! WorkerHeartBeatMessage(workId,cpu,mem)
      }
    }
  }
}
