package com.itheima.scala.function

object _04ClassObject {


  //闭包其实就是一个函数，只不过这个函数的返回值依赖于声明在函数外部的变量。
  //可以简单认为，就是可以访问不在当前作用域范围的一个函数。
  def main(args: Array[String]): Unit = {
     val y=10
    /**
     * 函数引用外部的变量共同的构成一个闭合的函数称之为闭包操作。一般的情况下函数自身是封闭的。
     * 闭包是函数和外部的变脸共同的构成一个闭环的。
     * 柯理化其实也是一个闭包的。b:Int=>a+b函数对应的依赖a的变量的。
     * def test(a:Int)={
     * b:Int=>a+b
     * }
     */

     def add(x:Int)=x+y
  }
}
