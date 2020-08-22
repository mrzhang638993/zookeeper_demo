package com.itheima.scala.traits

object _07TrailDemo {

   class UserService

  def main(args: Array[String]): Unit = {
     /**
      * 直接将特质中的方法混入到了对象中.警作用于当前的对象
      * 使用with只是对单个的对象起作用的，
      * 对于其他的对象不会产生作用的
      * */
     val service = new UserService  with Logger1
     val service2= new UserService
     service.log("hello world")
  }
}
