package com.itheima

object CsustomerObject {

  def main(args: Array[String]): Unit = {
    val customer = new Customer
    customer.name="章承龙"
    customer.sex="男"
    customer.printHello("good morning")
  }
}
