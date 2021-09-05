package com.itheima

/**
 * 测试相关的定义的操作方法和测试逻辑
 * */
object MethodTest {

  def main(args: Array[String]): Unit = {
    /*val addResult: Int = add(2, 3)
    println(addResult);
    val threeMulti: Int = addMulti(1, 2, 3)
    println(threeMulti)
    val destResut: Int = jiecheng(10)
    println(destResut)
    val result: Int = add(x = 3)
    println(result)*/
   /* val count: Int = addChange(1, 3, 5, 6)
    println(count)*/
    // 方法的调用模式:
    // 1.对象名称.方法名称(参数);2.
    /*val i: Int = Math.abs(-1)
    println(i)*/
    // 中缀调用的方式: 例如 1 to 10  之间使用空格来调用的,scala独有的函数调用方式。
    //println(Math abs -1)
    // scala中对应的理念  操作符即方法。scala中所有的操作符都是方法，方法名称是操作符号。
    //花括号调用逻辑和实现操作技巧，只有一个参数的时候才可以调用的
   /* val result: Int = Math.abs {
      println(5) //  花括号表达式实现操作指导
      1 + 2  //  最后一个表达式的数值对应的是表达式的数值的。
    }
    println(result)*/
    //无括号调用方法实现相关的操作
    m3()
  }
  def add(x:Int,y:Int): Int ={
    x+y
  }
  def  addMulti(x:Int,y:Int,z:Int):Int={
     add(add(x,y),z)
  }
  // 递归调用的时候,对应的是需要给出返回值的类型的
  def jiecheng(x:Int):Int={
     if(x<=1) 1 else x*jiecheng(x-1)
  }
  // 定义方法的默认值执行默认值的相加减的操作逻辑处理
  def changeLength(x:Int=0,y:Int=0){
     add(x,y)
  }
  //  定义变长的字符串信息处理操作
  def add(x:Int=0,y:Int=2,w:Int=4)=x+y+w;
  // 定义变长参数调用操作
  def addChange(x:Int*): Int ={
    /*var count=0;
     for(i <- x)  count=count+i;
     count*/
    x.sum
  }
  def m3()=println("hello")
}
