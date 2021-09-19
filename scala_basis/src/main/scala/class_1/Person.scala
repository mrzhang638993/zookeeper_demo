package class_1

class Person
class Student extends  Person

// 测试getClass以及相关的classOf操作实现机制
object TestClass{
  def main(args: Array[String]): Unit = {
    val student = new Student
    if(student.getClass== classOf[Student]){
       println("使用getclass 获取得到对应的数据类型的===")
    }
  }
}

