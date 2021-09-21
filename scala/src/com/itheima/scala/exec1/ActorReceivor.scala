package com.itheima.scala.exec1

class ActorReceivor extends Actor {
  override def act(): Unit = {
    loop(
      react {
        case msg: String => println(msg)
        case _ => println("没有接受到任何的消息")
      }
    )
  }
}
