# sparksql中可以使用schema对应的操作对应的对应的字段的。dataset包含了schema信息的。
RDD处理的时候对应的不知道是什么内容的。

spark sql最精华的一部分是Catalyst 优化器的内容的
spark-shell的代码操作:
1.不要出现返回值的类型；
2.对应的是上下文为spark的，是默认的。

dataset是支持优化的。

spark是一个比较成熟的工具的。可以在一段代码中使用了rdd,又可以使用dataset,还可以使用SQL语句执行操作的。
spark对应的是一个比较弹性的工具的。
dataset.rdd 可以将dataset转化为rdd操作的。

Dataset 是一个强类型, 并且类型安全的数据容器, 并且提供了结构化查询 API 和类似 RDD 一样的命令式 API。类似于java中的list等的。
DataFrame类似于关系型数据库的一张表，在dataFrame上的操作非常类似于sql语句。dataFrame其中包含了行和列的数据的，并且列具有schema的约束信息的。
dataFrame有行和列，还有schema的结构信息。
###########
一般的处理数据的过程对应的都是ETL的过程的。ETL代表的是extract(抽取),T(处理和转换操作),L(装载和落地)。
###########
DataFrame对应的表达的是二维元祖的表，里面无论是任何的对象的类型的，
dataframe存放的是对象的row对象的。存放的是row对象的，对应的是对象转换成为的row对象的。dataFrame操作的是row对象的。
dataframe代表的弱类型的操作的话只有在运行时才可以检查的。
############
DataSet代表的是一个强类型的容器的。存放的是对象类型的。dataset是直接操作对象的。
dateset对应的可以在运行时和编译时都是可以检查的。dateset对应的是编译时安全的，会存在类型检查的。
############
spark中的row对象需要配合schema才可以取出来相关的数据的


############
spark流式计算框架:
1.spark streaming:旧有的框架的，使用的是较多的。
2.spark structure：最新的框架的。
流计算的应用场景：比如性能指标的监控的。
1.对于数据的快速处理要求比较高的;
2.计算的数据量比较的高的。

############
spark的读写框架:

############
parquet的使用场景信息：
1.数据抽取到hdfs中的时候，可以使用parquet的数据格式进行保存的；
2.spark处理完成数据之后对应的数据存储的时候可以使用parquet的数据格式的；
############
hive分区表对应的可以根据某一个字段执行分区操作的，可以将数据方然到不同的文件夹中。
hive表对应的是分区表的话，
spark sql支持分区表的操作的原因：是因为spark需要和hive配合操作的，所以，spark需要支持hive的分区操作的。

sparksql的很多的操作底层对应的是column操作实现的。
数据清洗的主要的工作内容和实现的机制是什么？数据缺失值的处理是很关键的操作的，需要进行相关的逻辑操作处理实现的。
常见的几种缺失值操作：
1.NAN;
2.null:对象为空
3."NA":对应的是缺失值的操作的
4.“null”:缺失值
5.“”对应的是缺失值的操作的。
缺失值的产生存在着多种不同的类型和原因的。

缺失值的处理方法如下：
1.丢弃包含缺失值的行;
2.缺失值替换为初始值;

处理缺失值的框架：DataFrameNaFunctions



############
多个spark任务执行的时候，因为资源导致的等待时间过长的解决方案：
1.对spark sql执行的时候进行调优参数，参数如下：
2.集群扩容


spark的object class中对应的是不能存在@Test对象的
在类中需要使用到main对象的。
rdd以及dataset对应的执行的是离线分析操作实现的。








