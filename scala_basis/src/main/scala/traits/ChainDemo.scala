package traits
//测试责任链模式代码实现
//责任链模式的核心是需要构建责任链模式的链条和运行机制的。这个机制没有实现成功的话，对应的是存在问题的
//需要指定链条体系的,使用super或者是next完成相关的体系的构建的操作的。
object ChainDemo {
  //定义责任模式的特质
  trait  HandlerTrait{
     def handler(data:String)={
         println("处理支付数据...")
     }
  }
  //实现数据校验操作逻辑
  trait  DataValidate extends HandlerTrait{
    override def handler(data: String): Unit = {
       println("进行到了数据校验阶段")
       super.handler(data)
    }
  }
  //实现签名校验逻辑,实现数据的签名校验逻辑
  trait SignatureValidateTrait extends HandlerTrait{
    override def handler(data: String): Unit ={
       println("实现到了签名校验阶段了:")
       //理论逻辑上面是存在问题的，每次调用的逻辑对应的是全部是自己的最上层的父集操作实现的
       super.handler(data)
    }
  }
  //调用方式是从右向左进行校验的
  class PayService extends DataValidate with SignatureValidateTrait {
    override def handler(data: String): Unit = {
       println("准备支付操作")
       super.handler(data)
    }
  }

  def main(args: Array[String]): Unit = {
    val service = new PayService
    service.handler("支付数据")
  }
}
