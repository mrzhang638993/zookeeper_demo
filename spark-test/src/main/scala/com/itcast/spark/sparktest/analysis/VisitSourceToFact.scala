package com.itcast.spark.sparktest.analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 访问记录清洗数据，清洗数据到fact数据记录中
 * 1.关联维度
 * 2.过滤机器人
 * 3.去重
 * */
object VisitSourceToFact {
  /**
   * 可以使用内存来保存维度数据的。这样的话，后续的话就可以使用这些内存数据
   * 或者是使用redis来加载数据也是可以执行的。
   * 维度数据可以使用内存来实现相关的内存操作实现的。
   * */
    /**
     * 可以使用spark的groupBy实现分组操作，使用minBy或者maxBy就可以实现分组去重操作的。
     * */
  def main(args: Array[String]): Unit = {
    //创建本地的session数据信息
    val session: SparkSession = SparkSession.builder()
      .appName("VisitSourceToFact")
      .master("local")
      .getOrCreate()
    //执行相关的数据的操作实现.导入相关的隐式转换的操作类信息
    import session.implicits._
    //使用map数据结构保存区域维度的数据信息
    var areaMap=Map[String,String]()
     session.sql("select * from data_dimen.area_dim").as[AreaDimen].collect()
      .foreach(x => {
        areaMap += x.city->x.area_dim_id.toString
      })
    //获取课程的维度数据
    var comMap=Map[String,String]()
    session.sql("select *  from  data_dimen.course_dim").as[CourseDimen].collect().
      foreach(x =>{
        comMap +=x.course_dim_id->x.company_id
      })
    //获取事实表的数据
    val value1: RDD[String] = session.sparkContext.textFile("", 1)
    //string是不能创建dataFrame的，对应的是因为缺少相关的schema的信息的
    val visitClean: RDD[CourseVisitFact] = value1.map(x => {
      x.split("\t")
      //不包含机器人的数据的
    }).filter(arr => {
      !arr(5).contains("Robot")
    }).map(tmp => {
        val timeDim = DateUtils.getDateStr(tmp(6).toLong, "yyyy-MM-dd-HH")
        //区域维度数据,根据数据记录的ip数据得到区域维度数据
        val city: String = IpUtils.getArea(tmp(3))
        //根据城市维度得到对应的区域维度id数据
        val cityDim: String = areaMap.getOrElse(city, "")
        //获取公司维度id,根据课程id就可以得到数据
        val company_dim: String = comMap.getOrElse(tmp(0), "")
        //切割得到年月日的数据信息
        val dateStr: Array[String] = timeDim.split("-")
        CourseVisitFact(StrUtils.getUuId(), tmp(0), timeDim, cityDim, company_dim, tmp(3), tmp(2), tmp(5), tmp(6).toLong, dateStr(0), dateStr(1), dateStr(2))
      })
    //需要去重数据操作，根据session以及ip和课程id实现数据的去重操作实现
    val courseIter: RDD[(String, Iterable[CourseVisitFact])] = visitClean.groupBy(x => x.client_sessionid + x.client_ip + x.course_dim_id)
    //分组得到的是一个组中包含了很多的相同的记录的。minBy操作可以实现获取分组中最小的元素实现操作的。
    val cleanValue: RDD[CourseVisitFact] = courseIter.map(x => x._2.minBy(_.visit_time)).persist()
    //将清洗完成的数据保存到hdfs中的
    val frame: DataFrame = session.createDataFrame(cleanValue)
    //保存使用hive的动态分区操作实现
    //spark配置hive支持动态分区操作实现。
    session.conf.set("hive.exec.dynamic.partition.mode","nonstrict")
    //避免小文件过多的操作的，使用单个的分区可以解决这些问题。
    frame.repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("delimeter","\t")
      .csv("")
    session.close()
  }
}
