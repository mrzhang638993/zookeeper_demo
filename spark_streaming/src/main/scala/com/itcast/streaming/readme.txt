spark  streaming 的编程模型对应的是Dstream的。是一个流式的数据的。
spark的编程模型对应的是rdd的。

spark  streaming线程至少需要两个线程：
1.接收server端的数据
2.执行执行rdd的操作


spark  streaming是一个小批量的处理引擎的。
小批量的划分依据：
1.Spark Streaming 并不是实时流, 而是按照时间切分小批量, 一个一个的小批量处理

DStream对应的是针对于RDD进行处理的。每一个RDD称之为一个批次的数据的。
DStream中流动的是RDD的，对于DStream中的任何操作最终作用的对象是RDD的.最终的操作是交给rdd进行操作的


长时间运行的系统如何保证系统的容错性能？
总结下来, 有四个问题
DStream 如何对应 RDD?
如何切分 RDD?
如何读取数据?
如何容错?spark streaming需要长时间运行的，如何保证系统的高可用性的。


有向无环图：数据处理是分步骤的。是不断地往前流转的。数据的处理是有方向的，不能首尾相连接的。
sparkStreaming处理数据对应的是底层的Dstream的不断的流动的。Dstream的内部是RDD的数据的。
DStream内部对应的是RDD组成的。DStream也是可以组织成为有向无环图的。DStream经过转换操作对应的生成的是新的DStream的数据的。
DStream的有向无环图最终的执行是RDD的有向无环图执行的。
DStreamGraph对应的是DStream最为关键的流程图的操作实现的。体现的是随着时间的推进的之间的DStream的之间的关联关系的。
DStream之间也是存在依赖关系的，依赖关系之间通过DStreamGraph进行体现的。DStream底层的操作的核心是RDD的操作的。
一个Dstream对应的包含了多个RDD的操作的。

一个优秀的框架底层必然包了优秀的数据结构的。


获取数据的操作实现：
kafka对应的是分片的。spark streaming处理的时候对应的也是需要进行分片操作的。
计算系统以及大数据系统中的话，数据容错性的保证是很关键的。保证端到端的唯一性是很关键的。数据的容错性保证数据的容错性很关键的。

数据容错性的操作：
1.热备：数据存在冗余备份的。
2.冷备操作:预写日志之前执行操作实现。WAL操作实现。先写日志然后执行实际的操作。对应的称之为redo的日志的。
3.重放:可以从kafka的offset重新读取数据的。借助外部数据源的功能实现。


RDD的优点和缺陷：2011。执行的速度比较的慢的。
1.RDD面向对象进行操作，无法感知数据结构是什么的？无法针对数据结构进行操作的。操作的时候只能针对于黑盒子进行操作的；

DATAFrame:2013年---->优化器，dataframe的执行效率比较rdd的效率更加的。存储的是列式的数据的
只是支持结构化的数据的。
保留了数据的原信息，api可以对于数据结构进行操作的。
缺点：任何的DATAFrame取出来的是结构化的数据的，是弱类型的数据的。
优点：可以读多种格式的结构数据的。只能处理结构化的数据的

DATASET：2015年，代替DATAFRAME的。
DATASET具有dataframe的所有的api，不只能够读取row类型的api，还可以读取的是有类型的对象的api的数据的。
优点：可以处理结构化的数据以及非结构化的数据的。



在大量数据进行处理操作的时候，需要考虑到序列化和方序列化的执行的效率的。


序列化操作：将对象转化成为二进制的字节码
反序列号操作:将二进制字节码转化成为对象的过程称之为反序列化。
java的序列化操作包含了很多的环境信息，对象中的属性字段等。是比较浪费存储空间的。
网络中传输的是二进制数据的。将对象的数据缓存到文件中也是需要序列化的。


rdd算子使用外部的对象的话，是需要进行序列化操作的。
序列化的多种场景信息：
1.task分发
2.RDD缓存，rdd.cache
3.广播变量执行操作:
4.shuffle过程：mapper中拉取数据，需要设计到数据的传输操作的。
5.spark streaming的receivor，需要缓存kafka的数据
6.算子引入外部的对象


DataFrame以及Dataset对应的底层存储的是RDD[internalRow]存储的。

RDD的序列化只能使用java序列化操作或者是Kryo的序列化操作的。通常会使用Kryo的序列化操作的。
val conf = new SparkConf()
  .setMaster("local[2]")
  .setAppName("KyroTest")
conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
conf.registerKryoClasses(Array(classOf[Person]))
val sc = new SparkContext(conf)
rdd.map(arr => Person(arr(0), arr(1), arr(2)))

序列化的优化点：
1.源信息和数据独立存储。
2.不在直接使用堆内内存存储数据的，不推荐一般的程序员使用堆外内存的。降低GC的开销的。
Dataset以及DataFrame使用的是独立的序列化器的，使用的是堆外的内存实现操作的。效率是使用堆内内存的20倍以上的。
不在使用java的序列化器以及Kryo的序列化器的。


后期的代码操作不需要使用spark的rdd操作了，所有的操作都是基于spark sql的基础之上执行操作的。
所有的操作都是基于sparksql执行操作的。
spark  streaming是基于spark core的，是基于rdd的。后续的代码编写不需要使用这些代码的
structure streaming 后续的相关的代码的编写对应的需要采用的是这个的，是基于spark sql基础构建的。
另外还有一点, Structured Streaming 已经支持了连续流模型, 也就是类似于 Flink 那样的实时流, 而不是小批量, 但在使用的时候仍然有限制, 大部分情况还是应该采用小批量模式


