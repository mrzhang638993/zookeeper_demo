package com.itheima

import scala.collection.mutable.ArrayBuffer

/**
 * 函数操作和实现,变长数组实现操作和管理的。
 * */
object FunctionTest{

  def main(args: Array[String]): Unit = {
      // 定义函数对象,函数对象可以脱离对象直接进行调用的。
     /* val add=(x:Int,y:Int)=>x+y
      println(add(1,3))*/
      //方法属于类或者是对象的，运行的时候是会加载方法到jvm的方法区的。
      //可以将函数赋值给一个变量的，可以在运行的时候将变量加载到堆内存中运行的。函数其本质是一个对象的
     // 函数对应的是继承FunctionN对象的，是一个对象的。
     //  将函数转化成为一个变量执行,scala中是可以这么操作实现的。
     /* val addFun: (Int, Int) => Int = add _  // 使用_将方法转化成为一个函数
      val result: Int = addFun(3, 5)
      println(result)*/
     //定常数值以及变长数组
     //创建数组执行操作
    /* val a = new Array[Int](100)
     a(0)=110
     println(a(0))
    val strings =Array[String]("java", "scala", "hadoop")
    println(strings.length)*/
    //  使用变长数组执行操作实现和管理
    //  变长数组 ArrayBuffer 变长数组信息。创建变长数组信息
    /*val changeArray: ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer[String]()
    changeArray.append("hello")
    changeArray.append("scala")
    changeArray.append("java")
    changeArray.toSeq.foreach(println(_))*/
    //或者是使用如下的方式来使用变长数组信息
    /*val strings: ArrayBuffer[String] = ArrayBuffer[String]("test","good")
    strings.toSeq.foreach(println(_))*/
    //  变长数组增加，删除和修改元素操作
    //changeArray.append("many")
    //  返回删除的元素
    //val str: String = changeArray.remove(0)
    //println(str)
    //changeArray.toSeq.foreach(println(_))
    //  使更加简洁的方式实现数据增加，删除以及修改操作，或者是增加元素的操作的。
    //changeArray +="flume"
    //changeArray -="many"
    //changeArray.toSeq.foreach(println(_))
    // 将数组追加到变长数组之中的
    //val strings: Array[String] = Array("hive", "sqoop")
    //changeArray ++=strings
    //  修改操作
    //changeArray(0)="test"
    // 实现数组的遍历操作和实现机制的
    //  使用for来遍历数组执行操作的。
     val ints: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4, 5)
    //for(i<- ints) println(i)
    //或者是如下的.to方法是会包含开始和结束的值的
    //for(i <- 0 to ints.length-1 ) println(ints(i))
    //  使用until操作可以实现不包含相关的代码的操作实现机制的
    for(i <- 0 until  ints.length) println(ints(i))
  }

  def add(x:Int,y:Int)=x+y
}
