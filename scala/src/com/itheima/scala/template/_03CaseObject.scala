package com.itheima.scala.template

object _03CaseObject {


  def main(args: Array[String]): Unit = {
    val lisi = Person("lisi", 21)
    val lisi1=Person("lisi",21)
    println(lisi.eq(lisi1))  //  false 比较的是引用值是否相等，不同的对象是不相等的
    println(lisi==lisi1)  //  true 比较的是值是否相等的。样例类的相等比较的是成员变量值是否相等的。
  }
}
