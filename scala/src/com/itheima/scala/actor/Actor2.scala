package com.itheima.scala.actor

class Actor2 extends Actor {
  override def act(): Unit = {
    val list = (11 to 20).toList
    list.foreach((x: Int) => println(x))
  }
}
