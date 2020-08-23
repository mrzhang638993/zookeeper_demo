package com.itheima.scala.basis

/**
 * 使用main方法的话，必须使用单例模式
 * */
object _02ClassObject {

  /**
   * 省略后面的大括号。没有任何的成员的话，可以省略大括号的
   * */
  class Person;



  def main(args: Array[String]): Unit = {
    /**
     * 简写方式创建对象，参数为空的时候，后面的括号是可以省略的
     * */
    val person  = new Person
    println(person)
  }
}
