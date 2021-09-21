package com.itheima.scala.basis

/**
 * 抽象类和抽象方法的定义
 * */
object _21AbstractClass {

  def main(args: Array[String]): Unit = {
    //  定义正方形执行操作
    var square = new Square(2.0);
    println(square.square())
    //  定义长方形的面积
    var retangle = new Retangle(4, 5);
    println(retangle.square())
    // 定义圆的面积计算
    var yuan = new Circle(4.0)
    println(yuan.square())
  }

  abstract class Sharp {
    //  抽象方法对应的是没有方法体的
    def square(): Double
  }

  // 使用主构造器
  class Square(var edge: Double) extends Sharp {
    // 计算正方形的面积
    override def square(): Double = edge * edge
  }

  //计算长方形的面积，长乘以宽.使用主构造器
  class Retangle(var width: Double, var length: Double) extends Sharp {
    override def square(): Double = width * length
  }

  // 计算圆的面积，使用主构造器
  class Circle(var radius: Double) extends Sharp {
    override def square(): Double = Math.PI * radius * radius
  }
}
