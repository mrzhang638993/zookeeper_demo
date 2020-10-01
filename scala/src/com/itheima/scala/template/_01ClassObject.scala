package com.itheima.scala.template

object _01ClassObject {


  def main(args: Array[String]): Unit = {
    // 使用伴生对象创建对象
    val zhangsan = Person("zhangsan", 20)
    println(zhangsan.name+"------"+zhangsan.age)
    //  样例类的对象可以不使用new创建的。
    var  person1=Person1("zhangsan",30)
    println(person1.name+"----"+person1.age)
    person1.age=30
  }
}
