package com.itheima.akka

import akka.actor.Actor

object MasterActor  extends  Actor{
  override def receive: Receive = {
    //  创建新的模块构建操作
    case "connect"=>{
      println("master actor 接收到 connect信息")
      sender ! "success"
    }
  }
}
