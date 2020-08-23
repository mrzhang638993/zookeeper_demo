package com.itheima.scala.basis

/**
 * 匿名内部类
 * */
object _22AnyClass {
  /**
   * 定义抽象类
   * */
  abstract  class Person{
     def  sayHello()
  }

  def main(args: Array[String]): Unit = {
       //  匿名内部类的方式来创建代码实例。匿名内部类，对应的是没有名称的子类的。
       //  下面是匿名内部类
       var p=new Person {
         override def sayHello()=println("hello world")
       };
       p.sayHello()
  }
}
