package com.itheima.stream.exec1

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

/**
 * 数据导入到数据库中执行操作
 * */
object Exec2 {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 执行流式操作处理
    env.setParallelism(1)
    // 设置
    val mysqlSource: DataStream[(Int, String, String, String)] = env.fromCollection(List(
      (13, "dazhuang", "123456", "大壮"),
      (14, "erya", "123456", "二丫"),
      (15, "sanpang", "123456", "三胖")
    ))
    //  增加数据导出的操作和实现
    mysqlSource.addSink(new MySqlSink2())
    // 执行打印输出操作实现
    mysqlSource.print()
    env.execute()
  }
}
/**
 * 执行rickFunction操作实现
 * */
class MySqlSink2 extends  RichSinkFunction[(Int,String,String,String)]{
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
