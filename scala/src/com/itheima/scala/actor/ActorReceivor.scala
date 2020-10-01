package com.itheima.scala.actor

import scala.actors.Actor

object ActorReceivor extends Actor {
  override def act(): Unit = {
    //  异步接收消息。PartialFunction对应的是一个偏函数进行操作
    while (true) {
      receive({
        //  模式匹配操作。匹配字符串消息
        case msg: String => println(msg)
      })
    }
  }
}
