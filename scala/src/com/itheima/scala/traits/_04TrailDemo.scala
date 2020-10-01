package com.itheima.scala.traits

object _04TrailDemo {


  class  UserService  extends  Logger{
     def add()=log("添加用户")
  }


  def main(args: Array[String]): Unit = {
     val  userService=new UserService
      userService.add()
  }

}
