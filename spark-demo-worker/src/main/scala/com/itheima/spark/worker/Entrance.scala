package com.itheima.spark.worker

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {
  def main(args: Array[String]): Unit = {
    val actorSystemSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load("application"))
    //3.发送消息给actor
    val masterActor: ActorRef = actorSystemSystem.actorOf(Props(WorkerActor), "workerActor")
  }

    /**
     * 对应的worker需要定时的发送心跳信息给worker
     * */

}
