package com.itheima.stream.datasource

import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

/**
 * flink流式处理操作实现
 * flink的outputTag必须是在主要的流之后进行的处理的。
 * 不要在生成流的时候就处理了相关的内容的，那样的话，会看不到对应的stream的信息的。
 *
 **/
object Datasource_Collection {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val tests = List( MyTest("1", "zhangsan"),  MyTest("2", "lisi"),  MyTest("1", "wangwu"), new MyTest("1", "zhaoliu"))
    val value1: DataStream[List[MyTest]] = env.fromElements(tests)
    /*val tag1 = new OutputTag[MyTest]("1")
    val tag2 = new OutputTag[MyTest]("2")*/
    val tags: List[OutputTag[MyTest]] = List[OutputTag[MyTest]](new OutputTag[MyTest]("1"), new OutputTag[MyTest]("2"))
    val value2: DataStream[MyTest] = value1.process(new ProcessFunction[List[MyTest], MyTest] {
      override def processElement(value: List[MyTest], ctx: ProcessFunction[List[MyTest], MyTest]#Context, out: Collector[MyTest]): Unit = {
        for (key <- value) {
          val id: String = key.id
          val list: List[OutputTag[MyTest]] = tags.filter(id1 => {
            val id2: String = id1.getId
            if (StringUtils.equals(id, id2)) true else false
          })
          val value3: OutputTag[MyTest] = list.apply(0)
          ctx.output(value3,key)
        }
      }
    })
    for(tag <- tags){
      val value4: DataStream[MyTest] = value2.getSideOutput(tag)
      value4.print();
    }
    /*val value4: DataStream[MyTest] = value2.getSideOutput(tags.apply(0))
    value4.print();*/

    /*val value5: DataStream[MyTest] = value2.getSideOutput(tags.apply(1))
    value5.print();*/

   /* // 设置并行度.默认的并行度和cpu的核数是一样的。
    env.setParallelism(1)
    //  导入数据
    val sourceValue: DataStream[Int] = env.fromCollection(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    //  打印数据
    sourceValue.print()*/
    // 运行任务（在批处理的使用print方法是可以触发任务的，在流环境下面是需要手动的触发任务的）
    env.execute("stream")
  }
}
