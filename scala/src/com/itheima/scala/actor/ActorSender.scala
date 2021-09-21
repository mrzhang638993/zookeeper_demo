package com.itheima.scala.actor

import java.util.concurrent.TimeUnit

object ActorSender extends Actor {
  override def act(): Unit = {
    //  发送actor消息给actorReceive的信息的.这里面发送异步消息，不需要返回值
    while (true) {
      ActorReceivor ! "hello world"
      TimeUnit.SECONDS.sleep(1)
    }
  }
}
