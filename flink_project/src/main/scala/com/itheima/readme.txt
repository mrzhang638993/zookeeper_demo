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
1)ValueState:
2)ListState:
3)ReducingState:
4)AggregatingState:
5)MapState:
需要注意的是state的相关的接口仅用于状态的操作的，还有状态值的获取与当前的元素是相关的。不同的元素在同样的一个函数中会获取到不同的结果的。
#需要注意的是，这些对应的都是keyed state的，对应的需要在keyed DataStream中使用的。
使用state需要有对应的StateDescriptor，对应的常见的StateDescriptor包含了如下的:
1)ValueStateDescriptor
2)ListStateDescriptor
3)AggregatingStateDescriptor
4)ReducingStateDescriptor
5)MapStateDescriptor
state的访问仅仅可以在RuntimeContext中使用的,所以，其定义只能在RichFunction中的。
ValueState<T> getState(ValueStateDescriptor<T>)
ReducingState<T> getReducingState(ReducingStateDescriptor<T>)
ListState<T> getListState(ListStateDescriptor<T>)
AggregatingState<IN, OUT> getAggregatingState(AggregatingStateDescriptor<IN, ACC, OUT>)
MapState<UK, UV> getMapState(MapStateDescriptor<UK, UV>)
这些state的获取都是可以在RuntimeContext中获取得到的。
state对应的存在失效时间的，state的失效时间是需要进行配置的。对应的配置是如下的:需要引入的是StateTtlConfig配置参数的。

SourceFunction 对应的是单个并发度的source的，ParallelSourceFunction以及RichParallelSourceFunction对应的是并发情况下的source的。
TextInputFormat:对应的是文本输出的规范操作实现。目录监测以及数据读取技术操作实现。目录监测对应的是单线程的,文件读取对应的是多线程的任务的。
数据写入到文件末尾的话,会导致文件被重复的处理的,这样的话，会破坏exactly-once语义的。需要注意一下这方面的东西。
事实上,网络之间的数据之间的流动是使用缓冲区来实现流动的。使用的对应的是buffer的技术的。对应的buffer可以解决对应的数据的缓冲问题的，flink可以在source端设置缓冲区的时间和大小的。
对应的为了减低数据处理的延迟性能,降低程序处理的延迟的话，可以设置较低的延迟处理速度。
一个job对应的是有界的还是无界的，取决于组成job的source的构成的，如果组成job的所有的source对应的都是有界的话，那么整体的是有界的，否则的话是无界的。
当job是有界的话，使用batch的执行模式会更加的有效的，否则的话，使用streaming模式会更加好的。原因在于针对batch模式和streaming模式的话，对应的底层采用了不同的优化措施的。这个是关键的。
配置batch模式的话，最终使用的是env.setRuntimeMode(RuntimeExecutionMode.BATCH);但是建议在运行参数里面配置的,不要在外面配置的。
批模式以及streaming模式针对于任务调度以及数据交换的话，对应的存在着不同的策略的。batch模式在任务调度层面以及数据交换层面做了更好的处理措施的。批模式使用了更加高效的数据结构和算法来保证操作的。
flink的shuffle操作阶段和处理实现。需要注意的是streaming模式下面，数据是不断的流动的，所以，多个算子之间是无法实现交流的，数据不可能同时处于两个算子中的。
批模式下的话，对应的是不需要存储state信息的。只有streaming模式下面才需要存储相关的state的状态信息的。state的存在对应的代表了后续的checkpoint的相关的恢复机制的。
streaming模式下面需要使用state以及checkpoint来完成状态的管理协同和恢复的。水印机制是为了解决streaming模式下面的数据延迟问题而提供的技术解决方案的。

使用状态状态来实现错误恢复操作实现。会从checkpoint的地方重启所有的task的。stream处理的元素而言，必须要有对应的时间戳的信息的，这些信息的
抽取是来自于某些字段的。水印对应的是对于事件事件的处理操作实现的。
水印策略建议直接在source operation上面直接操作的，其他的非operation操作的上面不建议使用水印策略操作实现的。
数据源对应的一段时间不产生数据，这样的数据源对应的称之为idle source的数据源的。可以如下设置的:
1)设置数据源为idle模式的:
2)水印对其策略设置，这个对正在处理的operator是合理的，对于其下游的operator就不行了，下游的话，存在了水印对其的设置和策略的。
可以针对每一个source实现水印对其操作设置的，可以针对每一个source设置水印对其操作的。水印对其还只是一个实验性的性质。

Periodic WatermarkGenerator:对应的是使用的是event-time或者是processing的处理时间来处理相关的事件的。
Punctuated WatermarkGenerator:对应的是根据数据携带的水印信息，携带了水印的话，对应的会执行相关的操作的。
public class PunctuatedAssigner implements WatermarkGenerator<MyEvent> {
    @Override
    public void onEvent(MyEvent event, long eventTimestamp, WatermarkOutput output) {
        //对应的是根据相关的是否携带有相关的水印信息来判断的，这个是需要元素中特定的标记数据的。每一个数据都带有水印的话,会降低对应的处理性能的。
        if (event.hasWatermarkMarker()) {
            output.emitWatermark(new Watermark(event.getWatermarkTimestamp()));
        }
    }
    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        // don't need to do anything because we emit in reaction to events above
    }
}
kafka的分区识别水印机制：如果kafka的分区是严格有序的，那么使用基于分区识别的水印的话，可以获取得到完美的全局水印的。
是一个不错的优化点的。
forMonotonousTimestamps:单调递增水印时间
forBoundedOutOfOrderness:固定延时的水印时间

#状态工作
1.keyed state需要对应的Keyed DataStream的，对应的形成Keyed DataStream的方式是有很多种的，包括keyBy内容的。使用key DateStream对应的需要相关的key Selector的定义的。
我们需要相关的key selector来定义对应的selector，从而形成keyed DataStream的。
2.keyed state仅限于用于Keyed DataStream中的。状态的获取需要借助于对应的输入元素的key来实现操作管理的。
状态的获取需要在RuntimeContext中获取得到的,这个需要注意的。state的获取对应的需要在richFunction中的。对应的使用需要的是RichFunction中实现的。
需要注意的是使用keyed DataStream的话,针对特定的key的话，数据是存在不同的,所以，我们需要存储很多的key的state的，会导致出现大量的状态的数据的。
需要注意的是flink存储了众多的key的state的数据的话,后续可以通过state的失效来降低state的总量的，状态数据的话，是会在重新读取的时候清除状态数据的。
使用异步的方式来保存state的snapshot的数据是比较好的。同步的话，是会存在一些阻塞的问题的。

每一个kakfa的operator维护了topic分区数以及offset的数量来作为操作符的state信息。operator state主要用于source/sink等的operator的
其他的基本上用不到的。
Broadcast State是一种特殊类型的operater的state的,对应的是需要在所有的下游的子任务中，维护统一的状态的。这个不支持修改的，不要乱用这个广播状态的。

#下面是使用的操作示例：使用operator的state数据，需要实现CheckpointedFunction接口的。
void snapshotState(FunctionSnapshotContext context) throws Exception; 每次执行快照的时候
void initializeState(FunctionInitializationContext context) throws Exception;对应的是初始化的时候或者是失败了恢复的时候执行一次的服务的
CheckpointedFunction对应的可以实现non-keyed的state的状态的恢复操作和实现的。

Even-split redistribution：状态均匀分发，当并行度发生改变的时候，state数据会均匀的分发到所有的任务上面的。会将所有的算子上的状态全部收集，然后均匀的分发到不同的算子上去的。
Union redistribution:每一个算子会获取得到所有的状态数据的。
区分的规则如下：
1）如果区分的规则中没有对应的分发规则，比如union的话，对应的分发就是Even-split redistribution。

Broadcast State:
1)broadcast的使用需要满足如下的条件的,non-broadcast DataStream 连接到对应的BroadcastStream
下面是使用举例操作示例代码:
KeyedStream<Item, Color> colorPartitionedStream = itemStream
                        .keyBy(new KeySelector<Item, Color>(){...});
MapStateDescriptor<String, Rule> ruleStateDescriptor = new MapStateDescriptor<>(
            "RulesBroadcastState",
            BasicTypeInfo.STRING_TYPE_INFO,
            TypeInformation.of(new TypeHint<Rule>() {}));
// broadcast the rules and create the broadcast state
BroadcastStream<Rule> ruleBroadcastStream = ruleStream
                        .broadcast(ruleStateDescriptor);
//下面使用连接操作,连接non-broadcast DataStream以及对应的BroadCast DataStream数据的。
process中的处理函数的类型取决于colorPartitionedStream的类型：
1)如果colorPartitionedStream对应的是keyed，函数类型是KeyedBroadcastProcessFunction
2)如果类型是non-keyed,对应的则是BroadcastProcessFunction类型的数据的。
colorPartitionedStream.connect(ruleBroadcastStream).
process(
     new KeyedBroadcastProcessFunction<Color, Item, Rule, String>() {}
 );

需要注意的是flink的任务的话，需要显式的调用execute的。
flink的数据是需要在操作符之间进行数据传输的。source以及对应的sink等对应的都是operator的。
序列化导致的cpu的性能损耗操作实现。序列化对应的消耗的cpu的时间中序列时间的。
flink存在自己的序列化框架。可以自定义实现自己的序列化框架和实现机制。
Row types对应的在table以及sql中使用的特别的多的。这个是sql或者是table中使用的较多的序列化的类型的
####对应的RowSource需要处理的是数据类型的相关的获取信息。
public static class RowSource implements SourceFunction<Row>, ResultTypeQueryable<Row> {
  // ...
  @Override
  public TypeInformation<Row> getProducedType() {
    //通知需要序列化的数据的类型信息。
    return Types.ROW(Types.INT, Types.STRING, Types.OBJECT_ARRAY(Types.STRING));
  }
}
序列化方式将会存储在checkpoints以及对应的savePoints中的，当job重启的时候回自动的恢复相关的序列化注册机制的。
序列化方式的fallback操作的话，对应的是特别的消耗性能的，尽量的避免这样的操作实现的。直接在使用的时候对应的使用相关的序列化方式就可以了。
state的模式迭代操作: POJO and Avro
flink的序列化框架选择的话，推荐使用tuple或者是row来进行序列化操作的。或者是使用pojo或者是protobuf的序列化方式支持的。




