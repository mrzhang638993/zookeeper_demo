package traits
//特质:消息发送
trait MessageSender {
   //消息发送操作
   def send(msg:String)
}
//特质:消息接收
trait MessageReceiver{
   def receiver():String
}
//实现相关的特质操作，实现send以及receiver相关的方法
//使用with可以实现多个trait的特质信息。实现相关的特质操作实现逻辑
class MessageWorker extends MessageSender with MessageReceiver{
  override def send(msg: String): Unit = {
     println("发送消息:"+msg)
  }
  override def receiver(): String = {
     "接收信息,你好，我的名字叫一个好人"
  }
}
//特质的相关的操作实现
object TraitDemo{
  def main(args: Array[String]): Unit = {
    //创建对象信息
    val worker = new MessageWorker
    worker.send("我是一个好人")
    println(worker.receiver())
  }
}
