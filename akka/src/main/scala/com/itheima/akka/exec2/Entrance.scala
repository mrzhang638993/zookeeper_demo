package com.itheima.akka.exec2

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {
  /**
   * 构建actor系统
   * */
  def main(args: Array[String]): Unit = {
    val actorSystemSystem: ActorSystem = ActorSystem.create("actorSystem", ConfigFactory.load())
    val actorSender: ActorRef = actorSystemSystem.actorOf(Props(ActorReceivor), "actorRecivior")
    import scala.concurrent.duration._
    import  actorSystemSystem.dispatcher
    actorSystemSystem.scheduler.schedule(0 seconds,1 seconds){
      actorSender ! "send a message every seconds"
    }
  }
}
