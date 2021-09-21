package com.itheima.scala.basis

/**
 * getClass以及classOf执行的操作
 * getClass 可以获取到某一个类型的，
 * classOf可以精确的判断是否是相关的类型的。
 *
 * classof以及getClass可以执行精确的类型判断操作
 * isInstanceof以及asInstanceOf对应的是只能判断是类型以及类型的子类
 * student isInstanceOf Person
 * __________
 * 是student类型
 * */
object _20GetClass {

  def main(args: Array[String]): Unit = {
    var student: Person = new Student
    if (student.isInstanceOf[Person]) {
      println("student isInstanceOf Person")
    } else {
      println("student isNotInstanceOf Person")
    }
    println("_" * 10)
    // getClass 获取对象的类型，classOf 获取类的类型
    if (student.getClass == classOf[Person]) {
      println("是person类型")
    }
    if (student.getClass == classOf[Student]) {
      println("是student类型")
    }
  }

  class Person

  class Student extends Person
}
