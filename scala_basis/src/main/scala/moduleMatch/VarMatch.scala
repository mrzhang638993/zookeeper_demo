package moduleMatch
//变量匹配中的模式匹配操作
//直接获取数组中的元素以及列表中的元素进行数据操作？
object  VarMatch {
  def main(args: Array[String]): Unit = {
     //变量匹配中的模式匹配操作和实现,
     val array: Array[Int] = (1 to 10).toArray
     //使用模式匹配来获取第二个,第三个以及第四个元素
    //变量生成的时候使用模式匹配获取变量中的元素信息
    val Array(_,x,y,z,_*)=array
    println(x)
    println(y)
    println(z)
  }
}
