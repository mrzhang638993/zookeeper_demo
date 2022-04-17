package com.itcast.spark.sparktest

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

//spark相关的dataSource的学习和理解实现
object SparkDataSourceStudy {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[1]")
      .appName("testDataSource")
      .getOrCreate()
    //val frame: DataFrame = spark.read.parquet("spark-test/person.parquet")
    //frame.write.save("spark-test/load.parquet")
    //save操作的时候指定相关的格式,需要注意的是parquet是不需要执行后缀的操作的
    //frame.select("name","age").write.format("parquet").save("spark-test/saveFormat")
    //csv对应的也是不需要指定文件后缀的操作的。对应的文件后缀也是会作为文件名称的一部分的。
    /*frame.write
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .mode(SaveMode.Append).csv("spark-test/person.csv")*/
    //txt文件的写入操作的话，只能支持单列数据的写入操作的，对于多列数据的写入是不支持的。
    //对于txt文件而言,对应的也不需要保留对应的文件类型的后缀的，会自动的生成对应的文件名称的文件夹,使用的是spark格式支持的文件的。
    /*frame.
      select("name").
      write
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .mode(SaveMode.Append).text("spark-test/newPerson")*/
    //加载csv文件的数据到dataFrame中,进行数据的加载操作实现
    val schema=StructType(Array(StructField("name",StringType),StructField("age",IntegerType)))
    val person: DataFrame = spark.read
      .format("csv")
      //时间格式的问题,可以设置时区来操作。
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      //指定了对应的schema信息
      .schema(schema)
      .option("sep", ",")
      //下面的操作会进行schema的推断的，会进行全局扫描的，不推荐使用
      /*.option("inferSchema", "true")*/
      //指定数据中是否包含了header的信息的
      /*.option("header", "true")*/
      .load("spark-test/person.csv")
    //保留了对应的header的信息
   /* person.write.option("header","true").option("sep",";")
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .mode(SaveMode.Overwrite).format("csv").save("spark-test/header")*/
    //使用来保存对应的orc文件执行orc文件操作
    //默认的orc采用的是snappy的模式进行压缩存储的。
   /* person.write.format("orc")
      //只是使用name列的属性，这些属性的作用是什么，尚未清楚。
      .option("orc.bloom.filter.columns", "name")
      .option("orc.dictionary.key.threshold", "1.0")
      .option("orc.column.encoding.direct", "name")
      .save("spark-test/users_with_options")*/
    //格式信息
   /* person.write.format("parquet")
      .option("parquet.bloom.filter.enabled#favorite_color", "true")
      .option("parquet.bloom.filter.expected.ndv#favorite_color", "1000000")
      .option("parquet.enable.dictionary", "true")
      .option("parquet.page.write-checksum.enabled", "false")
      //不需要指定对应的parquet的约束信息。
      .save("spark-test/users_with_options")*/
    //对应的直接基于sql来读取文件实现操作管理,直接基于spark sql读取相关的文件实现操作
  /* val frame: DataFrame = spark.sql("select *   from parquet.`spark-test/load.parquet`")
    frame.show()*/
    /**spark数据持久化操作,对应的table持久化，数据会一直存在的。用户指定路径的话，table删除了，对应的文件还是会存在的，
      用户不指定路径的话，对于默认的路径而言,表删除的话，对应的文件是会被删除的。
     */
    //table的逻辑概念,可以采用相关的sql来查询的
    /*person.write.mode(SaveMode.Overwrite).option("path","spark-test/table").saveAsTable("person")
    spark.sql("select *  from person").show()*/
    //同步分区消息,可以使用MSCK REPAIR TABLE.执行相关的命令信息。
    //bucket,sort以及分区操作，输出操作的时候，可以使用这样的方式的。
    //bucket以及sort仅仅适合持久化操作的table。spark默认的是parquet的snappy的压缩方式的。
    /*person.write.bucketBy(2,"name","age").sortBy("name")
      .mode(SaveMode.Overwrite)
      .option("path","spark-test/person_bucket")
      .saveAsTable("t_person")
    spark.sql("select *  from  t_person").show()*/
    //partition可以用于save以及对应的saveAsTable的操作的。可以在保存output的时候指定对应的分区操作
    //可以指定多个分区键来实现分区操作实现的。这个操作很优秀,可以在output操作的时候根据分区将数据写入到分区中
    //person.write.partitionBy("name").format("parquet").save("spark-test/partition")
    //持久化的table则可以使用更多的方式，包括partitionBy,bucketBy,以及对应的sortBy的数据来操作的
    //bucket的name不能是分区的字段的。sort也不能是分区的字段的
    person.write.partitionBy("name").bucketBy(2,"age")
      .sortBy("age")
      .option("path","spark-test/table_partition")
      .mode(SaveMode.Overwrite)
      .saveAsTable("t_table")
  }
}
