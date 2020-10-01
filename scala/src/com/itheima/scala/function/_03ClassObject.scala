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

  //  函数柯理化的进一步操作.其中calc对应的是一个函数的名称的，calc(x,y)对应的是函数的调用的
  def caculate(x:Int,y:Int)(calc:(Int,Int)=>Int)={
    calc(x,y)
  }

  def main(args: Array[String]): Unit = {
    println(test(1)(2))
    println(caculate(1, 2)(_+ _))  // _ + _ 对应的是函数的占位符，代表的是单个元素。
    println(caculate(1, 2)(_ * _))
    println(caculate(1, 2)(_ / _))
    println(caculate(1, 2)(_ - _))
  }
}
