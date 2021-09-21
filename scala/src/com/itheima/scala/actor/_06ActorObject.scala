package com.itheima.scala.actor

object _06ActorObject {

  def main(args: Array[String]): Unit = {
    ActorSender.start()
    ActorReceivor.start()
  }

  object ActorReceivor extends Actor {
    override def act(): Unit = {
      loop {
        react {
          case MsgBody(id, message) => {
            println(s"${id}:${message}")
            // sender获取消息的发送对象
            sender ! MsgBody("2", "接收到你的访问消息")
          }
        }
      }
    }
  }

  object ActorSender extends Actor {
    override def act(): Unit = {
      val body = MsgBody("1", "helloWorld")
      //  发送同步消息,进行消息的确认.同步消息处于阻塞状态的
      val response: Any = ActorReceivor !? body
      if (response.isInstanceOf[MsgBody]) {
        val body1 = response.asInstanceOf[MsgBody]
        println(body1.id + "-----" + body1.message)
      }
    }
  }
}
