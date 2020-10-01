package com.itheima.scala.matchStu

object  _04MatchDemo {

/**
 * scala中使用模式匹配来进行模式匹配，匹配样例类的数据
 * */
  def main(args: Array[String]): Unit = {
      var p:Any=Customer("zhangsan",20)
      var p1:Any=Order("1")
     //  样例类进行匹配，可以快速的获取到样例类的成员变量的数值
    p1 match {
        case Customer(name,age)=>println(s"${name}:${age}")
        case Order(id)=>println(s"${id}")
        case _=>println("没有匹配到")
      }
  }
}
