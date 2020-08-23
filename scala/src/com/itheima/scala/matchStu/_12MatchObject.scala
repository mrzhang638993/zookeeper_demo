package com.itheima.scala.matchStu

object _12MatchObject {


  def main(args: Array[String]): Unit = {
    var content=List("38123845@qq.com","a1da88123f@gmail.com","zhansan@163.com","123afadff.com")
    //  使用括号进行分组匹配操作
    var regex=""".+@(.+)\..+""".r  // 使用括号进行正则表达式进行分组操作
    val list = content.map {
      case eml@regex(company) => s"${eml}->${company}"
      case x  => s"${x}->未知"
    }.toList
    println(list)
  }
}
