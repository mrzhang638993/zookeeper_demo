package com.itheima.scala.traits.exec

import java.util.Date

object _01_EXECObject {


  def main(args: Array[String]): Unit = {
    val str = DateUtil.parseDate(new Date())
    println(str)
  }
}
