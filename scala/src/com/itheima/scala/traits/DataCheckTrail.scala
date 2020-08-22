package com.itheima.scala.traits

/**
 * 数据校验trait
 * */
trait DataCheckTrail  extends  HandlerTrait {
  //  处理数据校验
  override def hander(data: String): Unit ={
    println("当前进行到了数据校验的过程中")
    super.hander(data)
  }
}
