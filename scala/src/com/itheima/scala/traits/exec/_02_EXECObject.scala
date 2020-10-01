package com.itheima.scala.traits.exec

object _02_EXECObject {


  class CustomerService{
      def save(client:String)=println(s"${CustomerService.SERVICE_NAME}:${client}")
  }

  object CustomerService{
     private val  SERVICE_NAME="customerServiceName"

  }
  def main(args: Array[String]): Unit = {
    val service = new CustomerService
     service.save("中国银行")
  }
}
