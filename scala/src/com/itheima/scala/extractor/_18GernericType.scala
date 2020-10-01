package com.itheima.scala.extractor

object _18GernericType {

  /**
   * 定义下界的操作，T必须是policeman的本身或者是它的父类的
   * 类型必须是PoliceMan以及它的父类的
   * */
  def  demo[T >:PoliceMan](array: Array[T])=println(array)

  def main(args: Array[String]): Unit = {
    demo(Array(new PoliceMan))
    demo(Array(new Person1))
    // demo(Array(new SuperMan))
  }
}
