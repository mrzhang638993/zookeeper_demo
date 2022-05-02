出租车的要点：
1.根据出租车上下坐标点的位置需要计算出来对应的行政区域的名称；
需要使用到geoJson处理坐标的？
2.需要区分每一个车辆的一次的阶段，包括载客开始和载客结束的时间点
需要进行session划分操作？
3.需要计算连续两次的载客过程中的间隔操作。也就是等待时间
需要确认是同一个人的连续的上车时间和下车时间的。
4.根据得到的区域和对应的每一次的出租的等到时间，得到区域的名称和平均的载客时间间隔的。
5.数据的过滤和清洗操作？设计到大量的ETL的过程和相关的实际操作实现的。
##### 流式计算的操作和实现的
sparkStreaming 对应的是早期的流式计算的操作的。
structuredStreaming对应的是最新的流式计算的操作点的。
流计算的应用场景和流计算的架构设置的？
流计算和批量计算以及离线计算的区别：
1.批量计算:数据已经存在,使用spark等的操作最终将文件存储到hdfs上面去的操作的。要求的是数据量比较大的。
批量计算的目标往往是形成数据仓库的。指标预测等的场景就可以使用redis的数据的。
批量计算对应的是离线计算的。数据是确定的。批量计算往往会计算全量的数据的。处理的是全量数据的。
2.流式计算:数据是源源不断的产生的。经过处理之后数据落地的。外部可以使用图形化的webui来展示相关的技术指标的。
要求快速处理的，处理的是增量数据的。
流式计算可以将数据存储到hbase以及
流式计算的架构和批处理的架构整合在一起称之为流式计算架构。称之为lamada架构。现阶段的架构很难维护，现阶段很少使用的
需要维护流式架构和批处理架构的。一般的是流式架构就是流式架构的，一般的批处理架构对应的采用的是批处理架构的。
流式架构在大规模的处理上面的性能比较的差的。很少使用的。流式计算是少量数据处理的。
批量计算对应的也称之为离线计算的。是针对于批量数据进行操作处理的。
kafka可以处理流式数据和批处理数据的。
Cassandra数据库:无中心的架构的
hbase:有中心的架构的。
sparkStreaming是针对于spark core  api的处理的。是spark1.0的时代的产物的。
机器学习和图计算比较倾向于使用sparkStreaming进行计算的。spark  streaming对应的是微批次的处理操作的。
是将批次的数据转化为rdd的操作的，处理的是批量的操作处理的。是小批量的操作处理实现的。
socket是java支持tcp/Udp协议的编程模型的。socket支持tcp以及udp的操作的。
tcp保证连接的安全的。udp只是传递数据的，不保证数据的安全的。socket支持udp以及tcp的
socket编程模型包含了server部分以及client部分的。
socketserver被动接受client的链接操作的。比较常见的是tcp的。http底层也是基于tcp连接的。
tcp的连接过程
1.客户端发送请求到服务端，syn请求操作;
2.服务端发送客户端连接确认信息ack请求，同时还发送连接客户端的syn请求
3.客户端返回确认的ack连接请求。
zookeeper中存在4字命令，对应的是管理端的命令的。
netcat可以发送命令检测请求的。netcate的命令简称职位nc的操作的。
nc  -lk 9999  #创建socketServer
nc  localhost  9999  #连接端口对外提供服务操作。

sparkStreaming 实时计算uv指标数据需要执行如下的方案的：
1.用户信息的判别采用的是用户id,sessionId以及ip数据形成的一个整体来实现操作的
2.实现计算pv操作的话，需要将每一个用户key保存到redis中的。
使用redis的set集合保存对应的用户的key的数据的。这样的话,并且保证设置redis的失效时间是对应的第二天的开始的
set集合只会保存不重复的数据的。需要使用到大量的内存空间来使用的。这样的话redis的使用量是比较的大的
这样查询的时候直接从redis中获取就可以了。至于说前一天的总的记录的话,直接使用离线分析就可以得到批处理的结果的。
这样的话，流和批的处理结果得到了空前一致的处理结果的。
这样的话,首先是从redis中获取数据的，然后查询数据库的数据的。数据库的数据也是需要更新数据的
3.实时计算的结果需要保存到mysql中的,防止redis失效的话,数据可以持久化保存到mysql中的。
4.流式处理数据的话，数据的容错处理操作实现的。checkpoint机制需要保全的。
5.kafka的exactly-once的消费操作记录和实现机制,这个是需要考虑和维护的。
使用分区的手动提交可以保证exactly-once机制实现操作的。
将kafka的offset保存到redis中可以实现相关的操作的

sparkStreaming 实时统计新增用户的指标其底层的实现逻辑和实时计算uv指标数据的计算逻辑是一样的
其核心也是使用的是sadd操作的。其他的实现的机制和上面的操作逻辑基本上是类似的操作逻辑的。

可以将spark打包完成的任务打包成为一个可以执行的jar文件的,后续将jar文件使用spark submit的方式提交出去的
完全可以使用如下的方式来实现相关的打包操作实现的。
YOUR_SPARK_HOME/bin/spark-submit \
  --class "SimpleApp" \
  --master local[4] \
  target/scala-2.12/simple-project_2.12-1.0.jar
这种方式一般的是生产中使用的方式的。将对应的jar文件提交到对应的目录下面执行操作。
rdd的底层是可以执行对应的交集,差集等的操作实现的。
spark使用本地文件的话，对应的是需要将本地的文件拷贝到对应的worker节点上去的。否则是不行的
spark所有的input操作,对应的是支持目录,压缩文件,以及对应的通配符等的适配符号。("/my/directory"),("/my/directory/*.txt"),textFile("/my/directory/*.gz")
默认的情况下,spark的textFile会根据每一个文件块来生成一个分区的,对应的表现是一个rdd的数据的，对应的大小是128M的大小的,可以配置更多的分区大小不用，但是不能比文件
块对应的分区的数量少的，传递的rdd的分区的数量要求是不小于对应的文件的分块数量的。
SequenceFiles  wholeTextFiles对应的是使用案例需要进一步的加强的。
spark的变量共享机制
1.broadcast variables 广播变量，可以实现在多个task之间进行共享操作；
2.accumulators 只能进行累加求和操作实现
Spark 3.2.1  默认使用的是scala的2.12的版本的

spark3.2.1的版本默认的是需要如下的：
1.spark_core的版本
groupId = org.apache.spark
artifactId = spark-core_2.12
version = 3.2.1
2.hadoop的版本，spark默认的是需要hdfs的支持的。
groupId = org.apache.hadoop
artifactId = hadoop-client
version = <your-hdfs-version>

-----实现相关的基类操作实现
context.newAPIHadoopRDD() 可以自定义其他非常规的格式文件的输入和读写操作的。所有的操作都是基于基类NewHadoopRDD
来实现相关的操作实现的。
spark中的相关的操作对应的还是基于底层的hadoop的操作实现的，底层操作实际上任然对应的
是hadoop相关的操作依赖的，所以，使用spark的时候少不了相关的hadoop的client的引入的。


需要注意的是如下的情况：
1.对于共享变量的修改推荐使用累加器实现操作的；
2.当使用println的使用，对应的执行的是在worker端的，所以，每一个打印的输出结果是不一样的。
这个在集群模式下面和local模式下面是不一样的，需要进行关注的。
如果需要打印元素的话，可以使用collect将元素聚集到driver上面去的，这样的话，可以完成对应的driver端的
元素的打印操作输出的。但是这样的话，会导致所有的数据都聚集到了driver上面的，会导致内存溢出的。
所有，如果想要打印少量的元素的话，推荐使用take来获取部分元素进行输出打印操作的。
3.spark的shuffle操作实现，分组或者是根据元素进行聚合操作实现。
4.需要主要的是使用reduceByKey的话，对应的需要重写对应的equals以及hashcode方法的,才可以使用不出现错误的
自定义对象需要重写这两个方法的，是很重要的操作的。重写对象的相等。
5.shuffle操作对应的是在executors之间重新分布数据的操作,是一个昂贵的并且成本极高的操作的。
为了解决shuffle的问题。spark单独为了shuffle操作提供了map操作以及reduce操作实现。
map的task用于组织数据，reduce的task用于聚合数据。在shuffle过程中会很多的临时文件的，防止出现错误，这样的话
对应的小文件就不用重新创建了。
spark的默认的cache的级别对应的是MEMORY_ONLY,是需要进行修改操作的。对应的配置成为MEMORY_AND_DISK_2是一个比较好的方案的
对应的spark的shuffle操作过程中也是会适用到cache或者是persistent的相关的操作的，
但是还是建议手动的调用相关的persistent来实现的。
spark默认的情况下是基于lru算法来清楚cache的数据的，可以使用unpersist()来手动的清楚cache的数据的
默认情况下清除操作不是阻塞的，如果需要阻塞操作的话，需要指定参数的。blocking=true 设置参数的
执行的时候，spark会将执行需要的参数独立的副本到worker节点上的，并且对应的更新操作不会更新到driver上去的。
spark的共享变量的使用场景如下：
1.broadcast variables and accumulators. 广播变量和累加器
accumulators对应的累加器的操作对应的是在action操作的时候才会执行的,只有在action操作的时候
对应的才会出现相关的累加器的操作执行的。在执行转换算子的时候是不会参与计算累加器的数值的
累加器只有在driver中才是可以获取数值的，worker节点上面是无法获取得到对应的累加器的数值的。
spark中是不允许在一个任务中执行两个不同的SparkContext的,对应的context是只能存在一个的。
2.spark提交任务的方式:
1)spark-submit方式提交任务；
2）


spark sql处理的是结构化的数据的。
Datasets ：对应的是无结构话约束的数据的
DataFrames：有点类似于关系型数据库中的关系表结构，包含了相关的列结构信息。

需要更多的关注orc/parquet文件的option选项的，里面很多的选项是很有意义的。
orc.bloom.filter.columns=name
orc.dictionary.key.threshold=1.0
orc.column.encoding.direct=name
parquet.bloom.filter.enabled#favorite_color=true
parquet.bloom.filter.expected.ndv#favorite_color=1000000
parquet.enable.dictionary=true
parquet.page.write-checksum.enabled=false
spark.sql.sources.partitionColumnTypeInference.enabled=true 是否启用分区键自动推断,默认是启用的。不启用的话,可以设置为false。此时string类型的是
作为分区键的类型的。默认是只会发现指定路径下面的分区信息，spark会自动的抽取分区信息。抽取分区路径信息会根据指定的路径实现操作的
根据路径抽取相关的分区信息。


需要重点关注的是
1.partition
2.bucket
3.sortBy操作
4.table的使用和用法。
各种格式的基于文件的sql操作实现还没有看到特别明显的操作实现的。对于整体的sql实现需要进行关注和
支持理解操作。spark sql的充分的使用还需要进行关注实现的。
5.模式合并的操作实现如下:
spark.sql.parquet.mergeSchema=true或者是mergeSchema=true
可以将单个文件夹下面的多个不同的schema进行智能的合并操作实现的
6.spark.sql.hive.convertMetastoreParquet=true 默认使用的是spark的parquet的支持的,
需要注意的是spark的schema以及hive的schema的话,我们是需要执行归一化的操作处理的。最为简单的方式是将
spark的schema约束和hive的schema保持一致的。这种不一致的schema会导致后续很多的莫名其妙的异常情况的。
在配置层面不是很好解决的，最好是数据类型保持一致,这样的话,对应的就可以完成相关的问题的。
还存在一个问题就是当hive的schema刷新的时候,对应的spark的schema是没有刷新的,这个时候需要设置如下的选线的
# 对应的可以刷新相关的hive的schema的约束的。
spark.catalog.refreshTable("my_table")
分区表中数据是存在到不同的目录下面的。Text/CSV/JSON/ORC/Parquet都是可以自动的发现分区信息的。
spark可以自动的从路径中解析出来分区键的信息。
hdfs://spark1:9000/spark-study/users/gender=male/country=US/users.parquet
上面的路径,对应的会将gender以及country作为分区键的。前提是没有禁用分区自动识别功能。
hive的Parquet以及spark的parquet对应的是存在一些问题的，需要做相关的转换操作的。数据类型方面的要求是特别关键的。
默认情况下，hive的parquet格式到spark的parquet的格式转换是默认支持的。对应的配置参数是如下的:
spark.sql.hive.convertMetastoreParquet=true的，在spark使用cache的时候，这种转换信息也是存在的。
需要手动的刷新相关的这种schame的转换信息的。对应的配置参数如下:spark.catalog.refreshTable("my_table")
避免hive的schema发生变化导致的schema的信息不同步的问题。

7.spark的列加密技术。针对的是parquet的列的加解密操作实现的。
spark.sparkContext.hadoopConfiguration.set("parquet.encryption.kms.client.class" ,
      "org.apache.parquet.crypto.keytools.mocks.InMemoryKMS")
    // Explicit master keys (base64 encoded) - required only for mock InMemoryKMS
    spark.sparkContext.hadoopConfiguration.set("parquet.encryption.key.list" ,
      "keyA:AAECAwQFBgcICQoLDA0ODw== ,  keyB:AAECAAECAAECAAECAAECAA==")
    // Activate Parquet encryption, driven by Hadoop properties
    spark.sparkContext.hadoopConfiguration.set("parquet.crypto.factory.class" ,
      "org.apache.parquet.crypto.keytools.PropertiesDrivenCryptoFactory")
    // Write encrypted dataframe files.
    // Column "square" will be protected with master key "keyA".
    // Parquet file footers will be protected with master key "keyB"
    nameDs.write.
      option("parquet.encryption.column.keys" , "keyA:square").
      option("parquet.encryption.footer.key" , "keyB").
      parquet("/path/to/table.parquet.encrypted")
    // Read encrypted dataframe files
    val df2 = spark.read.parquet("/path/to/table.parquet.encrypted")
8.parquet的option包含了如下的选项的：
spark 相关的parquet的option的设置是通过
DataFrameReader
DataFrameWriter
DataStreamReader
DataStreamWriter
这些参数设置的。
datetimeRebaseMode=spark.sql.parquet.datetimeRebaseModeInRead 仅用于read
int96RebaseMode=spark.sql.parquet.int96RebaseModeInRead 仅使用于read
mergeSchema=spark.sql.parquet.mergeSchema 适用于read
compression=snappy是默认的选项，spark.sql.parquet.compression.codec. 使用这个参数可以设置
设置的格式如下:none, uncompressed, snappy, gzip, lzo, brotli, lz4, and zstd
9.spark的parquet的设置参数可以如下的：
spark.sql("set spark.sql.parquet.binaryAsString=false")
spark.sql("set spark.sql.parquet.int96AsTimestamp=false")
spark.sql("set spark.sql.parquet.compression.codec=snappy")
spark.sql("set spark.sql.parquet.filterPushdown=true")
spark.sql("set spark.sql.hive.convertMetastoreParquet=true")
spark.sql("set spark.sql.parquet.mergeSchema=true")
spark.sql("set spark.sql.parquet.writeLegacyFormat=true")
spark.sql("set spark.sql.parquet.datetimeRebaseModeInRead=EXCEPTION")
spark.sql("set spark.sql.parquet.datetimeRebaseModeInWrite=EXCEPTION")
spark.sql("set spark.sql.parquet.int96RebaseModeInRead=EXCEPTION")
spark.sql("set spark.sql.parquet.int96RebaseModeInWrite=EXCEPTION")
spark.sql("set spark.sql.parquet.int96RebaseModeInWrite=EXCEPTION")
10.orc相关的参数是如下的:
orc的实现，是存在两个的。一个是hive的orc实现的，一个是spark的orc实现的
对应的orc的实现其本质的底层的区别在于底层的序列化方式的不一致的。当从hive的
orc实现转移到spark的orc实现上面去的话,对应的是需要这种转化的细微的差异的
这种序列化的差异体现在如下的地方的，比如:
CHAR/VARCHAR 类型的话，spark的native方式会解析成为String,对应的spark的hive模式
会解析成为CHAR/VARCHAR的类型的。
parquet也是存在如下的特性的。
配置参数是如下的：spark.sql.orc.impl 具体的参考SQLCONF的实现类的。
spark.sql.orc.impl=native
spark.sql.orc.enableVectorizedReader=true
spark.sql.orc.enableNestedColumnVectorizedReader=true可以嵌套的读取复杂的数据类型
比如，array, map and struct这些类型。
spark默认使用的是native模式的，对应的建表语句使用如下的：USING ORC
如果想要使用hive支持的序列化模式的话,使用hive option fileFormat 'ORC'进行数据的限制操作
spark.sql.hive.convertMetastoreOrc=true
orc格式的mergeschema约束配置如下:
mergeSchema=true
spark.sql.orc.mergeSchema=true
11.json格式:
json默认的是单行的数据的格式的，如果json的数据跨越了多行的话，需要设置多行模式支持的。
json相关的参数设置如下:
spark.sql.session.timeZone  推断时区
primitivesAsString=true  是否将基础类型的数据全部推断为string
12.text文件
text文件的话，每一行对应的都是一个row数据的，对应的列名称是value的














