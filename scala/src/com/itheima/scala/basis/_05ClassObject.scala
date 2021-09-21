package com.itheima.scala.basis

object _05ClassObject {

  def main(args: Array[String]): Unit = {
    val person = new Person
    println("初始化操作之前")
    println(person.name + "-----" + person.age)
    person.name = "zhangsan"
    person.age = 20
    println("初始化操作之后")
    println(person.name + "-----" + person.age)
  }

  /**
   * 使用下划线完成对象的初始化的操作
   * */
  class Person {
    /**
     * 使用下划线进行初始化操作，String默认的为null的空引用
     * Int对应的默认值是0
     * 使用下划线仅能对于var类型的变量进行初始化操作
     * */
    var name: String = _;
    var age: Int = _;
  }
}
