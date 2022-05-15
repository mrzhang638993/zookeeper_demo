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
#对应的sink operator的描述操作
writeAsText():元素以行结尾的方式写入到文件中。调用的是对象的toString的方法写入数据的。
writeAsCsv:对应的使用csv格式的输出操作的。
print() / printToErr() :结果输出到控制台或者是错误控制台
writeUsingOutputFormat:结果写入到自定义的输出格式中。
writeToSocket:结果写入到socket中。
addSink:使用自定义的sink操作实现。可以书写自定义的sink的实现的。现在提供了很多的自定义化的sink的实现的。
需要注意的是dataStream的write*相关的操作都不是exactly-once语义的实现的,数据的写入操作是依赖于OutputFormat的实现的，
对应的语义是at-least-once语义的实现的。异常情况下，会导致数据的丢失的。
写文件操作为了保证exactly-once的语义的话，需要使用StreamingFileSink进行文件的书写，或者是自定义实现相关的addSink语义实现的。来实现exactly-once的语义的。
#使用迭代stream来处理stream
下面是stream的迭代和关闭操作实现。
DataStream<Long> someIntegers = env.generateSequence(0, 1000);
IterativeStream<Long> iteration = someIntegers.iterate();
DataStream<Long> minusOne = iteration.map(new MapFunction<Long, Long>() {
  @Override
  public Long map(Long value) throws Exception {
    return value - 1 ;
  }
});
DataStream<Long> stillGreaterThanZero = minusOne.filter(new FilterFunction<Long>() {
  @Override
  public boolean filter(Long value) throws Exception {
    return (value > 0);
  }
});
//关闭迭代Stream的数据。过滤掉大于0的数据，剩下的小于0的数据继续参与到后面的数据处理中的。
iteration.closeWith(stillGreaterThanZero);
DataStream<Long> lessThanZero = minusOne.filter(new FilterFunction<Long>() {
  @Override
  public boolean filter(Long value) throws Exception {
    return (value <= 0);
  }
});
#flink的延时的控制,可以基于env或者是基于operator来实现延时的控制的
1)env.setBufferTimeout(timeoutMillis)：对应的基于env的延时控制操作。
2)基于operator的延时控制操作,env.generateSequence(1,10).map(new MyMapper()).setBufferTimeout(timeoutMillis);
基于算子的setBufferTimeout。默认的是100ms的。100ms之后的话，会自动的刷新缓冲区的。为了更小的延迟的话，可以将时间设置为
更小的数值，比如5 or 10 ms，但是禁止设置为0，会导致极为严重的性能下降操作。
setBufferTimeout(-1)会导致只有等缓冲区满了的话,才会进行刷新操作，可以保证最大的并发量的，但是延时比较的高的。
#flink项目在集群上运行之前，最好在本地执行一下，检测对应的性能和执行的效率的。
使用collection操作的话,对应的source的并行度是1的。collection data sources can not be executed in parallel ( parallelism = 1)

#配置使用stream还是batch模式的数据操作
-Dexecution.runtime-mode=BATCH
或者是 env.setRuntimeMode(RuntimeExecutionMode.BATCH); 这样的话，stream或者batch都是使用的同样的一套api
建议不要在程序中设置runtime-mode，建议在命令行运行的时候增加运行操作。-Dexecution.runtime-mode=BATCH
#任务调度和shuffle操作:
在batch模式或者是stream模式中，对应的任务调度和shuffle操作是不同的。这个因为在处理有界流的时候,可以使用更加高效的数据结构和算法。
在batch模式中,后面的数据完全可以等到前面的处理完成之后在处理的。在batch模式中，任务发生失败的话，只需要恢复部分的task的。在batch模式中,对应的state管理的话，会比较的简单的。在使用keyby的时候只需要记忆单个的key的state就可以了。
在stream模式中，数据是pipeline的。是需要立刻到达下游进行处理的。在stream中，数据是没有order顺序的。数据来了就会处理的。batch模式的话
在batch模式中，数据处理的order是按照这样的顺序的：
1)broadcast inputs are processed first.广播流的输入会先进行操作处理。
2)regular inputs are processed second:正常的输入会进行后续的处理操作实现。
3)keyed inputs are processed last:keyed的stream会进行后续的处理的。因为这个涉及到对应的shuffle操作。
多个广播流的操作，可以使用CoProcessFunction进行操作。多个keyed的输入,KeyedCoProcessFunction
stream下的数据是数据到了就会被处理的,所以不会考虑到对应的数据的顺序的。数据的顺序在这里面是没有什么太大的作用的。
在batch模式中，我们可以使用排序的方式来实现完美的水印机制的。
在批模式下面，下面的内容是不支持的:
1)checkpoint:任何基于checkpoint的操作符都不支持的。在batch模式下面,错误恢复是不需要使用到checkpoint的。
2)迭代操作:
需要注意的是因为在batch模式下面，对应的checkpoint机制是不起作用的。所以，相关的CheckpointListener以及kafka的exactly-once
,sink的OnCheckpointRollingPolicy对应的是不起作用的。这个时候要使用到精确的exactly-once的语义的话,需要的是其他的机制的。
#使用水印时间
使用水印时间的话，要求对应的数据记录存在有相关的时间戳信息的。TimestampAssigner对应的用于抽取记录中的时间信息。
WatermarkGenerator则主要用于生成相关的水印时间的。已经存在有大量的开箱即用的水印策略的。用户也可以使用自定义的策略的。
#其中TimestampAssignerSupplier用于抽取时间戳，WatermarkGeneratorSupplier用于生成水印时间。
public interface WatermarkStrategy<T>
    extends TimestampAssignerSupplier<T>,
            WatermarkGeneratorSupplier<T>{
    /**
     * Instantiates a {@link TimestampAssigner} for assigning timestamps according to this
     * strategy.
     */
    @Override
    TimestampAssigner<T> createTimestampAssigner(TimestampAssignerSupplier.Context context);
    /**
     * Instantiates a WatermarkGenerator that generates watermarks according to this strategy.
     */
    @Override
    WatermarkGenerator<T> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context);
}
水印对其存在于如下的场景中的：
1.不同source的数据；比如kafka以及file都产生相关的数据;
2.并行度和splits/shards/partitions是一样的，这个的话,也可以构成对应的多个数据的。
需要注意的是,水印的操作话，建议是直接在source上面的，不要在其他的算子上面操作的，官方建议水印是直接在source上面执行的。
kafka的per-Kafka-partition watermark对应的会为了每一个分区创建对应的watermark的。
AssignerWithPeriodicWatermarks以及对应的AssignerWithPunctuatedWatermarks将会被删除的。
WatermarkStrategy.forMonotonousTimestamps();用于数据内部是根据时间升序排列的。
WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10)); 固定延时时间的水印机制。

#flink的状态 stream 程序编程







