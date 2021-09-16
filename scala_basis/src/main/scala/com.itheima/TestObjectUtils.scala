package com.itheima

import java.sql.Date

object TestObjectUtils {

  def main(args: Array[String]): Unit = {
    // 得到当前的时间信息
    val date: Date = new Date(System.currentTimeMillis())
    val str: String = DateUtil.getDataStr(date)
    println(str)
  }
}
