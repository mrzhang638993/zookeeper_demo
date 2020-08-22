package com.itheima.scala.traits

object _06TrailDemo {

  class  ConsoleLogger  extends  Output{
    /**
     * 抽象日志方法
     **/
    override def log(msg: String): Unit = println(msg)
  }

  def main(args: Array[String]): Unit = {
    val logger = new ConsoleLogger
    logger.info("null pointer exception ")
    logger.warn("null pointer exception ")
    logger.error("null pointer exception ")
  }
}
