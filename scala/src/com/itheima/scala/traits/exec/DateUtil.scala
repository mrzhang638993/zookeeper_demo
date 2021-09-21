package com.itheima.scala.traits.exec

import java.text.SimpleDateFormat
import java.util.Date

object DateUtil {

  val simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def parseDate(date: Date): String = simple.format(date)

}
