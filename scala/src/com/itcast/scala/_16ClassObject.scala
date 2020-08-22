package com.itcast.scala

object _16ClassObject {

  /**
   * 创建伴生类
   * */
  class  Person(var  name:String,var age:Int)

  /**
   * 创建伴生类
   * */
  object Person{
    def apply(name: String, age: Int): Person = new Person(name, age)
  }

  def main(args: Array[String]): Unit = {
    val zhangsan = Person.apply("zhangsan", 30)
    println(zhangsan.name+"-----"+zhangsan.age)
  }
}
