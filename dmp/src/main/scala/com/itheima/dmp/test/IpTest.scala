package com.itheima.dmp.test

import com.maxmind.geoip.{Location, LookupService}
import org.junit.Test
import org.lionsoul.ip2region.{DataBlock, DbConfig, DbSearcher}

class IpTest {
  @Test
  def ip2Region(): Unit = {
    // ip2region 根据ip查询地域信息
    val searcher = new DbSearcher(new DbConfig(), "F:\\works\\hadoop1\\zookeeper-demo\\dmp\\src\\main\\scala\\com\\itheima\\dmp\\utils\\ip2region.db")
    val block: DataBlock = searcher.btreeSearch("47.114.186.104")
    println(block.getRegion)
    //  获取经纬度信息。
  }

  /**
   * 根据ip获取区域信息
   **/
  @Test
  def ip2Location(): Unit = {
    //  创建入口
    val service = new LookupService("F:\\works\\hadoop1\\zookeeper-demo\\dmp\\src\\main\\scala\\com\\itheima\\dmp\\utils\\GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE)
    //  设置数据的内存缓存操作实现
    val location: Location = service.getLocation("47.114.186.104")
    println(location.city)
  }
}
