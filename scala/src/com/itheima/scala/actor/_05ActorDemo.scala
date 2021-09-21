package com.itheima.scala.actor

object _05ActorDemo {

  def main(args: Array[String]): Unit = {
    ActorSender.start()
    ActorReceiver.start()
  }

  //  使用loop加上react来复用线程提高执行效率
  object ActorSender extends Actor {
    override def act(): Unit = {
      while (true) {
        ActorReceiver ! "hello world"
      }
    }
  }

  object ActorReceiver extends Actor {
    override def act(): Unit = {
      //  loop接收的参数是一个函数的，接收为空，返回值也是为空的。{}可是可以调用方法的。
      loop {
        //  react的参数对应的是一个偏函数
        react({
          case msg: String => println(msg)
        })
      }
    }
  }
}
