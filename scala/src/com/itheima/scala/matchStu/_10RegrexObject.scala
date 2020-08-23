package com.itheima.scala.matchStu

/**
 * scala的正则表达式对象
 * */
object _10RegrexObject {

  /**
   * 正则表达式语句，建议使用"""来进行描述
   * """正则表达式""".r
   * */
  def main(args: Array[String]): Unit = {
    /**
     * 正则表达式匹配邮箱合法性
     * 1.合法邮箱: qq12344@163.com
     * 2.不合法邮箱：qq12344@.com
     * */
    //  构建正则表达式
    var regex=""".+@.+\..+""".r
    var mail="qq12344@163.com"
    var mail1="qq12344@.com"
    var result=regex.findAllMatchIn(mail1).toList
    if (result.size==0){
      println("不合法的邮箱")
    }else{
      println("合法邮箱")
    }

    /**
     * 找到列表中所有的不合法的邮箱:
     * "38123845@qq.com"
     * "a1da88123f@gmail.com"
     * "zhansan@163.com"
     * "123afadff.com"
     * */
    var content=List("38123845@qq.com","a1da88123f@gmail.com","zhansan@163.com","123afadff.com")
    var regex1=""".+@.+\..+""".r
    // 使用偏函数过滤出来不合法的邮箱
    val list = content.filter {
      //  匹配过滤出来每一个元素
      case xm if regex1.findAllMatchIn(xm).size == 0 => true
      case _ => false
    }.toList
    println(list)
  }
}
