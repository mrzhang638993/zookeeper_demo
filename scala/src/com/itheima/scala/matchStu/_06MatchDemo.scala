package com.itheima.scala.matchStu

object _06MatchDemo {

  /**
   * 匹配列表操作
   * */
  def main(args: Array[String]): Unit = {
    val ints = List(0)
    val ints1 = List(0, 1, 2, 3, 4, 5)
    val ints2 = List(1, 2)
    ints2 match {
      case List(0)=>println("匹配单个元素0")
      case List(1,_*)=>println("匹配元素1开头的元素")
      case List(0,1,_*)=>println("匹配0,1开头的，后面是任意个数的list")
    }
  }
}
