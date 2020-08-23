package com.itheima.scala.extractor

object _17GernericType {

  /**
   * 定义上界操作
   * 限定泛型类型是person本身或者是person的子类的
   * */
  def  demo[T <:Person](array: Array[T])=println(array)
  def main(args: Array[String]): Unit = {
     demo(Array(new Person))
     demo(Array(new Student1))
  }
}
