package com.itheima.scala.extractor

object  _16GernericObject {

  def main(args: Array[String]): Unit = {
    val value = new Pair[String]("zhangsan", "20")
    println(value.a+"-----"+value.b)
    val value1 = new Pair[Int](1, 2)
    println(value1.a+"-----"+value.b)
    val value2 = new Pair[Double](1.0, 2.0)
    println(value2.a+"-----"+value2.b)
    val value3 = new Pair1[String, Int]("zhansgan", 20)
    println(value3.a+"-----"+value3.b)
  }
}
