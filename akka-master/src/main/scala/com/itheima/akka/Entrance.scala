package com.itheima.akka

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {


  def main(args: Array[String]): Unit = {
    //1.创建一个actor
    //2.创建一个actorSystem
    val actorSystemSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load("application"))
    //3.发送消息给actor
    val workActor: ActorRef = actorSystemSystem.actorOf(Props(MasterActor), "masterActor")
  }
}
