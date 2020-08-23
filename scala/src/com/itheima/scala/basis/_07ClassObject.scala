package com.itheima.scala.basis

object _07ClassObject {

  /**
   * scala的访问修饰符，scala中没有public的，
   * 只有private，protected的
   * 没有标明是private以及protected类型的话，对应的都是public类型的。
   *  定义成员变量和成员方法
   **/
  class Person {
    // 自动进行类型推断
    private  var  name=""
    private  var  age=0

    //定义成员变量和成员方法
    def getName():String=this.name

    def getAge():Int=this.age

    def  setName(name:String)={
      this.name=name
    }

    def setAge(age:Int)={
      this.age=age
    }

    //  获取到person对应的姓名和元祖进行操作实现.private是外部无法进行访问操作的
    private  def getNameAndAge()=(this.name,this.age)
  }

  def main(args: Array[String]): Unit = {
    val person = new Person
    person.setName("zhangsan")
    person.setAge(20)
    println(person.getName())
    println(person.getAge())
  }
}
