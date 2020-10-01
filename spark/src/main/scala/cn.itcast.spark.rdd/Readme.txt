spark比较适合的是迭代式的开发的，适合的是离线分析的
实时分析需要使用到sparkstream，需要借助源mysql等完成实时分析操作
######执行spark相关的算子操作实现
map和mapreduce的算子是一致的。map算子传入的参数是单条数据，mapPartition传递处理的是整个分区的数据的。
map返回的是单条数据的，mapPartitions对应的是返回的是iter的数据的。
mapPartitionsWithIndex相对于mapPartitions多了一个参数是index索引数据的。对应的是原始分区的索引数据的。


需要理解mapreduce的处理逻辑对于spark的数据处理逻辑会更加的清晰。

map转换操作的种类：
1.转换：map,mapPartitions,mapValues
2.过滤: filter,sample
3.集合操作：intersection,union,subtract
4.聚合操作:reduceByKey,groupByKey,底层是combineByKey.foldByKey,aggregateByKey
sortBy 手动指定排序字段.sortByKey 根据key进行分区，数据是key-value类型的数据。
####
spark的rdd的转换操作对应的都是惰性操作的，在执行的时候并不会真正的去执行的。并不会得到结果的。而只是生成rdd的。
只有在action操作的话，才会执行真正的计算的。

spark的action操作
1.collect:集合聚集
2.reduce算子:reduceByKey 和reduce的区别
reduceByKey:对应的是一个转换算子。是一个shuffle操作的。本质上先进行分组的，然后对于每一组进行聚合操作。针对于key-value类型的数据进行计算
/**
reduce：是一个action操作。针对于每一个数据组进行聚合操作。针对所有类型的数据进行计算。
reduce操作不是一个shuffle操作的。reduce操作对应的是在分区上执行reduce操作。
不存在数据从map端转换到reduce端的。reduce操作不存在map和reduce阶段操作的。
*/
shuffle操作的核心是针对于分区进行数据的聚合操作的。shuffle操作分为两端的。从map阶段转化为reduce阶段的。

RDD存在5个属性的：
1.PARTITIONLIST :分片列表，创建RDD的时候可以指定分片的个数信息。如果没有的话，会采用默认的分片数的。
2.compute:对每一个分区进行计算的函数。对应的是rdd的计算操作的。
3.dependencies：记录RDD之间的依赖关系，实现容错和计算
4.Partitioner：执行shuffle操作，是为了兼容mapreduce的shuffle操作的
5.prefer Location：实现数据本地化的操作，体现的是转移数据不如转移计算的理念设计思想的。


action操作：
1.collect操作
2.reduce操作
3.foreach操作
4.countByKey和count
5.take和takeSample以及first操作。first的速度是相当快的。


RDD中存放的数据类型：
1.基本类型：String 以及对象类型
2.kv类型：根据key对value执行操作。
3.数字类型：求解均值和最大数等的。









