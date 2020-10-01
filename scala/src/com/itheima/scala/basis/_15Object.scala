package com.itheima.scala.basis

object _15Object {

  /**
   * 主构造器。 private[this] 属性
   * */
  class Person(/*private[this] */var name:String)

  /**
   * 定义伴生对象
   * */
  object  Person{
    //  无法访问对象的name字段属性的p.name
    def printPerson(p:Person)=println(p.name)
    //  伴生对象中是可以访问伴生类的private成员的，但是无法访问private  this类型的
  }


  def main(args: Array[String]): Unit = {
     Person.printPerson(new Person("zhangsan"))
  }
}
