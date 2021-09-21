package com.itheima.akka.demo

import akka.actor.{Actor, ActorSelection}

object ReceiverActor extends Actor {
  override def receive: Receive = {
    case SubmitTaskMessage(msg) => {
      println(s"${msg}")
      val selection: ActorSelection = context.actorSelection("akka://actorSystem/user/sendActor")
      selection ! SuccessSubmitTaskMessage("接收到发送者的消息")
    }
  }
}
