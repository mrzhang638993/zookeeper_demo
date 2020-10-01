package com.itheima.scala.actor

object  _03ActorDemo {

  def main(args: Array[String]): Unit = {
       ActorReceivor.start()
       ActorSender.start()
  }
}
