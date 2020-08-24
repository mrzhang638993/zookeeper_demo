package com.itheima.scala.extractor

object _05GerernicType {
  //  实现非变，协变，逆变的方式比较
  def main(args: Array[String]): Unit = {
    val temp1:Temp1[Sub] = new Temp1[Sub]
    //  非变不允许任何的类型的转换。
    //val  temp2:Temp1[Super]=temp1

    /**
       协变：协变情况下，是可以将一个子类父子给父类的。
       Temp2[Sub]对应的是Temp2[Super]的一个子类的
     */
     var  temp3:Temp2[Sub]=new Temp2[Sub]
     var  temp4:Temp2[Super]=temp3

    /**
     * 下面是逆变的演示
     * 逆变的情况下：Temp3[Sub]认为是Temp3[Super]的父类的。不能将父类赋值给一个子类的
     * 可以将Temp3[Super]赋值给父类Temp3[Sub]的
     * */
    var  temp5:Temp3[Super]=new Temp3[Super]
    var  temp6:Temp3[Sub]=temp5
  }
}
