package com.itheima.sql

//以 流 处理方式，加载下列数据，并注册为表，查询所有数据，写入到CSV文件中。
//id     product     amount
//1         beer             3
//2         diaper         4
//3         rubber         2

// 2、
//
//使用Flink SQL统计用户消费订单的总金额、最大金额、最小金额、订单总数。
//订单id     用户名     订单日期                             消费基恩
//1        zhangsan 2018-10-20 15:30                     358.5
//
//测试数据（订单ID、用户名、订单日期、订单金额）
//(1,"zhangsan","2018-10-20 15:30",358.5),
//(2,"zhangsan","2018-10-20 16:30",131.5),
//(3,"lisi","2018-10-20 16:30",127.5),
//(4,"lisi","2018-10-20 16:30",328.5),
//(5,"lisi","2018-10-20 16:30",432.5),
//(6,"zhaoliu","2018-10-20 22:30",451.0),
//(7,"zhaoliu","2018-10-20 22:30",362.0),
//(8,"zhaoliu","2018-10-20 22:30",364.0),
//(9,"zhaoliu","2018-10-20 22:30",341.0)
object Exec1 {
  def main(args: Array[String]): Unit = {

  }
}
