package com.itheima.scala.genericType

object _01GernericObject {

  //def  getMiddle(array:Array[Int]):Int=array(array.length/2)

  //  定义泛型类型的方法,方法后面增加泛型操作即可的。
  def  getMiddle[T](array:Array[T]):T=array(array.length/2)

  def main(args: Array[String]): Unit = {
    val i = getMiddle(Array(1, 2, 3, 4, 5, 6, 7))
    println(i)
    val i1 = getMiddle(Array(10, 11, 12, 13, 14, 15))
    println(i1)
    // 实现更多的类型的比较
    val value = getMiddle(Array("001", "002", "003", "004", "005"))
    println(value)
    //
    val value1 = getMiddle(Array(1.2, 2.5, 3.6, 4.8, 5.9))
    println(value1)
  }
}
