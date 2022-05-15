1.使用connect连接的两个dataStream的话,需要注意的是连接的两个DataStream对应的都是需要保持运行
状态的，当有一个不处于运行状态的话，整个的checkpoint都是无法运行的。
2.timestamp是flink sql中的关键字的，不要使用的。可以使用createTime完成相关的操作的。
定义字段需要避开sql中的关键字信息的。
#source的operator的描述
SourceFunction:对应的是一个非并行的source的operator算子的。
RichParallelSourceFunction:对应的是并行计算的算子的。
#下面是常见的定义的几个source的operator的算子的
1)readTextFile(path):读取text文件的source。
2)readFile(fileInputFormat, path, watchType, interval, pathFilter, typeInfo)可以实现更加
完善的读取文件功能的实现操作。
DataStreamSource<String> source = env.readFile(new TextInputFormat(new Path("")), "", FileProcessingMode.PROCESS_CONTINUOUSLY, -1);//对应的获取数据源
文件读取的操作是如下的:读取文件包含了2个子任务的。一个是目录监测任务，一个是数据读取任务的。
数据读取任务对应的将文件切割成为多个split，然后将对应的split交给下游的download Stream来读取数据的。然后数据读取任务真正的读取任务的
每一个对应的split都最终会交给单个的reader来读取数据的，一个reader可以读取多个的数据的。
需要注意的是FileProcessingMode.PROCESS_CONTINUOUSLY对应的会破坏exactly-once的语义的。文件追加的话，会导致数据被重新读取。
FileProcessingMode.PROCESS_ONCE 设置成为这个的时候，当文件关闭的时候，恢复数据就会比较漫长，因为需要从source读取数据来进行恢复操作。
socketTextStream:对应的从socket中读取数据的。
#基于集合的,从集合中读取数据
fromCollection:从java的collection中读取数据。
fromCollection(Iterator, Class):从迭代器中读取数据。
fromElements:从元素序列中读取数据。
fromParallelCollection(SplittableIterator, Class):从迭代器中并行读物数据。
generateSequence:生成序列,并行方式执行
addSource:自定义方式读物数据源

