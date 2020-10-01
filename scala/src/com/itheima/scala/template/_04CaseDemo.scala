package com.itheima.scala.template

 object _04CaseDemo{


   def main(args: Array[String]): Unit = {
     val lisi = Person2("lisi", 21)
     val lisi1 = Person2("lisi", 22)
     println(lisi.hashCode()) //1141881860
     println(lisi1.hashCode()) // 977205189
     //  样例类的属性值相同的话，对应的hashcode是相同的
   }
 }
