package com.itheima

object PersonAccessDemo {

  def main(args: Array[String]): Unit = {
    val access = new PersonAccess
    access.setAge(20)
    access.setName("zhangsan")
    println(access.getAge())
    println(access.getName())
    // 无法访问定义的私有的方法  getNameAndAge 访问受到限制了
  }
}
