加载海量的数据到hbase中。
直接和hdfs文件进行交互


# bulkload方式批量加载数据可以使用如下的方式操作：completebulkload,/hbase/hfile_out,myuser2 对应的为参数信息
yarn jar /export/servers/hbase-2.0.0/lib/hbase-server-1.2.0-cdh5.14.0.jar completebulkload /hbase/hfile_out myuser2