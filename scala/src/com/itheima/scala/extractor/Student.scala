package com.itheima.scala.extractor

class Student(val name:String,val age:Int)

//定义伴生对象
object  Student{
  def apply(name: String, age: Int): Student = new Student(name, age)
  //  使用unapply的提取器进行操作，将对象解构成为属性的
  def unapply(arg: Student): Option[(String, Int)] = {
    if(arg==null) None
    else  Some(arg.name,arg.age)
  }
}

