# 通过练习对应的体现出来相关的spark的rdd的数据作用的。

# 下面简单说明一下RDD的shuffle和分区操作：分区的增加和减少以及重分区操作
RDD经常需要读取外部的数据创建RDD操作，外部存储的RDD往往对应的都是支持分片操作的。分片侧重于存储，分区侧重于计算。
所以RDD需要支持分区和外部系统一一对应的，RDD的分区是一个并行计算的手段的。
分区和shuffle的关系：只有key-value类型的数据才可以执行shuffle操作的，原因在于收集数据需要根据key进行分组操作。
指定rdd的分区：1.在创建rdd的时候指定；
# RDD的缓存
# RDD的checkpoint操作

shuffle过程涉及到的概念
1.reducer的数据一般的是通过hdfs上面的文件的方式实现数据的获取操作的。
shuffle处理的方式：
1.hash的shuffle方式；描述的是如何存储文件和获取文件信息。hash  base  shuffle
存在的问题，每一个mapper对应的针对于reducer而言需要产生多个reducer对用的文件的。这样会导致长生大量的文件的。极大的占用内存资源的。
产生的中间文件过多的。占用很多的资源的。
2.sort类型的shuffle方式； sort hash shuffle
使用appendOnlyMap对应的shuffle方式的，可以减少中间文件的大量生成的。

shuffle执行的操作是需要在多个计算机之间进行数据拷贝操作的。存在大量的io的
写代码的境界和操作实现:
1.只是直到自己在写代码；
2.直到代码在做什么的；

aggregate,aggregateByKey,treeAggregate等在一个算子中执行聚合然后在整个的算子中执行聚合操作的
使用的时候需要重点注意一下的
1.涉及到RDD单个分区的操作的，还涉及到RDD的多个分区的操作的。
分区数的改变以及数据量的改变对应的都会导致结果的最终变化的。
2.针对于这些问题，可以将seqOp以及combOp设置成为一样的算法的，
还有就是特别的注意这种设计到分区内部以及分区shuffle的操作的函数的。对结果的影响很大的。





