package com.itheima.scala.traits

import java.util.Date

object _05TrailDemo {

   class  ConsoleLogger extends  TestLogger{
     /**
      * 定义log抽象方法，用于日志的输出操作
      * 差值表达式实现操作。
      * 复杂的输出可以使用差值表达式进行操作
      **/
     override def log(msg:String ): Unit = println(s"${Type}:${simple.format(new Date())}:${msg}")

     /**
      * 定义type类型，确定日志的输出操作
      * 抽象的变量，没有任何的初始化的数值
      **/
     override var Type: String = "控制台消息"
   }

  def main(args: Array[String]): Unit = {
    val logger = new ConsoleLogger
    logger.log("你是一个好人")
  }

}
