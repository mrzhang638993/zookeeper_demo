package com.itheima.scala.actor

import scala.actors.Actor

object  _06ActorObject {

   object ActorReceivor extends  Actor {
     override def act(): Unit = {
        loop{
          react{
            case  MsgBody(id,message)=>{
              println(s"${id}:${message}")
              // sender获取消息的发送对象
              sender ! MsgBody("2","接收到你的访问消息")
            }
          }
        }
     }
   }

  object ActorSender extends  Actor {
    override def act(): Unit = {
      val body = MsgBody("1","helloWorld")
      //  发送同步消息,进行消息的确认
      val  response=ActorReceivor !? body
      println(response.isInstanceOf[MsgBody])
    }
  }

  //  问题：为什么没有接受到返回的消息
  def main(args: Array[String]): Unit = {
    ActorSender.start()
    ActorReceivor.start()
  }
}
