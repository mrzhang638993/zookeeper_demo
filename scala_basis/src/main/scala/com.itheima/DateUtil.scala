package com.itheima

import java.sql.Date
import java.text.SimpleDateFormat

/**
 * 使用单例对象来创建工具类
 * 实现相关的工具类的操作和实现
 *
 * */
object DateUtil {

  def getDataStr(date:Date):String={
    if(date==null){
        return ""
    }
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    format.format(date)
  }

}
