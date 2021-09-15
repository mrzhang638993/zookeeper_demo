package com.itheima

/**
 * 使用下划线来初始化成员变量
 * 使用更加简洁的初始化代码的方法的
 * _下划线初始化的话，只能初始化的是var变量修饰的内容的
 * val修饰的变量，必须要手动的完成相关的初始化的操作的。
 * */
class Person1 {
  /**
   * 使用下划线完成变量的初始化操作实现
   * 对于String 而言,使用_初始化的话，默认值是null，int类型的默认值是0的
   * 值的默认化和java中是类似的。
   * */
  var name:String=_
  var age:Int=_
}
