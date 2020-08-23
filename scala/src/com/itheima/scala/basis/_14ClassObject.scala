package com.itheima.scala.basis

object _14ClassObject {
  //  创建class
  class CustomerService{
    //  调用伴生对象CustomerService
    def save()=println(CustomerService.SERVICE_NAME)
  }

  //  创建伴生对象
  object  CustomerService{
     private val SERVICE_NAME="CustomerService";
  }

  //  伴生类和伴生对象是可以相互访问私有属性的。

  def main(args: Array[String]): Unit = {
    // 实例可以调用实例方法和静态属性的
     var service=new CustomerService
    service.save()
  }
}
