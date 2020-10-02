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





