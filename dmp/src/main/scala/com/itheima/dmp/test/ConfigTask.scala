package com.itheima.dmp.test

import com.typesafe.config.{Config, ConfigFactory}

object ConfigTask {
  def main(args: Array[String]): Unit = {
    //  创建工具类
    val config: Config = ConfigFactory.load("test.conf")
    //  读取
    val bar: Int = config.getInt("foo.bar")
    val baz: Int = config.getInt("foo.baz")
    //  打印
    println(bar)
    println(baz)
  }
}
