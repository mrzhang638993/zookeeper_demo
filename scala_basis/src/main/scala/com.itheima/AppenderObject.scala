package com.itheima

/**
 * 测试辅助构造器完成相关的辅助构造器代码的编写操作
 * */
object AppenderObject
{
  def main(args: Array[String]): Unit = {
    val arrays: Array[String] = Array("zhangsan", "清华大学")
    //  使用辅助构造器完成代码的编写操作
    val constructure = new AppendConstructure(arrays)
    // 使用辅助构造器来实现相关的对象的构建操作和实现逻辑
    println(constructure.name)
    println(constructure.address)
  }
}
