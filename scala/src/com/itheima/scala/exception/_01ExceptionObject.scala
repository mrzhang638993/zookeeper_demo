package com.itheima.scala.exception

object _01ExceptionObject {
  /**
   * 除零异常操作
   * */
  def main(args: Array[String]): Unit = {
      try{
        var num=10/0
      }catch {
        case ex:Exception=>println(ex.printStackTrace())
      }finally {
        //  对应的执行相关的异常操作
        //  抛出异常 throw  new Exception
      }
      println("程序继续执行")
  }
}
