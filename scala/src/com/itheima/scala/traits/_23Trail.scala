package com.itheima.scala.traits


/**
 * 特质的定义和实现
 * 特质相当于java中的接口的，可以将字段和方法结合封装起来的。
 * scala中的继承对应的是单个的继承的，一个类是可以实现任意数量的特质的。
 * scala都是采用的是extends实现接口的。使用多个特质的话采用的是with实现多个的。
 * */
object _23Trail {

  /**
   * 创建特质中创建抽象方法
   * */
  trait Logger {
    def logger(msg: String)
  }

  /**
   * 实现控制台消息管理操作
   * */
  class ConsoleLogger extends Logger {
    override def logger(msg: String): Unit = println(msg)
  }

  def main(args: Array[String]): Unit = {
    var nullPoninter = new ConsoleLogger
    nullPoninter.logger("null  pointer  exception")
  }
}
