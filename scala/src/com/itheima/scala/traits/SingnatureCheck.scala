package com.itheima.scala.traits

/**
 * 签名校验操作
 * */
trait SingnatureCheck  extends  HandlerTrait {
  override def hander(data: String): Unit ={
    println("当前进行到了签名校验中来了")
    super.hander(data)
  }
}
