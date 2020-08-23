package com.itheima.scala.template

object _06ClassObject {


  def main(args: Array[String]): Unit = {
   // 样例对象可以作为枚举对象来实现相关的机制的。
   val zhansan = new SexPerson("zhansan", Male)
   val lisi = new SexPerson("lisi", Female)
   println(zhansan.name+"-----"+zhansan.sex)
   println(lisi.name+"-----"+lisi.sex)
  }
}