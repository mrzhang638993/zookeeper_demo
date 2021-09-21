package com.itheima.scala.actor

object _07ActorObject {

  def main(args: Array[String]): Unit = {
    MsgSender.start()
    MsgReveivor.start()
  }

  case class Message(message: String, company: String)

  /**
   * 发送异步无返回消息
   * 使用的是对象的方式发送和接收消息
   * */
  object MsgSender extends Actor {
    override def act(): Unit = {
      //  发送异步消息。可以确认只有一个对象进行消息的接收的。
      MsgReveivor ! Message("hello", "测试消息通讯")
    }
  }

  object MsgReveivor extends Actor {
    override def act(): Unit = {
      // 接收异步消息,使用loop可以循环调用react方法来处理消息
      loop {
        react {
          case Message(message, company) => println(message + "------" + company)
        }
      }
    }
  }
}
