package com.itheima.scala.traits

trait Output {
  /**
   * 抽象日志方法
   * */
  def log(msg: String)

  /**
   * 定义具体的实现方法info,会调用抽象方法
   * */
  def info(msg: String) = log("信息:" + msg)

  /**
   * 定义warn设计方法,会调用抽象方法
   * */
  def warn(msg: String) = log("预警:" + msg)

  /**
   * 定义error的设计方法,会调用抽象方法
   * */
  def error(msg: String) = log("错误:" + msg)
}
