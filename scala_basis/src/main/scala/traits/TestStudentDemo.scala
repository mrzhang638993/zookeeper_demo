package traits
//测试相关的代码和实践机制
object TestStudentDemo {
  //在logger构造器中执行代码顺序
  trait Logger{
     println("执行Logger构造")
  }
  trait MyLogger extends Logger{
      println("执行MyLogger构造")
  }
  trait TimeLogger extends Logger{
    println("执行TimeLogger构造")
  }
  class  Person {
     println("执行Person构造")
  }
  class  Student extends Person with  MyLogger with TimeLogger{
      println("执行Student的构造")
  }
  //执行构造的顺序如下:最终通过如下的构造器完成最终的代码实现机制
  //1.父类的构造器
  //2.父类特质
  //3.从左到右的特质
  //4.自身的构造器
  /**
   *首先是类级别的层次的,然后是特质级别的父类，最后是特质的子类代码
   *
   * */
  def main(args: Array[String]): Unit = {
     val student = new Student
  }
}
