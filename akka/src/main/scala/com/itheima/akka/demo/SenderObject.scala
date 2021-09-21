package com.itheima.akka.demo

import akka.actor.{Actor, ActorSelection}

object SenderObject extends Actor {
  //  之前的actor并发编程模式是act方法，使用的是loop+react方法的
  //  akka中使用偏函数就可以了
  override def receive: Receive = {
    case "start" => {
      println("SenderObject 接收到start消息")
      //  获取actor的路径如下：akka://actor系统的名称/user/actor的名称.获取的是本地的actor的，获取远端的actor需要ip地址操作
      val selection: ActorSelection = context.actorSelection("akka://actorSystem/user/receiverActor")
      val message: SubmitTaskMessage = SubmitTaskMessage("发送消息给消息接收者")
      selection ! message
    }
    case SuccessSubmitTaskMessage(msg) => {
      println(s"${msg}")
    }
  }
}
