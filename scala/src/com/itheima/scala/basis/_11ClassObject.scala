package com.itheima.scala.basis

object _11ClassObject {

  def main(args: Array[String]): Unit = {
    // 访问单例对象的属性的
    println(Dog.LEG_NUM)
    println(Dog.printSpliter())
    Dog.testHello("zhangsan", "身高")
  }

  // 创建单例对象,使用object的关键字的
  object Dog {
    // 相当于静态变量
    val LEG_NUM = 4

    //  创建单例对象的成员方法
    def printSpliter(): Unit = {
      println("__" * 10)
    }

    //  创建单例对象的方法，比较类似于java中的static的
    def testHello(name: String, body: String) = println(name + "-----" + body)
  }
}
