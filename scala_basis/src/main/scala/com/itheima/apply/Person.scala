package com.itheima.apply

//备注：class的属性前面不要增加private属性的。使用了private属性的话,后续的调用是无法使用的
class Person ( var name:String,  var age:Int)


// 创建伴生对象实现相关的代码的操作实现。
// 伴生类对象是可以访问伴生类的私有属性的,但是无法访问private this属性修饰的变量信息
object Person{
   //使用apply方法来创建伴生类对象
   def apply(name:String,age:Int): Person ={
      new Person(name,age)
   }
}

object  PersonTest{
  def main(args: Array[String]): Unit = {
    //使用伴生类的apply方法来创建对象信息的
    val zhangsan: Person = Person("zhangsan", 20)
    //使用apply方法完成对象的创建操作和实现。
    println(zhangsan.name)
    println(zhangsan.age)
  }
}