package  com.itheima.akka

import akka.actor.{Actor, ActorSelection}


object WorkerActor extends  Actor{
  override def receive: Receive = {
    //  实现akka编程操作
    case "setup"=>{
       println("workActor 接收到 setup消息")
      // 获取到masterActor的引用，后续的可以接收到消息的
      // 获取到master的引用路径。自己定义的actor对应的都是user的，其他的定义的不是。acterSystem对应的是Masteractor系统的名称
      // 获取actor的路径如下：akka.tcp://acterSystem@127.0.0.1:8888/user/actor的名字
      val masterActor: ActorSelection = context.actorSelection("akka.tcp://actorSystem@127.0.0.1:8888/user/masterActor")
      masterActor ! "connect"
    }
    case "success"=>{
      println("workActor  接收到master的  success信息")
    }
  }
}