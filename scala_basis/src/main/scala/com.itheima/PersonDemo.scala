package com.itheima

// 增加private[this]的访问权限的，对应的伴生类对象也是无法访问的。
// 伴生对象的话,是可以访问到伴生类的class里面的private对象的，但是无法访问private this修饰的内容的。
// 伴生对象其本质上就是一个单例对象的，其存在一些特殊的要求来保证相等性的内容的.
class PersonDemo( var name:String)

object PersonDemo{
   def access(p:PersonDemo): Unit ={



      println(p.name)
   }
}
