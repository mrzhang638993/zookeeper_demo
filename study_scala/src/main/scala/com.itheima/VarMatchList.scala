package com.itheima
//获取模式匹配中的数据的元素信息
//变量声明的时候使用相关的模式匹配操作实现相关的数据操作实现
object VarMatchList {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = (1 to 10).toList
    //val List(x,y,_*)=list
    //获取是使用如下的代码实现操作,对应的也是相关的模式匹配操作
    val x::y::tail=list
    println(x)
    println(y)
  }
}
