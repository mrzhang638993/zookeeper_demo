package com.itheima.stream.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
/**
 *
 * 数据落地到mysql数据库
 * */
object Sink_Mysql {

  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 创建连接操作实现
    env.setParallelism(1)
    //  加载数据
    val sourceDataStream: DataStream[(Int, String, String, String)] = env.fromCollection(List(
      (10, "dazhuang", "123456", "大壮"),
      (11, "erya", "123456", "二丫"),
      (12, "sanpang", "123456", "三胖")
    ))
    // 数据落地操作实现
    sourceDataStream.addSink(new MysqlSink() )
    // 执行数据落地操作实现
    env.execute()
  }
}

/**
 * 自定义的sink对象数据执行操作实现
 * */
class MysqlSink extends  RichSinkFunction[(Int,String,String,String)]{
  var  conn:Connection=null
  var  ps:PreparedStatement=null
  /**
   * open方法开启数据库连接
   * */
  override def open(parameters: Configuration): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    // 获取连接信息
    conn = DriverManager.getConnection("jdbc:mysql://cdh1:3306/spark_sql", "root", "123456")
  }

  /**
   * 执行具体的open方法操作
   * */
  override def invoke(value: (Int, String, String, String), context: SinkFunction.Context[_]): Unit = {
     val sql="insert into user(id,username,password,name) values(?,?,?,?)"
     ps= conn.prepareStatement(sql)
     ps.setInt(1,value._1)
    ps.setString(2,value._2)
    ps.setString(3,value._3)
    ps.setString(4,value._4)
    ps.executeUpdate()
  }

  /**
   * 重写close方法实现操作，关闭数据库连接操作实现
   **/
  override def close(): Unit = {
      if(ps!=null){
        ps.close()
      }
      if(conn!=null){
        conn.close()
      }
  }
}
