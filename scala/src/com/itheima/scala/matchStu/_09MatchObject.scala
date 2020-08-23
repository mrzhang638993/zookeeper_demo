package com.itheima.scala.matchStu

object _09MatchObject {

  /**
   * 1.定义1-10一共10个数组
   * 2.将1-3的数字转化为[1-3]
   * 3.将4-8的数字转化为[4-8]
   * 4.将其他的数字转化为[8-*]
   * */
  def main(args: Array[String]): Unit = {
    //  将一个列表转化为另外的一个列表
    var array=(1  to 10).toArray
    // 偏函数简化函数的定义.map实现偏函数的转换操作。偏函数简化函数式编程操作
    var result=array.map{
      case x if x>=1 && x<=3 =>"[1-3]"
      case x if x>=4 && x<=8 =>"[4-8]"
      case _=>"[8-*]"
    }.toList
    println(result)
  }
}
