package com.itheima.scala.matchStu

object _05ClassObject {
  /**
   * 匹配集合操作
   * */
  def main(args: Array[String]): Unit = {
    var arr1:Array[Int]=Array(1,2,3)
    // 数组单个元素为0
    var  arr2:Array[Int]=Array(0)
    // 0开头的，后面任意多个的成员
    var arr3:Array[Int]=Array(0,1,2,3,4,5)
    arr3 match {
      //array加上数组的方式实现匹配操作的
      case Array(1,x,y)=>println(s"匹配三个元素的数组，第一个元素为1，后面的为${x}:${y}")
      case  Array(0)=>println(s"匹配单个元素，元素值为0")
      case Array(0,_*)=>println("匹配到了0开头的，后面的元素个数不固定的")
    }
  }
}
