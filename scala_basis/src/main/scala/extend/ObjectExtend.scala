package extend

//class对象。
class Person1(var name:String)
// 实例继承class需要给定具体的实例数值来赋值操作的
object Student1 extends Person1(name="zhangsan"){

}


class Person2{
    var name:String=_
    def getName():String={
        name
    }
}

object Students2 extends Person2{
  // 首先打印父类的属性值的，然后打印自身的属性值信息的。
    println(name)
}
// 实现对应的对象继承操作实现的。
object ObjectExtend {
  def main(args: Array[String]): Unit = {
    Students2.name="hello world"
    println(Students2.name)
      //println(Student1.name)
  }
}
