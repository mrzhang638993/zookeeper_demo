# spark的原理部分的理解和操作实现


怎么说明一个集群是spark集群的：
判断一个集群是spark集群：spark程序运行在集群中的。通过集成中的进程以及相关的程序的理解和操作的。
1.集群所以被称之为集群，对应的是因为集群中包含多个角色和进程，能够执行有组织的操作执行任务的。
spark集群包含如下的角色：
1.master  demo: 负责master节点的操作;
2.worker demo: 负责worker节点的任务的调度执行,master demo调度执行worker demo进程执行任务；
3.executor  backend：负责executor的执行的。对应的执行的是内部的task的，worker demo调度执行executor  backend。
一个executor backend 对应的只存在一个executor的。
4.driver：spark应用程序驱动节点。运行一个spark application对应的是由driver生成的。driver负责创建spark application应用启动的上下文
获取最终的executor的执行结果，最终的action执行操作对应的最终的结果存在于driver中的。

问题：确认一下是否是只有一个executor实例的，还是存在多个executor的？

1.逻辑执行流程图的讲解说明：rdd的链条
逻辑执行图对应的表示的是rdd的执行流转图。表示的是rdd是如何生成的，以及RDD之间的关联关系。
逻辑执行图描述的是数据如何流转的，如何计算的过程。
逻辑之心图并不是真实存在的，代表的是RDD的一个逻辑的执行流程图的。
逻辑执行图表达的是数据如何计算的。
2.物理执行图:描述的是rdd如何放置到集群中运行的。物理执行图表示的数据如何在集群中执行的

HadoopRDD:其中compute方法对应的是RDD的方法的，HadoopRDD对应的overide相关的方法。改变了RDD对象的计算方式。
HadoopRDD的patitions对应的是HDFS的blocks数据。


逻辑执行图的边界：从第一个RDD开始，逻辑图遇到action算子之前结束。逻辑图表达的是一组逻辑关系以及对应的依赖关系
RDD的5大属性：
1.分区列表；
2.依赖关系；
3.计算函数；
4.最佳位置（可选的）
5.分区函数(可选的)
HADOOPRDD重写了分区列表和对应的compute函数。重写分区列表对应的实现了RDD对应了HDFS的block数据块。重写计算函数从而可以读取分片数据进行计算。
逻辑图是数据处理和存储的过程。逻辑图中必须要有RDD的生成和依赖关系。

RDD之间是没有关系的。有关系的是RDD的分区符数据的。从一个RDD的分区到另外的一个RDD的分区的数据的。
RDD之间的依赖关系不是指的是RDD之间的依赖关系的，而是RDD的分区之间的依赖关系的。体现的是RDD对应的分区之间的对应的关系的。
1.一对一的关系:map以及flatmap对应的是一对一的关系的。
2.多对一的关系:reduceByKey对应的可以实现多对一的关系的。
/**
spark给RDD划分关系的目标：查看RDD的分区是否可以放置在同一个流水线上执行的。
这个是划分RDD的依赖关系的根本。取决于这两个RDD是否是shuffle关系。
如果是shuffle关系的话，是无法在一个流水线上运行的。否则的话是可以在一个流水线上运行的
*/
RDD的关系是窄依赖还是宽依赖：
shuffle操作的判断：需要对数据进行分区操作的。查看RDD算子是宽依赖还是窄依赖只需要查看相关的RDD对应的返回的dependencies的数据的。
1.cartesian算子对应的返回的是窄依赖的：cartesian NarrowDependency
2.reduceByKey对应的返回的是宽依赖的，对应的返回的是宽依赖的。ShuffledRDD ShuffleDependency依赖数据的。
/**
判断宽窄依赖的核心对应的是否存在shuffle操作的。shuffle操作对应的是一个数据的分发的过程的。宽窄依赖的分辨是根据shuffle操作的。
shuffle的特点是数据拆分给不同的分区的。数据全部给下面的一个rdd的话是债依赖的，部分的给与的话是窄依赖的
*/
RDD默认的是1对1的窄依赖的。多对一的话，需要查看是否存在数据分发的操作的，存在的话为宽依赖的，否则为窄依赖的。最准确的判断是查看源码。
如果子RDD和父RDD之间的关系是如下的：
1.1对1的关系对应的是窄依赖的；
2.多对一的话，对应的是宽依赖的。
多对一的处理关系和一对一的处理关系：

窄依赖的类别：
1.



