package com.itheima

import com.itheima.CustomerService.name

// 伴生类以及伴生对象是可以直接访问private修饰的私有属性和方法的。
// 伴生类
class CustomerService {
    // 访问伴生对象的属性信息
    def save(): Unit ={
       println(name)
    }
}

//伴生对象
object CustomerService{
   private val name:String="good"
   def test(): Unit ={
     //  访问伴生类的私有方法
     val service = new CustomerService
     service.save()
   }
}
