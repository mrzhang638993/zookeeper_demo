package com.itheima.scala.function

import java.io.File

object _05StartObject{
  /**
   * 执行隐式转换操作
   * */
  def main(args: Array[String]): Unit = {
    var  file=new File("F:\\works\\hadoop1\\zookeeper-demo\\scala\\src\\com\\itheima\\scala\\wordCount\\data\\1.txt");
    //  进行隐式转换，这样的话，原有类型的对象的功能可以得到增强的。类似于java中的装饰器的模式的，可以实现功能增强的。
    //val str: String = ImplicitObject.fileToRichFile(file).read(file)
    //  手动导入隐式转换操作
    import  ImplicitObject.fileToRichFile
    val str: String = file.read()
    println(str)
  }
}
