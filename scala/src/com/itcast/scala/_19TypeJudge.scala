package com.itcast.scala

/**
 * 执行类型判断操作
 *
 * isInstanceOf/asInstanceOf
 * getClass/classOf
 *
 * */
object _19TypeJudge {

    /**
     * isInstanceOf:判断对象是否是指定类型的对象。判断对象是某个类型或者是某个类型的子类对象。
     * asInstanceOf:将对象转化为指定类型
     *
     * scala中和类型相关的都是采用方括号进行类型判断的。
     * 判断类型和类型转换操作
     * */
    class  Person(val name:String="testPerson",val age:Int=20){
       def getName()=name
    }
    class  Student extends  Person{
      override val name: String = "good morning"
      override def getName(): String = name
    }

     def main(args: Array[String]): Unit = {
       val student = new Student
        // 类型相关的都是采用[]进行类型的判断操作
        println(student.isInstanceOf[Student])
        println(student.isInstanceOf[Person])
       //   类型匹配的话，执行类型的判断操作。并且类型的转换操作
       if(student.isInstanceOf[Person]){
         //  asInstanceOf 将对应的转化成为相关的对象的实例。对应的是Person对象的实例
          var person=student.asInstanceOf[Person]
          println(person.getName())
       }
     }
}
