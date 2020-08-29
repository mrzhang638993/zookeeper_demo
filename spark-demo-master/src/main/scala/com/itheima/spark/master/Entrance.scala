package com.itheima.spark.master

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {

  def main(args: Array[String]): Unit = {
    val actorSystemSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load("application"))
    //3.发送消息给actor
    val masterActor: ActorRef = actorSystemSystem.actorOf(Props(MasterActor), "masterActor")
  }
}
