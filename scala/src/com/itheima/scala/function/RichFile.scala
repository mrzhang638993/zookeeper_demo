package com.itheima.scala.function

import java.io.File

import scala.io.Source

class RichFile(var file:File){


  /**
   * 定义隐式转换的操作方法的，对应的实现相关的代码的转换操作的
   *
   * */
  def read( )={
    //  将文件内容读取成为字符串对象
    val string: String = Source.fromFile(file.getAbsolutePath).mkString
    string
  }
}
