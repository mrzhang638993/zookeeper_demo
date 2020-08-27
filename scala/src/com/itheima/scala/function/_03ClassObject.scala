package com.itheima.scala.function

object _03ClassObject {

  /**
   *  编写一个方法，用来完成两个Int类型数字的计算
   *  具体如何计算封装到函数中
   *  使用柯里化来实现上述操作
   * */
  def transForm(a:Int,b:Int)=a+b

  // 函数颗粒化对应的是函数的逐层分解操作
  def test(a:Int)={
     b:Int=>a+b
  }

  def main(args: Array[String]): Unit = {
    val i = test(1)(2)
    println(i)
  }
}
