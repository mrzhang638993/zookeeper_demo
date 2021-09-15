package com.itheima

/**
 * scala中默认是全局可见的，不存在public的关键字的
 * scala只会存在关键字：private protected
 * private 修饰的变量或者是方法的话，外部是不可访问的
 * protected修饰的变量，只有满足继承关系才可以看到的。
 * */
class PersonAccess {

    private var name:String=_
    private var age:Int=_

  def  getName(): String ={
     this.name
  }

  def getAge(): Int ={
     this.age
  }

  def  setName(name:String)={
      this.name=name
  }

  def setAge(age:Int):Unit={
      this.age=age
  }

  /**
   * 定义私有方法名称和信息
   * 获取姓名和年龄，对应的返回的是元祖信息
   * */
  private def getNameAndAge(): (String,Int) ={
    (name,age)
  }
}
