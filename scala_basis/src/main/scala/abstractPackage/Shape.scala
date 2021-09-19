package abstractPackage

// 定义抽象类和抽象方法
abstract  class Shape {
    // 抽象方法:对应的是不存在方法体的
    def area():Double
}
// 定义正方形实现相关的代码机制
class  Square(var length:Double) extends Shape{
    override def area(): Double = {
         Math.pow(length,2)
    }
}
// 计算长方形的面积公式，定义主构造器实现相关的操作
class Rectangle(var length:Double,var width:Double) extends Shape{
    override def area(): Double = {
        length*width
    }
}
//计算圆形的面积
class  Circle(var radis:Double) extends Shape{
    override def area(): Double = {
         Math.PI*Math.pow(radis,2)
    }
}
//创建实现类的对象,计算相关的面积
object SequreCalc{
    def main(args: Array[String]): Unit = {
        //计算正方形的面积
        val square = new Square(2)
        println(square.area())
        //计算长方形的面积
        val rectangle = new Rectangle(1.5, 3.8)
        println(rectangle.area())
        //计算圆形的面积
        val circle = new Circle(3)
        println(circle.area())
    }
}