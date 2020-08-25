package com.itheima.scala.exec1

object _02ExecObject {


  def main(args: Array[String]): Unit = {
    //  一个sender发送，另外的一个sender接收的。
    val receivor: ActorReceivor = new ActorReceivor
    val send: ActorSender = new ActorSender(receivor)
    send.start()
    receivor.start()
  }
}
