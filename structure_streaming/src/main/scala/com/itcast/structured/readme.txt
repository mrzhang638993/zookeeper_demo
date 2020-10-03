Dataset和流式计算：

sparksql 支持批式处理和流式处理的操作。
1个dataset在逻辑上表示的是：表示的是表。dataset表达的是关系型数据库的表的。
dataset可以看做是无限扩展的，无限追加新的数据的表的话，
dataset就可以在语义的层面上执行流式逻辑数据的处理的。
read 对应的创建的是不可扩展的表的。
readStream对应的创建的是无限可以扩展的表的。
所以在流式处理和批处理的时候完全可以使用dataset的同样的一套的api来实现的。
StreamExecution:主体操作的实现类实现操作管理实现。
使用stateStore保存增量数据。
structured streaming是在dataset的基础上执行的操作的。

python小工具：python当做shell来执行的。生成大量的小文件的。生成本地的小文件是很好使用的。
大数据中对应的还是可以使用python实现操作的。python对于创建操作还是存在很友好的意义的。


kafka对应的是消息引擎或者消息系统。kafka可以支撑千万级别的消息的。kafka中真正存储消息的是分片的。
日志是一种数据格式的，是连续的。一个partition之间是有顺序的，多个partition之间是有顺序的。
一个topic可以理解为数据库中的一个表格的。topic是可以分区的，相当于mysql中的一个数据库表的。
kafka中消息的标识不是id，而是offset的。offset的偏移量的。offset强调的是消息只能追加不能插入数据的。
// Topic Partitions
structured streaming：虽然模拟了一张无限扩展的表的，但是对应的底层的还是对应的是增量处理的。
性能调优的操作：
1.无限扩展的表格：

需求：设计一个智能物联网的数据统计和设计
kafka的消息对应的是一个key-value的形式的数据集的操作的。
structured streaming 官方只提供了数据落地到kafka以及hdfs中的，
需要落地到其他的地方需要使用foreach  writer操作实现的。
structured streaming 设置时间使用的api对应的是Trigger进行操作的。


微批次处理以及连续流处理操作:
1.微批次处理操作:对应的处理的是一批次的数据的。structured  streaming可以做到1秒的级别的
2.spark可以执行到微批次的数据的。可以做到准实时的操作的。flink可以实现到了实时的处理的。
spark的2.3版本提供了连续流处理操作的api。

容错语义：
1.at most once：消息最多发送一条，消息不会重复消费的。
2.at least once：数据至少被处理一次。
3.exactly once：数据只有一条数据的。






