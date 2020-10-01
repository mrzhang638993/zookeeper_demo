package com.itcast.spark.sparktest

import org.json4s.jackson.Serialization

object JsonClass {

  def main(args: Array[String]): Unit = {
    val product =
      """
        |{"name":"Toy","price":35.35}
      """.stripMargin
    import org.json4s._
    import org.json4s.jackson.JsonMethods._
    val jValue: JValue = parse(product)
    // 获取解析后的对象属性值
    // println(jValue \ "name")
    // 导入隐式转换的参数。formats对应的是转换的规则的
    implicit  val formats=Serialization.formats(NoTypeHints)
    // 解析为某一个对象
    val prodObj: Products = parse(product).extract[Products]
    println(prodObj.name+"======"+prodObj.price)
    println(prodObj)
    //  下面使用更加简单的方式来实现json的转换操作和实现规则的
    import org.json4s.jackson.Serialization.{read, write}
    // 可以直接将字符串转换为json对象的。
    val prodObj2: Product = read[Product](product)
    println(prodObj2)
    // 对象转化为json字符串形式的数据的
    val  prod3:Products=Products("电视",10.5)
    //  语句存在的问题和解决办法？不要使用对象进行操作实现的
    //val prodJvalue: JValue = parse(prod3).extract[Products]
     //val str= compact(render(prodJvalue))
    // 获取json对象的过程
    val jsonStr: String = write(prod3)
    println(jsonStr)
    //  read将字符串序列化为一个对象的，write将对象序列化为一个字符串的
    // java中也存在很多的解析geojson对象的，但是很多的依赖于底层的C语言操作的。

  }
}

case class Products(name:String,price:Double)
