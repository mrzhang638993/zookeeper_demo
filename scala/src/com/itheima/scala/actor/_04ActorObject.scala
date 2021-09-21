package com.itheima.scala.actor

object _04ActorObject {
  /**
   * ActorSender不断的发送消息
   * ActorReceivor不断的接收消息
   * 不断的发送消息和接收消息
   * */
  def main(args: Array[String]): Unit = {
    ActorSender.start()
    ActorReceivor.start()
  }
}
