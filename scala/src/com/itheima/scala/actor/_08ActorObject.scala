package com.itheima.scala.actor

import scala.actors.{Actor, Future}

object  _08ActorObject {
   /**
    * 发送异步有返回值的消息
    * */
    object  MsgReceivor extends  Actor{
     override def act(): Unit ={
       loop{
         react{
           case Message(id,message)=>{
             println(s"${id}:${message}")
             sender ! Message(2,"hello world!")
           }
         }
       }
     }
   }

  case  class  Message(id:Int,message:String)

  def main(args: Array[String]): Unit = {
    //  Actor 可以发送和接收消息进行处理，本身具备了发送和接收消息的全部的功能实现的
    MsgReceivor.start()
    /*val  response=MsgReceivor !! Message(1,"hello world")
    //  将reponse中的对象进行apply操作获取得到想要的对象的
    if (response.apply().isInstanceOf[Message]){
      val message = response.apply().asInstanceOf[Message]
      println(message.id+"------"+message.message)
    }*/
    var future:Future[Any]=MsgReceivor !! Message(1,"hello world")
    //  判断数据是否接收到。对应的采用isSet方法
    while(future.isSet){
      if(future.apply().isInstanceOf[Message]){
        val message = future.apply().asInstanceOf[Message]
        println(message.id+"-----"+message.message)
      }
    }
  }
}
