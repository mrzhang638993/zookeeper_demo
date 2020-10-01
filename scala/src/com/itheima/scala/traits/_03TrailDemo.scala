package com.itheima.scala.traits


/**
 * 单例对象实现特质的操作实现
 * */
object _03TrailDemo {


  trait Logger{
    def log(msg:String)
  }

  /**
   * 单例对象实现特质
   * */
  object ConsoleLogger extends  Logger{
    override def log(msg: String): Unit = println("控制台信息输出"+msg)
  }

  def main(args: Array[String]): Unit = {
    ConsoleLogger.log("hello world")
  }
}
