package com.itheima

import scala.util.control.Breaks.{break, breakable}

object ScalaBasis_1 {

  //  使用breakable实现相关的break以及continue的操作逻辑和实现的。
  def main(args: Array[String]): Unit = {
    // 使用break语句执行break的效果操作实现.操作的效果是跳出整个的循环的
     /*breakable{
        for(i<- 1 to 100) if(i==50) break else println(i)
     }*/
    // 实现continue操作效果和实现.可以实现相关的continue的效果。
    for(i<-1 to 5){
      breakable{
         if(i==2) break() else  println(i)
      }
    }
  }
}
