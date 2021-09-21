package com.itheima.scala.basis

import java.text.SimpleDateFormat
import java.util.Date

/**
 * 定义scala中的工具类。
 * 在java中使用的是静态类和方法的，
 * 在scala中使用单例模式实现上述功能实现
 * */
object DateUtils {

  def main(args: Array[String]): Unit = {
    val str = DateUtil.format(new Date())
    println(str)
  }

  /**
   * 1.步骤一：创建单例对象
   * */
  object DateUtil {
    /**
     * 2.步骤二：定义格式化的格式
     * */
    val formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * 3.定义日期格式化的方法
     */
    def format(date: Date) = formatDate.format(date)
  }

}
