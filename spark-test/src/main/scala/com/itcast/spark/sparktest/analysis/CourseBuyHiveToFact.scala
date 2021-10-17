package com.itcast.spark.sparktest.analysis

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

/**
 * 课程购买量事实表的生成操作逻辑和实现
 * 推荐使用mysql或者是redis完成数据的存储和操作实现,不要使用collect进行数据的收集操作，会导致内存溢出问题的。
 * */
object CourseBuyHiveToFact {
  def main(args: Array[String]): Unit = {
    //相关的session数据信息
    val session: SparkSession = SparkSession.builder().appName(this.getClass.getSimpleName)
      .master("local")
      .getOrCreate()
    //根据用户id获取得到对应的ip数据的，根据ip数据最终得到对应的区域id信息的
    var userMap=Map[String,String]()
    import session.implicits._
    session.sql("select * from date_dimen.user_dim").as[UserDimen].collect().map(x=>{
       userMap +=x.user_dim_id->x.ip
    })
    //得到区域维度的数据
    var areaMap=Map[String,String]()
    session.sql("select * from data_dimen.area_dim").as[AreaDimen].collect()
      .foreach(x => {
        areaMap += x.city->x.area_dim_id
      })
    //得到公司维度的数据
    var comMap=Map[String,String]()
    session.sql("select *  from  data_dimen.course_dim").as[CourseDimen].collect().
      foreach(x =>{
        comMap +=x.course_dim_id->x.company_id
      })
    //时间维度的数据需要自己处理操作和实现管理的。
    //获取事实表的数据进行数据的关联操作和实现。
    var dateInfo="2019-11-13"
    val sources: Dataset[CourseOrdersSource] = session.sql(s"select * from data_course.course_orders_source  where from_unixtime(order_time,'yyyy-MM-dd')=${dateInfo}").as[CourseOrdersSource]
    val caseValue: DataFrame = sources.map(obj => {
      //获取维度数据操作和实现
      //时间维度的数据,对应的是时间维度的数据操作和实现逻辑
      val timeDim: String = DateUtils.getDateStr(obj.order_time.toLong * 1000, "yyyy-MM-dd-HH")
      //区域id的数据信息,对应的获取ip的数据,ip数据可以不存在的
      val ipStr: String = userMap.getOrElse(obj.user_id, "")
      var areaDim: String = ""
      if (StringUtils.isNotEmpty(ipStr)) {
        areaDim = areaMap.getOrElse(ipStr, "")
      }
      //获取课程id的数据信息
      val details: String = obj.details
      //获取得到countryId
      val nObject: JSONObject = JSON.parseArray(details).getJSONObject(0)
      val courseId: String = nObject.getString("itemId")
      val countryId: String = comMap.getOrElse(courseId, "")
      //获取得到年，月，日的数据分区操作实现
      val timeArray: Array[String] = timeDim.split("-")
      //构建case对象
      CourseBuyFact(obj.order_number, courseId, timeDim, areaDim, countryId, obj.price.toFloat, obj.status, obj.user_id, timeArray(0), timeArray(1), timeArray(2))
    }).toDF()
    //保存数据到hive中,hive的动态分区操作的代码和相关的实现
    session.conf.set("hive.exec.dynamic.partition.mode","nonstrict")
    caseValue
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      //insertInto以及saveAsTable对应的使用的是相同的内容的。
      .insertInto("data_course.course_buy_fact")
    //获取是savede的执行结果进行数据的检查操作,可以用于检查相关的数据的操作实现的
    session.sql("select *  from data_course.course_buy_fact").show()
    session.close()
  }
}
