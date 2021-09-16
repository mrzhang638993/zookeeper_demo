package com.itheima

/**
 * 使用主构造器对应的构建一个相关的class对象信息的。
 * 主构造器中不能使用_来实现参数赋值的
 * 需要使用常量数值来完成相关的代码的开发和实现操作的
 * 只有在class内部的成员变量的话，才可以使用下划线操作的。
 * */
class MainConstructure(var name:String="",val age:Int=0) {
  // 对应的是主构造器的代码实现的。
   println("调用主构造器")

}
