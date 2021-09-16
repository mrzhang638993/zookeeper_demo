package com.itheima

/**
 * 定义主构造器的方法和名称信息
 * */
object MainConstructorTest {
   /**
    * 使用主构造器构建实例对象信息
    * */
  def main(args: Array[String]): Unit = {
    val constructure = new MainConstructure("zhangsan", 20)
    //对应的是成员变量的数据信息的
    println(constructure.name)
    //对应的是成员变量的数据信息的
    println(constructure.age)
    println("-----")
    //创建一个空的对象,创建新的数据信息,什么都不传递的话，构造的是一个空的数据对象的
    //对应的是一个空的数据对象信息的。
    val constructure1 = new MainConstructure
    println(constructure1.name)
    println(constructure1.age)
    // 不传递姓名，对应的只是传递一个对应的年龄参数,获取得到相关的年龄参数信息
    //  根据参数名称完成赋值和调用的，这个逻辑是很关键和有用的信息的。
    val str = new MainConstructure(age = 30)
    //  指定方法的名称和参数实现相关的代码操作实现的。对应的获取得到相关的数值信息的
    println(str.name)
    println(str.age)
  }
}
