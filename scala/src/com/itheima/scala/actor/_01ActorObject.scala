package com.itheima.scala.actor

object  _01ActorObject {

  def main(args: Array[String]): Unit = {
      var actor1=new Actor1
      var  actor2=new Actor2
     actor1.start()
    actor2.start()
  }
}
