package com.itheima.transformation


//张三,中国,江西省,南昌市
//李四,中国,河北省,石家庄市
//Tom,America,NewYork,Manhattan
// 转化为
//(张三,中国) (张三,中国,江西省) (张三,中国,江西省,南昌市) (李四,中国) (李四,中国,河北省) (李四,中国,河北省,石家庄市)

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object FlatMapTrans {
  /**
   * flatMap的trans操作实现
   **/
  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 使用flatmap算子执行map操作实现
    val dataStr: DataSet[String] = env.fromCollection(List[String]("张三,中国,江西省,南昌市", "李四,中国,河北省,石家庄市", "Tom,America,NewYork,Manhattan"))
    //进行转换操作。flatMap返回的是数组的数据的
    val flatMapData: DataSet[Product] = dataStr.flatMap {
      item => {
        val arr: Array[String] = item.split(",")
        // flatmap一次性的返回的是多个数组的。
        List((arr(0), arr(1)), (arr(0), arr(1), arr(2)), (arr(0), arr(1), arr(2), arr(3)))
      }
    }
    flatMapData.print()
  }
}
