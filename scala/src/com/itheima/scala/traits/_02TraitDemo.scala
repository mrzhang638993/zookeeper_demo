package com.itheima.scala.traits

/**
 * 一个类可以实现多个特质的。可以实现多个接口的内容的
 * */
object _02TraitDemo {

  // 创建2个特质的
  trait  MessageSender{
     def  send(msg:String):Unit
  }

  trait  MessageReceiver{
    //返回值是String的内容的
     def receive():String
  }

  // 实现相关的特质的代码
  class MessageWorker extends  MessageSender with  MessageReceiver{
    override def send(msg: String): Unit = println(msg)

    override def receive(): String = "你好，我的名字叫一个好人"
  }

  def main(args: Array[String]): Unit = {
      var  messageWorker =new MessageWorker
      messageWorker.send("你好!我是一个坏人")
    val str = messageWorker.receive()
     println(str)
  }
}
