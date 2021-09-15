package com.itheima

/**
 * 定义和访问成员变量的数据
 * var:表示变量是可以修改的。
 * val:对应的表示的是变量是不能修改的。
 * */
object DefineAndAccessProperties {
  def main(args: Array[String]): Unit = {
    var person: Person = new Person
    // 直接访问成员变量,不需要get以及set方法的。
    person.name="张三"
    person.age=20
    println(person.name)
    println(person.age)
  }
}
