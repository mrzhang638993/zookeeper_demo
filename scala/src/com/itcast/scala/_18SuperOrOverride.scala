package com.itcast.scala


/**
 * 查看super以及overide的类的继承操作
 *
 * override仅仅作用于val的不可变的overide对象的。
 *
 * */
object _18SuperOrOverride {

  class  Person(val name:String="zhangsan",val age:Int=20){
    // 定义成员方法
     def  getName()=name
  }

  /**
   * 类的继承，然后执行重写操作实现
   * */
  class  Student extends  Person{
    override  val name:String ="Student"
    override def getName()=name
  }

  def main(args: Array[String]): Unit = {
    val person = new Person
    println(person.getName())
    val student = new Student
    println(student.name)
  }
}
