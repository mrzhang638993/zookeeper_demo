package com.itheima.scala.basis

object _10ClassObject {

  def main(args: Array[String]): Unit = {
    var customer = new Customer(Array("zhangsan", "beijing"));
    println(customer.name + "-----" + customer.address)
  }

  /**
   * 步骤一：首先定于主构造器
   * */
  class Customer(var name: String = "", var address: String = "") {
    println("调用主构造器")

    /**
     * 步骤二：定义辅构造器
     * */
    def this(data: Array[String]) {
      //  第一步调用主构造器
      this(data(0), data(1))
    }
  }
}
