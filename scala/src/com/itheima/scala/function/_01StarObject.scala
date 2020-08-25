package com.itheima.scala.function

import scala.collection.immutable

object _01StarObject {


  def intToStr(num:Int):String="*"*num
  def main(args: Array[String]): Unit = {
    val ints = List(1, 2, 3, 4, 5)
    //val list: List[String] = ints.map("""*""" * _ )
    // 或者是采用下面的方法的。加上括号的话，可以分开相关的参数和结果的
    val list: List[String] = ints.map((num:Int) =>"*" * num )
    /**
     * val list: List[Int=>String] = ints.map(_ =>"x" * _ )
     * 使用这种用法的错误之处在于，map方法会将_ =>"x" * _作为返回值进行返回操作的。而不是进行计算操作的
     * 对于scala中的方法的返回值的理解需要加强的。
     */
    println(list)
    //  这里面函数可以作为参数传递给方法进行执行，称之为作为值的函数。
    val strings: immutable.IndexedSeq[String] = (1 to 10).map(intToStr)
    println(strings)
  }
}
