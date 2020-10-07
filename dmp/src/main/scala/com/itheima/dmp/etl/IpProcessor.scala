package com.itheima.dmp.etl

import com.maxmind.geoip.{Location, LookupService}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{FloatType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.lionsoul.ip2region.{DbConfig, DbSearcher}

/**
 * 操作处理：将没有省市信息和经纬度的数据集，加入这些信息之后继续返回即可。
 **/
object IpProcessor extends Processor {
  /**
   * ip处理器
   **/
  override def process(dataset: Dataset[Row]): Dataset[Row] = {
    // 原始数据集,里面必须包含数据列字段ip信息.转换后的数据集必须追加region,city,longitude,latitude
    //  方法一:生成只有ip的数据，将ip对象转化成为(ip,region,city,longitude,latitude)，然后和原来的数据集根据ip操作进行join操作实现
    // 方法二： 在原始的数据集中增加4列region,city,longitude,latitude。使用withColumn，使用自定义的udf函数。
    //  方法三：生成新的数据，修改原来的schema的数据的。
    //  下面的操作选择方法三：
    // map以及mapPartition的区别和使用：mapPartitions会针对于每一个partitions进行操作的。而map操作会对于每一个元素进行操作的。
    val converted: RDD[Row] = dataset.rdd.mapPartitions(covertIp(_))
    // 需要生成新的schema信息的，需要增加region,city,longitude,latitude等的信息的
    val schema: StructType = dataset.schema
      .add("region", StringType)
      .add("city", StringType)
      .add("longitude", FloatType)
      .add("latitude", FloatType)
    // 创建一个新的dataset的
    val dataSetWithIp: DataFrame = dataset.sparkSession.createDataFrame(converted, schema)
    dataSetWithIp
  }

  def covertIp(iterable: Iterator[Row]): Iterator[Row] = {
    // 生成省市信息
    val dbSearcher = new DbSearcher(new DbConfig(), "F:\\works\\hadoop1\\zookeeper-demo\\dmp\\src\\main\\scala\\com\\itheima\\dmp\\utils\\ip2region.db")
    // 生成经纬度信息
    val service = new LookupService("F:\\works\\hadoop1\\zookeeper-demo\\dmp\\src\\main\\scala\\com\\itheima\\dmp\\utils\\GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE)
    iterable.map(item => {
      val ip: String = item.getAs[String]("ip")
      val regionAll: String = dbSearcher.binarySearch(ip).getRegion
      val region: String = regionAll.split("\\|")(2)
      val city: String = regionAll.split("\\|")(3)
      val location: Location = service.getLocation(ip)
      // 维度
      val latitude: Float = location.latitude
      // 精度
      val longitude: Float = location.longitude
      //  拼装row操作的
      val rowSeq = item.toSeq :+ region :+ city :+ longitude :+ latitude
      Row.fromSeq(rowSeq)
    })
  }
}
