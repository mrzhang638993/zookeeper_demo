package com.itcast.spark.sparktest


import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

import com.esri.core.geometry.{Geometry, GeometryEngine, Point, SpatialReference}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

import scala.io.Source


object TaxiAnalyseRunner   {


  /**
   * 执行汽车分析操作实现
   * */
  def main(args: Array[String]): Unit = {
    // 1.创建sparksession
    val spark: SparkSession = SparkSession.builder()
      .master("local[6]")
      .appName("testTaxi")
      .getOrCreate()
    // 2.导入隐式转换和函数门
    import spark.implicits._
    // 3.数据读取
    var schema = StructType(
      Seq(
        StructField("medallion",StringType),
        StructField("hack_license", StringType),
        StructField("pickup_datetime", DateType),
        StructField("dropoff_datetime", DateType),
        StructField("pickup_longitude", DoubleType),
        StructField("pickup_latitude", DoubleType),
        StructField("dropoff_longitude", DoubleType),
        StructField("dropoff_latitude", DoubleType)
      )
    )
    val taxiRaw: DataFrame = spark.read
      .option("header", true)
      .option("delimiter",",")
      .format("csv")
      //.schema(schema)
      .csv("F:\\works\\hadoop1\\zookeeper-demo\\spark-test\\src\\main\\scala\\com\\itcast\\spark\\sparktest\\half_trip.csv")
    taxiRaw.show()
    //  row的操作转成为rdd的操作实现的.对于数据异常的处理和操作实现逻辑处理操作实现。
    val taxiParsed: RDD[Either[Trip, (Row, Exception)]] = taxiRaw.rdd.map(safeParse(parse))
    // 使用Either对象进行操作处理实现的.使用Either对象返回对应的异常的信息的操作实现的。Either对象的操作使用很简单的。
    // 得到完整的安全的数据操作的。得到正常的数据的
    taxiParsed.filter(item=>item.isLeft).map(it=>it.left.get).toDF().show()
    val taxiGood: Dataset[Trip] = taxiParsed.map(item => item.left.get).toDS()
    taxiGood.show()
    // 绘制时长石方图
    // 编写udf函数，将毫秒数转化为小时单位的操作逻辑实现
    import  org.apache.spark.sql.functions._
    /**
     * 时长:将毫秒单位的时间转化成为对应的小时单位的计量单位
     * 获取持续的事件
     * */
    var hours=(pickUpTime:Long,dropOffTime:Long)=>{
      val durations=dropOffTime-pickUpTime
      val durationHour: Long = TimeUnit.HOURS.convert(durations, TimeUnit.MICROSECONDS)
      durationHour
    }
    val hour=udf(hours)
    // 进行统计操作。第一列对应的是时长的数据，第二个是单位时间内的交易量
    // 自定义的udf函数进行分组操作实现
    taxiGood.groupBy(hour($"pickUpTime",$"dropOffTime") as 'duration)
      .count()
      .sort('duration)
      .show()
    //   根据直方图的数据减除相关的数据的
    //  需要清理相关的数据的
    spark.udf.register("hours",hour)
    // 过滤出来的数据的
    val cleanValue: Dataset[Trip] = taxiGood.where("hours(pickUpTime,dropOffTime) between 0 and 1")
    cleanValue.show()
    // 上面的数据清洗完成了，下面需要进行更进一步的数据操作的。
    // 获取出租车在不同的行政区域的等待时间。
    // 理解Geojson执行相关的操作实现。GeoJson解析操作实现。
    // 7.获取行政区信息
    val geoJson: String = Source.fromFile("F:\\works\\hadoop1\\zookeeper-demo\\spark-test\\src\\main\\scala\\com\\itcast\\spark\\sparktest\\nyc-borough-boundaries-polygon.geojson").mkString
    //  7.1  读取数据集
    val featureCollection: FeatureCollection = FeatureExtraction.parseJson(geoJson)
    //  7.2  排序
    // 后续需要得到每一个出租车的行政区信息，获取到经纬度信息，遍历features得到对应的行政区
    // 在搜索的行政区中，面积较大的行政区的放置到前面的话，更加的容易命中的，减少次数。
    // 根据面积降序排列
    val features: List[Feature] = featureCollection.features.sortBy(item => {
      (item.properties("boroughCode"), -item.getGeometry().calculateArea2D())
    })
    //  7.3  广播
    // 数据量比较的少，可以发送广播的方式实现消息的发送操作,发送消息给每一个actuator
    val featuresBc: Broadcast[List[Feature]] = spark.sparkContext.broadcast(features)
    //  7.4  UDF创建，完成功能.下面的是UDF函数信息的
     var boroughLookUp=(x:Double,y:Double)=>{
       // 搜索判断是否可以找到对应的经纬度对应的行政区的数据信息
       val featureHit: Option[Feature] = featuresBc.value.find(item => {
         // 地图对象的点
         GeometryEngine.contains(item.getGeometry(), new Point(x, y), SpatialReference.create(4326))
       })
       // 获取行政区域的名称信息.返回行政区域的名称信息
       val borough: String = featureHit.map(item => item.properties("borough")).getOrElse("NA")
       borough
     }
    //  7.5  统计信息。自定义的udf转化成为spark中使用的udf函数进行操作实现
    val boroughUDF=udf(boroughLookUp)
    // 看看在每个出租车在那个行政区停留
    cleanValue.groupBy(boroughUDF('dropOffX,'dropOffY))
      .count().show()
  }




  /**
   * 对于对应的parse方法实现相关的封装操作实现
   * 正常的话返回正常的结果，
   * 异常的情况的话，返回的是初始值和对应的Exception异常信息的。
   * */
  def safeParse[P, R](f: P => R): P => Either[R, (P, Exception)] = {
    //  function需要在集群之间传递的，需要进行序列化的操作的
    new Function[P, Either[R, (P, Exception)]] with Serializable {
      override def apply(param: P): Either[R, (P, Exception)] = {
        try {
          Left(f(param))
        } catch {
          case e: Exception => Right((param, e))
        }
      }
    }
  }

  def parse(row: Row): Trip = {
    // 数据处理的时候需要处理数据的空的操作的
    val row1 = new RichRow(row)
    var license: String = row1.getAs[String]("hack_license").orNull
    val pickupDatetime: Long = parseDate(row1, "pickup_datetime")
    val dropoffDatetime: Long = parseDate(row1, "pickup_datetime")
    val pickupLongitude: Double = parseLocation(row1, "pickup_longitude")
    val pickupLatitude: Double = parseLocation(row1, "pickup_latitude")
    val dropoffLongitude: Double = parseLocation(row1, "dropoff_longitude")
    val dropoffLatitude: Double = parseLocation(row1, "dropoff_latitude")
    Trip(license, pickupDatetime, dropoffDatetime, pickupLongitude, pickupLatitude, dropoffLongitude, dropoffLatitude)
  }

  /**
   * 进行时间格式的转换操作和实现
   * */
  def parseDate(row: RichRow, field: String): Long = {
    //  获取英国时区的数据的
    var simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    //  对应的还需要处理成为空值的问题的
    val date: Option[String] = row.getAs[String](field)
    // 返回的是None或者是存在数据的时候才会返回数据的
    val longOption: Option[Long] = date.map(time => simple.parse(time).getTime)
    longOption.getOrElse(0L)
  }

  /**
   * 将字符串类型的转化为double类型的字符串的。
   * */
  def parseLocation(row: RichRow, field: String): Double = {
    val location: Option[String] = row.getAs[String](field)
    val doubleValue: Option[Double] = location.map(loc => loc.toDouble)
    doubleValue.getOrElse(0.0D)
  }
}

/**
 * row的包装类型数据的
 * */
class RichRow(row: Row) {
  // 为了返回option提供外面的支持操作和实现管理的
  def getAs[T](field: String): Option[T] = {
    if (row.isNullAt(row.fieldIndex(field))) {
      None
    } else {
      Some(row.getAs[T](field))
    }
  }
}


/**
 * 定义样例类对象
 * */
case class Trip(license: String,
                pickUpTime: Long,
                dropOffTime: Long,
                pickUpX: Double,
                pickupY: Double,
                dropOffX: Double,
                dropOffY: Double)  {
}

