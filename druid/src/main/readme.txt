druid:olAp对应的的时一个大数据分析的数据库的。
hbase:列式数据库。
mysql,hbase,redis等都是可以特色的操作的。
druib：针对的时时间序列的，低延迟的，快速交互式的查询操作的。可以满足实时的查询和实时的数据的插入操作实现的。
es:在大量的数据的查询操作的时候性能时特别的优异的，但是在大量数据的操作的情况下是会消耗很多的时间的。
大数据的情况下，对于数据的查询操作时很多的，基本上时不涉及删除和修改操作的，操作的重点时分析操作的。
oltp:注重的时对于数据的增删改的操作的，对于查询的操作以及分析操作是很少的。
hive/sparksql：注重离线分析操作的，数据的实时性的很差的。离线分析延时性比较的高的，很难满足毫秒级甚至秒级的要求的。hive/sparksql适用于离线分析操作实现的。hive底层对应的时maprecuce的操作的
sparksql在hive的基础上进行了优化，但是性能还是存在很大的问题的。
es:在时效性和查询延时的效果时很好的，数据量在中等规模的时候性能时比较的优秀的，但是在万亿甚至更大的数据级别的情况下，性能存在很大的问题的。
kylin以及druib等的操作的话，druib支持数据的实时导入操作的。统计分析的时候需要得到实时的数据结果的，数据的实时插入的话，对应的存在可以使用druid的分布式数据库的。

#数据新增
curl -X 'POST' -H 'Content-Type:application/json' -d @quickstart/wikiticker-index.json hp101:8090/druid/indexer/v1/task
#数据查询操作
curl -X 'POST'  -H'Content-Type: application/json'  -d @quickstart/wikiticker-top-pages.json http://hp101:8082/druid/v2/?pretty


