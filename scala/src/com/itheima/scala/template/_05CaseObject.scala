package com.itheima.scala.template


/**
 * 测试样例类的copy方法
 * */
object _05CaseObject {


  def main(args: Array[String]): Unit = {
    val lisi = Person("lisi", 25)
    val wangwu  = lisi.copy(name = "王五")
    println(lisi) // Person(lisi,25)
    println(wangwu) //Person(王五,25) 可以实现快速的拷贝实现
  }
}
