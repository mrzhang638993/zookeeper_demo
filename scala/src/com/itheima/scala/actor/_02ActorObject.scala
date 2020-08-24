package com.itheima.scala.actor

import scala.actors.Actor

object _02ActorObject {

  //  单例模式的实现是scala自己实现的
  object Actor1  extends  Actor {
    override def act(): Unit ={
      //  业务逻辑代码布置在这个地方的？
      val list = (1 to 10).toList
      list.foreach((x:Int)=>println(x))
    }
  }

  //  单例模式的实现是scala自己实现的。整个程序中只有一个对象的。
  object Actor2 extends  Actor {
    override def act(): Unit = {
      val list = (11 to 20).toList
      list.foreach((x:Int)=>println(x))
    }
  }

  def main(args: Array[String]): Unit = {
    //  不用使用new进行创建的。
    Actor2.start()
    Actor1.start()
  }
}
