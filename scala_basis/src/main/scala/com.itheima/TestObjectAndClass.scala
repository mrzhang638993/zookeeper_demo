package com.itheima

/**
 * 测试伴生类以及伴生对象
 * */
object TestObjectAndClass {

  def main(args: Array[String]): Unit = {
    // 使用伴生类实现相关的代码操作
   /* val service = new CustomerService
    service.save()*/
    //  使用伴生对象实现相关的操作,提示不是对象的一个方法的。
    CustomerService.test()

  }
}
