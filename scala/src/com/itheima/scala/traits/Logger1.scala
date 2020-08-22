package com.itheima.scala.traits


/**
 * 特质混入到对象中
 * */
trait Logger1 {

  val name:String="testFactor"

  def  log(msg:String)=println(msg)
}
