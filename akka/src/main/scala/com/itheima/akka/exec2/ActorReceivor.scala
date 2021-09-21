package com.itheima.akka.exec2

import akka.actor.Actor


object ActorReceivor extends Actor {
  override def receive: Receive = {
    case msg => println(msg)
  }
}
