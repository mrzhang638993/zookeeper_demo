package com.itheima.scala.traits

object _08TrailDemo {

   /**
    * 首先执行签名校验，然后执行数据校验操作
    * 多个接口的调用方式:对应的调用方式的执行操作是从右往左执行的。
    * 首先执行SingnatureCheck操作，然后执行DataCheckTrail操作的
    * */
   class  PayService extends   DataCheckTrail with  SingnatureCheck{
     override def hander(data: String): Unit ={
       //执行具体的操作逻辑
       println("准备支付")
       super.hander(data)
     }
   }

  def main(args: Array[String]): Unit = {
    val service = new PayService
    service.hander("支付数据")
  }
}
