package  com.itheima.akka

import akka.actor.Actor


object WorkerActor extends  Actor{
  override def receive: Receive = {
    //  实现akka编程操作
    case x=>println(x+"123456")
  }
}