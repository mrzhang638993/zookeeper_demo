package com.itheima.scala.actor

import scala.actors.Actor

class Actor1  extends  Actor {
  //  打印1到10
  override def act(): Unit ={
    val list = (1 to 10).toList
    list.foreach((x:Int)=>println(x))
  }
}
