package com.itheima.option
//完成相关的数据实现操作和逻辑操作
//使用Some或者是None等完成数据的封装操作实现
object OptionDemo {
  def main(args: Array[String]): Unit = {
     //定义相除的方法
    /*div(10.0,2.0) match {
      case None=>println("zero")
      case Some(num)=>println(s"result is ${num}")
      case _=>println("未知结果")
    }
    div(10.0,0.0) match {
      case None=>println(" div 0 exception")
      case Some(res)=>println(s"${res}")
      case _=>println("exception")
    }*/
    //数据打印操作和实现机制.getOrElse实现代码编程操作实现
    val res: Option[Double] = div(10.0, 2.0)
    println(res.getOrElse(0))
    val zero: Option[Double] = div(10.0, 0)
    println(zero.getOrElse(0))
  }
  //option的主要作用是使用来进行数据封装操作的。Some代表的是存在数据的操作的，NONE代表的是不存在数据的
  //option的getOrElse操作实现编程处理
  def div( x:Double,y:Double): Option[Double] ={
    if (y==0){
      None
    }else{
      val res:Double=x/y
      //some表示存在数据，需要进行数据封装操作
      Some(res)
    }
  }
}
