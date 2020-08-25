package com.itheima.scala.exec1



import scala.actors.Actor

class ActorSender(actor:Actor)  extends Actor{
  override def act(): Unit = {
    //  发送异步消息，没有通信地址，对方无法接收的
    actor  ! "hello world"
  }
}
