package com.itheima.scala.matchStu

/**
 * option的相关的机制
 * option有两个形式的，包括some以及none的逻辑的。
 * */
object _08OptionalObject {

  /**
   *  定义相除的数据类型.使用option进行类型的匹配操作
   * */
   def  divide( x:Double,y:Double)={
        // 表示没有数据
      if(y==0) None
        //  表示存在数据的
      else Some(x/y)
   }

  def main(args: Array[String]): Unit = {
    val some = divide(10.0, 2.0)
    var  none =divide(10.0,0)
    //  match的类型匹配操作
    some match {
      case None=>println("除0异常")
      case Some(x)=>println("最终的输出结果是:"+x)
    }
    //  getOrElse对应的可以对于null进行操作处理的。空值的时候给予0进行默认的处理的。
    var  value=none.getOrElse(0)
    println(value)
  }
}
