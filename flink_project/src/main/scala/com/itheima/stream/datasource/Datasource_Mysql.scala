package com.itheima.stream.datasource

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 读取mysql数据执行操作
 * 创建mysql的source数据源读取数据
 **/
object Datasource_Mysql {
  def main(args: Array[String]): Unit = {
    //  创建流式运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //  读取数据
    import org.apache.flink.api.scala._
    val sqlValue: DataStream[(Int, String, String, String)] = env.addSource(new MysqlSource)
    sqlValue.print()
    //  程序执行
    env.execute("mysql")
  }
}

/**
 * 自定义的数据源，一定需要实现RichSourceFunction类信息的，后面的是返回的数据信息的
 **/
class MysqlSource extends RichSourceFunction[(Int, String, String, String)] {
  // 连接mysql数据库驱动
  override def run(ctx: SourceFunction.SourceContext[(Int, String, String, String)]): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    // 获取连接信息
    val conn: Connection = DriverManager.getConnection("jdbc:mysql://cdh1:3306/spark_sql", "root", "123456")
    val sql = "select id,username,password,name from user"
    val ps: PreparedStatement = conn.prepareStatement(sql)
    val set: ResultSet = ps.executeQuery()
    while (set.next()) {
      val id: Int = set.getInt(1)
      val username: String = set.getString(2)
      val password: String = set.getString(3)
      val name: String = set.getString(4)
      // 收集数据。整个很关键的。
      ctx.collect((id, username, password, name))
    }
  }

  override def cancel(): Unit = {

  }
}
