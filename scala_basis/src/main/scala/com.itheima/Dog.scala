package com.itheima

/**
 * 单例对象
 * 保存了一个单例对象，保存了一条狗需要多少腿的
 * */
object Dog {
   //定义单例对象的一个成员的。类似于java中的static变量的
   //相当于static的静态的成员变量信息的。对应的可以完成相关的实例代码的编写操作和实现的
   var legs:Int=4

   //  定义成员方法，相当于定义静态方法实现相关的代码操作实现
   def  printLeg(): Unit ={
        println(legs)
   }
}
