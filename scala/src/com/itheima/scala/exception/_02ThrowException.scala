package com.itheima.scala.exception

object _02ThrowException {


  def main(args: Array[String]): Unit = {
    //  方法声明的时候不需要抛出异常的，scala解决了相关的异常检查的机制的。
    //  不需要在方法上面显示声明抛出的异常的。
     throw  new Exception("测试抛出异常")
  }
}
