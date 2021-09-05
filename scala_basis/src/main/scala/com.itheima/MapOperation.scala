package com.itheima

/**
 * map映射操作逻辑
 * */
object MapOperation {

  def main(args: Array[String]): Unit = {
    //获取对应姓名下面的年龄信息
    val nameAge = Map("zhangsan" -> 30, "lisi" -> 40)
    println(nameAge.get("zhangsan").getOrElse(0))
    println(nameAge.get("lisi").getOrElse(0))
  }
}
