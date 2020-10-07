1.kudu更多的使用的情况是和spark进行整合操作的。所以kudu需要和spark进行整合操作实现的。
可以将kudu的操作融入到spark中进行操作的。
大数据中删除表的话，都是可以回溯的。
2.
sparksql最重要的数据结构是dataframe的数据结构的.dataFrame和kudu的结合进行操作实现的。
dataframe操作kudu的优势：

rdd的分区和kudu的tablet的对应关系:
kuduclient读取数据的时候只能读取leader的数据的，写数据的时候对应的也是书写leader的数据的。
kudu的默认情况下，数据的读写只能发生在leader上面的。
spark的读取操作可以发生在follower上面的。对应的读取的时候并发的性能会更加的好的。
spark的分区机制和kudu的tablet的机制的优势的？
使用spark操作kudu进行kudu的操作实现的。kudu和impala来自于同样的一个公司的。来自于cloudera的产品的。
impala的使用场景：impala并不是编写代码来实现jdbc来进行访问的，不是直接书写代码访问的。
impala主要是提供数据分析人员来进行分析和查询操作实现的。
impala和hive的区别操作实现：impala的速度相较于hive而言，速度快得多的。hive是使用mapreduce以及yarn进行调度的
impala有自己的执行计划，impala对应的是mmp系统，对应的是大规模运行时查询的系统的。会将节点分开操作的。
每一个节点对应的自己的存储的服务器的。
impala的mmp架构可以快速的处理大量的数据的。可以显著的提高运行速度的。
hive使用mr，可以使用用于大规模的数据处理的，mr在大量数据的处理上面缺失存在很显著的优势的。
impala的架构决定了不适合长时间处理大量的数据的。
impala和kudu整合：目标是通过hive表进行操作的。impala是强依赖于hive的。

################对应的创建kudu的表数据结构
使用impala创建kudu的表:
create external table  student stored as kudu
tblproperties('kudu.table_name'='student','kudu.master_addresses'='cdh1:7051,cdh2:7051,cdh3:7051');
Query: create external table  student stored as kudu
tblproperties('kudu.table_name'='student','kudu.master_addresses'='cdh1:7051,cdh2:7051,cdh3:7051')
Fetched 0 row(s) in 4.27s


DMP:本质上是一个标签系统。
受众存在大量的标签的。给受众标注标签的话。可以实现准确的标记操作的。

广告网络服务商：将小媒体聚集成为一个整体，对外提供服务。
广告交易平台：在广告交易的基础上形成的广告交易平台的。
RTB:实时交易系统。倾向于媒体的服务的。
DSP：帮助广告主筛选用户。
DMP：数据管理平台。负责收集用户数据和给用户数据打上标签的。DMP根据标签查询用户的数据的。
DMP获取数据：
1.平台自身就可以收集到用户的数据的。比如百度，阿里等;
2.部分的数据开放给DMP平台的，DMP可以获取到部分的数据的;
3.黑产方式获取数据:通过链接获取到相关的数据的。
DMP的作用：1.收集用户数据；2.给用户打上相关的标签的。3.运营辅助操作(运营数字化操作实现).
zeppelin:数据可视化工具。




