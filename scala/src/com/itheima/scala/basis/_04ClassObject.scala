package com.itheima.scala.basis

object _04ClassObject {

  /**
   * 定义类，定义成员变量
   * */
  class  Person{
    /**
     * 使用val以及var定义成员变量
     * val：对应的表示成员变量是不可变的
     * var：对应的是表示成员变量是可变的
     * */
    var name:String=""
    var  age:Int=0
  }

  def main(args: Array[String]): Unit = {
    val person = new Person
    person.name="zhangsan"
    person.age=23
    println(person.name+"-----"+person.age)
  }
}
