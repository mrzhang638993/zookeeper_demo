package com.itheima.scala.basis

object _16ClassObject {

  def main(args: Array[String]): Unit = {
    //val zhangsan = Person.apply("zhangsan", 30)
    //println(zhangsan.name+"-----"+zhangsan.age)
    //  使用apply方法实现快速创建对象
    val zhangsan = Person("zhangsan", 20)
    println(zhangsan.name + "-----" + zhangsan.age)
  }

  /**
   * 创建伴生类
   * */
  class Person(var name: String, var age: Int)

  /**
   * 创建伴生对象
   * */
  object Person {
    def apply(name: String, age: Int): Person = new Person(name, age)
  }
}
