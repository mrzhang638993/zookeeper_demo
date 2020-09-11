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

一般的处理数据的过程对应的都是ETL的过程的。ETL代表的是extract(抽取),T(处理和转换操作),L(装载和落地)。




