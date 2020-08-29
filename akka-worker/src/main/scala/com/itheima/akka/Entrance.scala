package com.itheima.akka


import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {

  def main(args: Array[String]): Unit = {
        //println(System.getProperty("config.resource"))
        //println(System.getProperty("config.file"))
        //println(System.getProperty("config.url"))
       //var   prop=new Properties()
       // prop.setProperty("config.resource","classpath:application.conf")
        //System.setProperties(prop)
         //1.创建一个actor
         //2.创建一个actorSystem
         val actorSystemSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load())
         //3.发送消息给actor
         val workActor: ActorRef = actorSystemSystem.actorOf(Props(WorkerActor), "workActor")
         workActor  ! " setup "

  }
}
