package com.itheima.scala.matchStu

object _06MatchDemo {

  /**
   * 匹配列表操作
   * */
  def main(args: Array[String]): Unit = {
    var int0=0::Nil  //定义一个空的列表
    val ints = List(0)
    val ints1 = List(0, 1, 2, 3, 4, 5)
    val ints2 = List(1, 2)
    int0 match {
      //case List(0)=>println("匹配单个元素0")
      //case List(1,_*)=>println("匹配元素1开头的元素")
      //case List(0,1,_*)=>println("匹配0,1开头的，后面是任意个数的list")
      case 0::Nil=>println("匹配列表只有一个元素0的集合")
      case 0::tail=>println("匹配开头元素为0,后面元素不固定的元素")
      //  匹配两个元素
      case x::y::Nil=>println(s"匹配两个元素的列表,${x}:${y}")
      case _=>println("无法匹配到满足条件的list")
    }
  }
}
