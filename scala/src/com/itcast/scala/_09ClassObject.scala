package com.itcast.scala

object _09ClassObject {

  /**
   * 定义主构造器
   * */
  class Person(var name:String="",var age:Int=0){
      println("调用主构造器进行创建")
  }



  def main(args: Array[String]): Unit = {
    val person = new Person("zhangsan",20);
    println(person.name+"-----"+person.age)

    //  创建空的对象
    val person1 = new Person()
    println(person1.name+"-----"+person1.age)

    //  创建man40对象
    val person2 = new Person(age = 40)
    println(person2.name+"-----"+person2.age)
  }
}
