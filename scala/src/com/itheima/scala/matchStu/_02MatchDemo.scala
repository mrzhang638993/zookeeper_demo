package com.itheima.scala.matchStu

object _02MatchDemo {

  def main(args: Array[String]): Unit = {
      //  定义Any类型的变量
      //  分别赋值"hadoop",1,1.0
      //  使用模式匹配来匹配数据类型
      val a:Any ="haddoop"
     a match {
         //  后面不需要使用到匹配的x变量的话，可以使用_进行替代的
       case _=>println("是一个字符串的类型")
       //case x:String=>println(s"${x} 是一个字符串类型")
       case x:Int=>println(s"${x}是一个int类型的变量")
       case x:Double=>println(s"${x}是一个double类型的变量")
       case _=>println("未匹配到任何的内容")
     }
  }
}
