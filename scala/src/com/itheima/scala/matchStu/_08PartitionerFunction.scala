package com.itheima.scala.matchStu

object _08PartitionerFunction{

   def main(args: Array[String]): Unit = {
     //  定义偏函数。没有match的相关语句的
      var partitionFunction:PartialFunction[Int,String]={
        case 1=>"一"
        case 2=>"二"
        case 3=>"三"
        case 4=>"四"
      }
     //  偏函数调用
     println(partitionFunction(1))
     println(partitionFunction(2))
     println(partitionFunction(3))
   }
 }
