package com.itheima

import scala.util.control.Breaks.{break, breakable}

object ScalaBasis {

  def main(args: Array[String]): Unit = {
  /*    // 定义String类型的变量。val代表的是不能重新赋值的变量。var对应的代表的是可以赋值的变量
      val name:String="zhangsan"
      println(name)
      var name1:String="lisi"
      println(name1)
      name1="goodBye"
      println(name1)
    //  使用更加简洁的方式来实现变量定义的，可以实现类型推断操作指导的。可以根据数值来推断变量的。
     // 惰性赋值和加载数据操作.lazy 只是在调用的时候才会完成数据的加载的。和java中的懒汉以及恶汉模式是一样的。
     lazy val  testLazy="good"
     println(testLazy)
    //定义字符串的高级方式：
    //  差值表达式来表达大量字符串的拼接操作,差值表达式可以使用变量或者是表达式的
    //  差值表达式是s开头的，引用变量使用${}来引用变量实现操作的。
    val  nameStr=s"name1=${name1},name=${name},testLazy=${testLazy}"
    println(nameStr)
    //  使用"""来定义字符串实现操作的,可以保存大量的字符串实现操作管理的。
    val sqlStr=
      """
        |select *  from  good
        |
        |""".stripMargin
        println(sqlStr)
    // 测试数据类型和操作符的使用的
    //  scala中所有的数据类型都是大写开头的文件的。scala中是没有++以及--等的运算符号的。
    //  == 以及!=  比较内存地址eq
    val str1="abc"
    val str2=str1+"" // 使用"abc"+""对应的是true,服务对应的是相等的。
    println(str1.equals(str2))
    println(str1==str2)
    //比较引用值相等
    println(str1.eq(str2))
    //  if条件表达式，来代替三元表达式
    val sex="male"
    val result=if (sex=="male") 1 else 0
    // 块表达式{} 对应的返回值是块表达式的最后的一行的
    val square={
      println(1+3)
      1+1
    }
    println(square)*/
    //for表达式实现遍历和迭代操作,其中1是对象的，to是1的方法的。1.to(10)
    /*for(i <- 1 to 10){
      println(i)
    }
    //推荐使用这个的，中缀调用方式的。
    for(i <- 1 to 10) println(i)
    // 嵌套循环
    for(i<- 1 to 3 ; j <- 1 to 5){
       print("* ")
       if(j==5) println() else print()
    }*/
    //  for循环中增加if表达式，称之为守卫操作。只有当if条件是成熟的话，才会进入到for循环中的。
    //for( i <- 1 to 10  if i%3==0 ) println(i)
    // 使用for的推导式生成一堆的集合数据信息。使用for表达式生成集合数据信息。
    /*val v=for(i <- 1 to 10 )  yield  i*10
    for(i<- v) println(i)*/
    var j=0
    while(j<=10)
      {
        println(j)
        j=j+1  //  scala 中是不支持++以及--的操作的。
      }
    // break以及continue语句操作和实现。需要导入相关的数据包，不能直接使用break以及continue操作的。
    // break以及continue语句操作和实现。需要导入相关的数据包，不能直接使用break以及continue操作的。
    // 使用break语句执行break的效果操作实现.操作的效果是跳出整个的循环的
    /*breakable{
       for(i<- 1 to 100) if(i==50) break else println(i)
    }*/
    // 实现continue操作效果和实现.可以实现相关的continue的效果。
    for(i<-1 to 5){
      breakable{
        if(i==2) break() else  println(i)
      }
    }
  }
}
