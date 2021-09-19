package instance

//测试isInstanceOf以及asInstanceOf等相关的代码
class Person
class Student extends Person

// 测试相关的isInstanceOf以及asInstanceOf等的相关的代码和逻辑实现
object TestInstance{
  def main(args: Array[String]): Unit = {
    //创建相关的student对象信息
    val student = new Student
    //使用isInstanceOf来进行类型的推断操作
    println(student.isInstanceOf[Person])
    if(student.isInstanceOf[Person]){
      val person: Person = student.asInstanceOf[Person]
      println(person)
    }else{
      //进行类型的转换操作
      println(student.isInstanceOf[Student])
    }
  }
}
