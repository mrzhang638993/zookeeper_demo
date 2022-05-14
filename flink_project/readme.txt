#flink相关知识
对应的每一个flink应用的话,都是需要一个执行环境的。
flink对应的可以理解为两层意思的：
1)静态代码的，flink对应的会将程序代码编译成为job graph的代码的
2)运行时:对应的静态的job graph会被编译成为真正能够运行的execution graph的执行图的。
flink的stream程序运行的话，如果不调用对应的execute的,对应的flink程序是不会启动的。
flink其实也是有一个中心化的思想，flink运行是的架构是一个主从的架构的，主节点负责资源调度以及对应的协调checkpoint执行操作实现
还有需要注意的是，flink底层使用DataStream完成替代DataSet的操作的,后续的所有的操作使用DataStream就可以完成了，不需要相关的DataSet的架构的
#flink的使用场景
such as Apache Kafka, Kinesis, and various filesystems. REST APIs and databases are also frequently used for stream enrichment.
在flink中使用对应的rest的方式需要关注一下的。在flink中调用相关的代码需要进行关注的。这个可以理解一下，
flink主要的分布式操作是生成对应的job graph的。flink的分布式的前提是对应的operator算子的，这个是flink实现分布式调度的核心观点的。
所以，如果不在operator中使用rest操作的话，对应的是不会执行相关的分布式操作的。这个核心是需要领会的。
#生产中常见使用的sink如下：
commonly used sinks include the StreamingFileSink, various databases, and several pub-sub systems.
主要包括文件sink,数据库的sink操作，各种发布订阅系统。




