package com.itheima.scala.basis

object _06ClassObject {

  /**
   * 定义成员方法
   **/
  class Customer {
    /**
     * var类型的成员变量进行初始化操作
     * */
    var name:String=_
     var sex:String=_
    /**
     * 返回值可以不书写的，scala中可以执行类型推断的
     * */
     def printHello(msg:String): Unit ={
        println(msg)
     }
  }

  def main(args: Array[String]): Unit = {
      var customer=new Customer
      customer.name="zhangsan"
      customer.sex="男"
      /**
       * 调用成员方法
       * */
      customer.printHello("helloWorld")
  }
}
