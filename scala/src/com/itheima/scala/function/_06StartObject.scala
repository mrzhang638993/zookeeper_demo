package com.itheima.scala.function

import java.io.File

object _06StartObject{


  def main(args: Array[String]): Unit = {
    // 备注：将隐式转换定义在file.read的作用域中的话，会自动导入的。作用域相同的话，会自动导入的。
    var  file=new File("F:\\works\\hadoop1\\zookeeper-demo\\scala\\src\\com\\itheima\\scala\\wordCount\\data\\1.txt");
    implicit def fileToRichFile(file:File):RichFile={
      val file1 = new RichFile(file)
      file1
    }
    println(file.read())
  }
}

