package com.itheima.akka.schedule

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object _02ScheduleDemo {

  object ActorReceivor extends  Actor{
    override def receive: Receive = {
      //  定期接收和处理消息
      case x=>println(x)
    }
  }

  def main(args: Array[String]): Unit = {
    val actorSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load())
    val actorReceiver: ActorRef = actorSystem.actorOf(Props(ActorReceivor), "actorReceiver")
    import  scala.concurrent.duration._
    //  导入隐式参数
    import  actorSystem.dispatcher
    // 使用自定义的schedule完成定时任务操作
    // initialDelay: FiniteDuration,
    //    interval:     FiniteDuration)(f: ⇒ Unit)
    actorSystem.scheduler.schedule(0 seconds,1 seconds){
       // 业务逻辑代码.自定义业务代码实现
      actorReceiver ! "hello  world "
    }
  }
}
