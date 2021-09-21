package com.itheima.scala.traits

/**
 * 特质中增加具体的实现方法
 * */
trait Logger {
  /**
   * 特质：提供方法的默认实现.特质中可以增加默认的接口实现的。
   * */
  def log(msg: String) = println(msg)
}
