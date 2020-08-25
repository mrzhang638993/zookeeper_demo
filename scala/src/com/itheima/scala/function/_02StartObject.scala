package com.itheima.scala.function

object _02StartObject {

  /**
   * 使用匿名函数简化代码的书写
   * */
  def main(args: Array[String]): Unit = {
    // (x) => "*" * x)称之为匿名函数，没有名称的函数
    println((1 to 10).map(x => "*" * x))
    //  下面使用_来简化代码的辨析
    println((1 to 10).map("*" * _))
  }
}
