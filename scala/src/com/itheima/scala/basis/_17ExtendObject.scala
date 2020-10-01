package com.itheima.scala.basis

/**
 * scala的继承操作体系
 *  下面演示的是类的继承
 * 类和单例对象都是可以从父类中继承的
 * */
object _17ExtendObject {

  /**
   * 定义父类对象
   * */
  class  Person{
     var name:String="\001"
     def  getName()=name
  }

  /**
   * 定义子类对象
   * */
  class Student extends  Person{

  }

  /**
   * 实现单例对象的继承
   * */
    object Student1 extends  Person

  def main(args: Array[String]): Unit = {
    val student = new Student
    println(student.name)
    student.name="zhangsan"
    println(student.name)
    Student1.name="zhangsan1"
    println(Student1.name)
  }
}
