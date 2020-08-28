package com.itheima.scala.function

import java.io.File

object ImplicitObject{

  /**
   * 定义隐式转换的方法将file转换成为RichFile对象
   * 隐式转换操作只能定义在单例对象中的
   * */
  implicit def fileToRichFile(file:File):RichFile={
    val file1 = new RichFile(file)
    file1
  }
}
