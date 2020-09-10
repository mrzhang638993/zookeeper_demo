package cn.itcast.spark.priciple

import org.junit.Test

class Closure {

  /**
   * 编写高阶函数，在这个函数内部存在一个变量，返回一个函数，通过这个变量完成一个计算
   *
   * */
  def  testClosure(): Int=>Double ={
      val  factor=3.14
      var areaFunction=(r:Int)=>Math.pow(r,2)*factor
     // areaFunction 对应的是闭包函数的，一般的java中是返回引用和数值的，这个地方对应的传递的是函数的。
      areaFunction
  }
  /**
   * 测试方法
   * 闭包的本质：闭包的本质是一个函数，在scala中函数是一个特殊的类型。闭包是一个特殊的函数类型的
   *
   * 一个函数携带了外部作用域的对象的话，对应的体现出来的是闭包的概念的。
   * areaFunction闭包访问了外部作用域的对象的。
   * */
  @Test
  def test1(): Unit ={
    val intToDouble: Int => Double = testClosure()
    println(intToDouble(4))
  }
}
