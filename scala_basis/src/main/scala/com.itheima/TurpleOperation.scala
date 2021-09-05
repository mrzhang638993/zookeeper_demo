package com.itheima

/**
 * 元祖操作实现。包含不同类型的值，元祖中的元素是不可变的。
 * 元祖中的每一个元素都是不可变的元素的，不要试图赋值操作
 * */
object TurpleOperation {

  def main(args: Array[String]): Unit = {
      // 定义元祖的操作,可以定义多个元素
      val tuple: (String, Int, String, Int, String) = ("hello", 1, "many", 2, "world")
      // 定义两个元素的元祖
      val tuple1: (Int, String) = 1 -> "hello"
      //  元祖的访问方式:
      println(tuple1._1+"======"+tuple1._2)
      //  获取元祖的第二个方式和实现相关的操作实现的
      println(tuple._1+"======"+tuple._2+"======"+tuple._3)
  }
}
