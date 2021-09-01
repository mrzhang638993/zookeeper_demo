package com.itheima.sql

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.api.{Table, TableEnvironment}

import java.util.UUID
import java.util.concurrent.TimeUnit
import scala.util.Random


//步骤
//1. 获取流处理运行环境
//2. 获取Table运行环境
//3. 设置处理时间为EventTime
//4. 创建一个订单样例类Order ，包含四个字段（订单ID、用户ID、订单金额、时间戳）
//5. 创建一个自定义数据源
//使用for循环生成1000个订单
//随机生成订单ID（UUID）
//随机生成用户ID（0-2）
//随机生成订单金额（0-100）
//时间戳为当前系统时间
//每隔1秒生成一个订单
//6. 添加水印，允许延迟2秒
//7. 导入import org.apache.flink.table.api.scala._ 隐式参数
//8. 使用registerDataStream 注册表，并分别指定字段，还要指定rowtime字段
//9. 编写SQL语句统计用户订单总数、最大金额、最小金额
//分组时要使用tumble(时间列, interval '窗口时间' second) 来创建窗口
//10. 使用tableEnv.sqlQuery 执行sql语句
//11. 将SQL的执行结果转换成DataStream再打印出来
//12. 启动流处理程序
object StreamFlinkSqlDemo {
  def main(args: Array[String]): Unit = {
    // 获取流处理环境执行操作实现
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //  设置并行度
    env.setParallelism(1)
    //  获取table的运行环境
    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //  设置水印时间进行操作,便于后续的sql查询操作
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //  增加数据源进行操作实现
    val sourceValue: DataStream[Order12] = env.addSource(new MySelfSource)
    // 增加水印操作实现
    val waterDataStream: DataStream[Order12] = sourceValue.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[Order12] {
      //  解决网络延时，增加水印操作实现
      var currentTimestamp: Long = 0L;

      //  允许延迟2秒钟设置水印时间。
      override def getCurrentWatermark: Watermark = {
        new Watermark(currentTimestamp - 2000)
      }

      override def extractTimestamp(element: Order12, previousElementTimestamp: Long): Long = {
        currentTimestamp = Math.max(element.createTime, System.currentTimeMillis())
        currentTimestamp
      }
    })
    //  最终形成最终的数据操作实现和管理实现
    import org.apache.flink.table.api.scala._
    // 注册使用rowTime实现相关的代码的操作和实现机制的。
    tableEnv.registerDataStream("t_order",waterDataStream,'id, 'userId, 'money, 'createTime.rowtime)
    //  编写sql语句进行统计操作实现
   // 编写SQL语句统计用户订单总数、最大金额、最小金额。分组操作必须要使用到tumble函数才可以操作的。
    val sql=
   """
     |select userId,max(money),min(money),count(1) from t_order   group by userId,tumble(createTime, interval '5' second)
     |""".stripMargin
    val table: Table = tableEnv.sqlQuery(sql)
    table.printSchema()
    //  使用toAppendStream  需要制定table是只读的。
   //val value: DataStream[Row] = tableEnv.toAppendStream[Row](table)
    // 下面的语句存在问题的，需要进行关注的。
    // Exception in thread "main" org.apache.flink.table.api.TableException: Result field does not match requested type. Requested: String; Actual: Integer
    // val value: DataStream[Order12] = tableEnv.toAppendStream[Order12](table)
   val value: DataStream[(Boolean, (Int,Int,Int,Long ))] = tableEnv.toRetractStream[(Int,Int,Int,Long)](table)
    value.print()
    env.execute()
  }
}

case class Order12(id:String,userId:Int,money:Int,createTime:Long)
/**
 * 自定义数据源，不断的生成Order12的数据对象进行操作实现
 * */
class  MySelfSource  extends  RichSourceFunction[Order12]{
  var isRunning=true
  override def run(ctx: SourceFunction.SourceContext[Order12]): Unit = {
     while(isRunning){
        for(i<- 0 until 1000){
          val order1: Order12 = Order12(UUID.randomUUID().toString,  Random.nextInt(3), Random.nextInt(101), System.currentTimeMillis())
         ctx.collect(order1)
          // 每1秒生成一个订单
           TimeUnit.SECONDS.sleep(1)
        }
     }
  }
  override def cancel(): Unit = {
    isRunning=false
  }
}
