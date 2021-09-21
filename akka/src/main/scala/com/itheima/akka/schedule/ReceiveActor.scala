package com.itheima.akka.schedule

import akka.actor.Actor

object ReceiveActor extends Actor {
  override def receive: Receive = {
    case msg => println(msg)
  }
}
