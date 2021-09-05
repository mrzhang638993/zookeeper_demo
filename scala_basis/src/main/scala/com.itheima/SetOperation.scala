package com.itheima

import scala.collection.mutable

/**
 * set 定义了不可变的集合元素
 * 元素是不保证数据的，数据不会重复的
 * */
object SetOperation {

  def main(args: Array[String]): Unit = {
    /*val ints = Set[Int](1, 1)// 实际底层的元素还是只有一个元素的。
    ints.foreach(println(_))*/
    // set的相关的操作
   /* val ints = Set(1, 1, 2, 3, 4, 5)
    println(ints.size)
    println("$$$$$$$$$$$$$$$$$")
    for(ele <- ints)println(ele)
    val ints1 = Set(6, 7, 8)
    val dest=ints++ints1
    println("$$$$$$$$$$$$$$$$$")
    dest.foreach(println(_))
    val ints2 = List(6, 7, 8, 9)
    val setList=ints++ints2
    println("$$$$$$$$$$$$$$$$$")
    setList.foreach(println(_))*/
    // 集合操作之间的++操作适用于所有的集合之间元素之间的操作实现的。
    //可变集合的操作管理,对于可变集合的操作处理实现
    val ints: mutable.Set[Int] = scala.collection.mutable.Set(1, 2, 3, 4)
    ints+=5 //增加元素实现相关的元素的操作实现
    println("$$$$$$$$$$$$$$$$")
    ints.foreach(println(_))
    ints -=1 // 移除元素
    println("$$$$$$$$$$$$$$$$")
    ints.foreach(println(_))
  }
}
