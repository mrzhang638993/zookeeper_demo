package com.itheima.scala.extractor

object _15UnapplyStudent {
  /**
   * 创建对象和初始值的操作的。
   * 样例类自身实现了apply和unapply的相关的方法的。
   * 自定义的对象需要自己实现unapply方法的。
   * */
  def main(args: Array[String]): Unit = {
    val zhangsan = new Student("zhangsan", 20)
    val unit = zhangsan match {
      case Student(name, age) => println(s"${name}:${age}")
    }
    println(unit)
  }
}
