package extend

class Person{
    var name:String=_

   def  getName():String={
      name
   }
}

class Student extends  Person {
    //子类无法查看到父类的private name属性信息。不是private属性的是可以继承的。
    //scala中的属性操作不需要set方法的，只需要对象名称.属性名称即可了。
    def setName(name:String): Unit ={
       this.name=name
    }
}
//相关的测试代码和实现体系以及机制操作
//完成相关的代码实现和机制操作。
object TestExtend{
  def main(args: Array[String]): Unit = {
    val student = new Student
    student.name="lisi"
    println(student.name)
  }
}
