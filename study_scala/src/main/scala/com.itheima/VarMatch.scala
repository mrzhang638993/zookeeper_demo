package com.itheima

object  VarMatch {
//使用模式匹配获取数组中的元素信息
  def main(args: Array[String]): Unit = {
    val nums: Array[Int] = Array(1, 2, 3, 4, 5)
    val Array(_,x,y,z,_*)=nums
    println(x)
    println(y)
    println(z)
  }
}
