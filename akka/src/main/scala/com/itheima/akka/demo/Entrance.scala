package com.itheima.akka.demo

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {

  def main(args: Array[String]): Unit = {
    //1.实现一个actor的trait.SenderObject以及ReceiverActor都已经实现了
    // 2. 创建actorSystem
    // name: String, setup: ActorSystemSetup
    val actorSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load())
    // 3. 加载actor
    val sendActor: ActorRef = actorSystem.actorOf(Props(SenderObject), name = "sendActor")
    val receiverActor: ActorRef = actorSystem.actorOf(Props(ReceiverActor), name = "receiverActor")
    sendActor ! "start"
  }
}
