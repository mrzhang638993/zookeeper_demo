package com.itheima.scala.function

object _07StartObject {

 /**
  * 定义隐式参数implicit delimiters:(String,String)
  * */
  def quote(what:String)(implicit delimiters:(String,String))={
    delimiters._1+what+delimiters._2
  }

  /**
   * 定义隐式参数,implicit修饰变量
   * */
  object implicitDemo{
     implicit  val delimiters=("<<",">>")
  }
  /**
   * 定义隐式参数
   * */
  def main(args: Array[String]): Unit = {
    //  导入对象的所有的参数
    import implicitDemo._
    println(quote("nihao"))
  }
}
