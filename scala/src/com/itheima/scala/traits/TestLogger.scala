package com.itheima.scala.traits

import java.text.SimpleDateFormat

/**
 * 实现日志的输出功能和日志进行操作
 * */
trait TestLogger {

  /**
   * 定义一个时间格式的转换，进行时间格式的转换操作
   * */
  var simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  /**
   * 定义type类型，确定日志的输出操作
   * 抽象字段，没有任何的初始化的数值
   * 具体字段的是需要初始化的
   * */
   var Type:String

  /**
   *  定义log抽象方法，用于日志的输出操作
   *  具体的方法是需要实现的。抽象的方法是不需要的。
   * */
  def  log(msg:String)

}
