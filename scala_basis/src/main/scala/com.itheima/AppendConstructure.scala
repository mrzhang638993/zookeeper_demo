package com.itheima

/**
 * 辅助类构造器实现相关操作
 * */
class AppendConstructure(var name:String,var address:String) {

    // 接受一个辅助构造器完成相关的初始化操作实现。
    // 辅助构造器的编程，对应的采用的是this实现相关的代码的操作的。
    def this(args:Array[String]){
      //  调用主构造器实现初始化操作的。辅助构造器的第一句代码必须是调用主构造器或者是其他的构造器的
      this(name=args(0),address=args(1))
      // 使用辅助构造器来实现赋值操作和实现管理的，
      // 实现了辅助构造器实现相关的操作的。
    }
}
