package com.itheima.scala.matchStu

import scala.io.StdIn

object  _03ClassObject {

  def main(args: Array[String]): Unit = {
    val count = StdIn.readInt()
    count match {
        /**
         * 模式匹配，匹配指定的范围。相较于java的if...else操作。
         * scala的match中的if称之为守卫
         * */
      case x if  x>=0&&x<=3=>println("x大于等于0小于等于3")
      case x if x>=4&&x<=8=>println("x大于等于4小于等于8")
      case _=>println("未匹配到任何的内容")
    }
  }
}
