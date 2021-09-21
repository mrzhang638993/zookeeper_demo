package com.itheima.akka.schedule

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object _01ScheduleDemo {


  def main(args: Array[String]): Unit = {
    //  创建actorSystem
    val actorSystem: ActorSystem = ActorSystem("actorSystem", ConfigFactory.load())
    val receiveActor: ActorRef = actorSystem.actorOf(Props(ReceiveActor), "receiveActor")
    //final def schedule(
    //    initialDelay: FiniteDuration,
    //    interval:     FiniteDuration,
    //    receiver:     ActorRef,
    //    message:      Any)(implicit
    //    executor: ExecutionContext,
    //                       sender: ActorRef = Actor.noSender): Cancellable =
    //1.延时多长时间启动当时任务
    //2.延时任务的周期
    //3.指定接收者的角色
    //4.指定发送的消息
    import scala.concurrent.duration._
    //  导入隐式参数
    import actorSystem.dispatcher
    // 每个一秒执行一次发送消息来实现定时任务的。
    actorSystem.scheduler.schedule(0 seconds,
      1 seconds, receiveActor, "hello")

  }
}
