package com.itheima

import scala.collection.mutable.ArrayBuffer

/**
 * 数组相关的常见的算法操作和实现
 * */
object ArrayOperation {

  def main(args: Array[String]): Unit = {
    val ints: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4, 5)
    // 封装的常见的数组的sum方法的
    val sum: Int = ints.sum
    println(sum)
    // 获取数组的最大值
    println(ints.max)
    // 获取最小值信息
    println(ints.min)
    // 实现数组的排序操作，升序操作
    ints.sorted
    // 降序排序
    ints.sorted.reverse
  }
}
