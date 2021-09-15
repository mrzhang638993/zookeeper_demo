package com.itheima

class Customer {
  /**
   * 定义成员变量
   * */
     var  name:String=_
     var sex:String=_

  /**
   * 定义成员方法
   * */
    def printHello(msg:String): Unit ={
        println(name+"==="+sex+"==="+msg)
    }
}
