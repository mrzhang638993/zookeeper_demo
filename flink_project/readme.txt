#flink相关知识
对应的每一个flink应用的话,都是需要一个执行环境的。
flink对应的可以理解为两层意思的：
1)静态代码的，flink对应的会将程序代码编译成为job graph的代码的
2)运行时:对应的静态的job graph会被编译成为真正能够运行的execution graph的执行图的。
flink的stream程序运行的话，如果不调用对应的execute的,对应的flink程序是不会启动的。
flink其实也是有一个中心化的思想，flink运行是的架构是一个主从的架构的，主节点负责资源调度以及对应的协调checkpoint执行操作实现
还有需要注意的是，flink底层使用DataStream完成替代DataSet的操作的,后续的所有的操作使用DataStream就可以完成了，不需要相关的DataSet的架构的
#flink的使用场景
such as Apache Kafka, Kinesis, and various filesystems. REST APIs and databases are also frequently used for stream enrichment.
在flink中使用对应的rest的方式需要关注一下的。在flink中调用相关的代码需要进行关注的。这个可以理解一下，
flink主要的分布式操作是生成对应的job graph的。flink的分布式的前提是对应的operator算子的，这个是flink实现分布式调度的核心观点的。
所以，如果不在operator中使用rest操作的话，对应的是不会执行相关的分布式操作的。这个核心是需要领会的。
#生产中常见使用的sink如下：
commonly used sinks include the StreamingFileSink, various databases, and several pub-sub systems.
主要包括文件sink,数据库的sink操作，各种发布订阅系统。
#
在使用的cpu的周期中,对应的序列化以及反序列化需要消耗很多的cpu的时钟周期的。优秀的序列化以及反序列化操作是很重要的。
尽量的避免程序中无意义的序列化和反序列化相关的操作实现,这个很关键,影响程序的性能和实现。
# 下面是flink中常见的序列化和反序列的方式的,主要包括下面的几种方式:
1)Flink-provided special serializer:flink提供的序列化方式;
2)POJOs:对应的pojo对象,主要用于po的序列化操作，这里面有部分的规则的。包括不能包含静态变量以及non-transient变量的。
3)Generic types:泛型的变量信息，用户自定义的，不能识别为POJO类型的。使用Kryo完成对应的序列化操作。
4)可以使用用户自定义化的序列化器以及反序列化器的。可以使用Google Protobuf或者是 Apache Thrift。
5)TupleSerializer:对应的是序列化的方式的，主要用于序列化tuple的。
6)Row Data Types :主要用于table或者是sql中使用的。使用row类型的话，需要告诉flink数据类型的。RowSerializer后续会根据给定的数据类型完成序列化操作的。
可以采用如下的方式来实现相关的rowType的信息保存的：
1)方式一:采用如下的方式
public static class RowSource implements SourceFunction<Row>, ResultTypeQueryable<Row> {
    @Override
    public TypeInformation<Row> getProducedType() {
      return Types.ROW(Types.INT, Types.STRING, Types.OBJECT_ARRAY(Types.STRING));
    }
  }
2)方式二：可以采用如下的方式
DataStream<Row> sourceStream =
    env.addSource(new RowSource())
        .returns(Types.ROW(Types.INT, Types.STRING, Types.OBJECT_ARRAY(Types.STRING)));
使用row的时候，如果不指定数据类型的话，对应的会使用Kryo来进行序列化操作的，后续会抛出序列化异常的信息的。
3)Avro序列化操作:需要引入相关的依赖。org.apache.flink:flink-avro。可以使用如下的方式
1方式一:指定Avro的schema约束信息
public static class AvroGenericSource implements SourceFunction<GenericRecord>, ResultTypeQueryable<GenericRecord> {
  private final GenericRecordAvroTypeInfo producedType;
  public AvroGenericSource(Schema schema) {
    this.producedType = new GenericRecordAvroTypeInfo(schema);
  }
  @Override
  public TypeInformation<GenericRecord> getProducedType() {
    return producedType;
  }
}
方式二:在对应的source上面构建相关的schema的约束信息
DataStream<GenericRecord> sourceStream =
    env.addSource(new AvroGenericSource())
        .returns(new GenericRecordAvroTypeInfo(schema));
方式三:使用Avro作为默认的序列化器使用
env.getConfig().enableForceAvro();
7)Kryo序列胡:
使用Kryo序列化器可以使用如下的方式的:
方式一:注册使用Kryo来序列化对应的类型
env.getConfig().registerKryoType(MyCustomType.class);
env.getConfig().disableGenericTypes();  #禁止序列化失败之后使用Kryo来序列化,可以用于检查序列化失败的异常信息的。
8)Apache Thrift:使用的是Kryo
可以使用kryo注册其他类型的序列化器来使用的。需要引入相关的依赖的
com.twitter:chill-thrift 或者是org.apache.thrift:libthrift类型的。
#注册默认的KryoSerializer的实现的
env.getConfig().addDefaultKryoSerializer(MyCustomType.class, TBaseSerializer.class);要求MyCustomType是一个Thrift-generated类型的数据的
#或者是使用如下的方式来实现的
registerTypeWithKryoSerializer:注册KryoSerializer序列化,这个和前面的注册默认的序列化器效果是一样的。
例如：env.getConfig().registerTypeWithKryoSerializer(MyCustomType.class, ProtobufSerializer.class);
9)使用Kryo注册Protobuf序列化方式
引入依赖,com.google.protobuf:protobuf-java
#注册序列化方式
env.getConfig().registerTypeWithKryoSerializer(MyCustomType.class, ProtobufSerializer.class);
这里面要求MyCustomType必须是一个Protobuf-generated的类型的，否则会报错的。
或者是如下的方式来序列化的。
env.getConfig().addDefaultKryoSerializer(MyCustomType.class, ProtobufSerializer.class);
flink1.10只是支持两种状态模式的演变：
POJO and Avro
#序列化方式的性能比较操作和实现
1)从pojo转换成为Kryo的重试会降低75%的性能;
2)使用反射方式相比较于Kryo会降低45%的性能的，因为反射对应的是间接访问属性字段，其他的是直接访问的。pojo的序列化方式
也是使用反射的方式来操作的。
3)tuple想教于pojo的话，会提高42%的性能的。但是唯一的问题在于tuple的弹性扩展的功能不强，后续的修改的话,需要修改代码的。
不能使用POJO的话，可以使用其他的自定义化的序列化方式的。
Protobuf, Avro, Thrift (in that order, performance-wise).
使用env.getConfig().disableGenericTypes(); 可以显著的提升相关的效率的。

1.dataStream  program对应的转换操作
DataStream programs对应的就是在stream上面执行对应的转换操作的，转换操作对应的包含了一个或者是
多个的操作算子的。
dataStream中的数据是不可变的，一旦创建的话，不可修改和删除操作。不可检查元素，仅能在元素上面执行操作。
在这些dataStream上的操作，我们称之为transformations 转换操作。
对应的是基于dataStream上的相关的操作实现的。

#flink常见的项目对应的包括如下的5个步骤的：
1.获取项目的执行环境 Obtain an execution environment ;
2.加载或者是创建初始化的数据 Load/create the initial data;
3.指定基于数据上的转换操作 Specify transformations on this data;
4.指定数据输出位置 Specify where to put the results of your computations
5.触发程序的执行 Trigger the program execution。


flink应用对应的就是在dataStream上运用相关的转换算子实现操作。
DataStream在flink中代表的是一种特定的集合数据，代表的是不可变的数据集合。他和java中的集合的使用是
有很多的类似的方式的。一旦创建的话,对应的就是不可变的，不能增加和移除相关的内容的。不能检测里面的数据的，
我们称之为数据的转换操作的。
需要注意的是这种dataStream的转换操作，不建议创建过多的中间过度的过程的，flink其实只需要指定对应的转换算子的操作实现即可的
所以，我们不需要过多的关注太多的细节的，其整个的过程对应的是一个流式的过程的。
所以，整个的过程是这样的。
1.source的创建;
2.转换算子的运用;
3.sink操作实现
flink的sink操作不建议使用write*相关的操作的，write相关的操作对应的不是exactly-once语义的，推荐使用的是addSink实现相关的操作的，
其本质是调用底层的StreamingFileSink来实现相关的exactly-once的语义的。
setAutoWatermarkInterval(long milliseconds):控制自动产生水印的时间间隔，
flink批处理的执行结果和stream处理得到的执行结果是一样的，这个是flink的stream以及batch处理的相似特性所在的。
在stream以及batch模式下的话，可以使用不同的策略来实现相关的优化的，使用不同的各种优化措施，最终是需要得到相同的结果的。
问题:如何在将批处理模式下面的结果延续到stream模式下面去,从而实现优化操作和实现机制。
flink默认的对应的是stream模式操作的，需要配置batch模式或者是其他的模式的话。
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.setRuntimeMode(RuntimeExecutionMode.BATCH);
###对应的执行操作语句和实现逻辑
-Dexecution.runtime-mode=BATCH
不建议在程序中设置运行模式，在运行参数中设置运行模式比较的好。
flink将相同的operator组成了对应的task，不同的task之间对应的存在数据的shuffle操作的。多个operator对应的是一个chain的过程的。
多个任务之间构成了对应的flink的执行任务的job graph流程图。根据对应的shuffle操作我们可以将其划分为对应的stage的。
flink的多个操作符而言：在stream模式下面，不涉及操作符的顺序问题，数据来了就处理。在批处理模式下面是会涉及到数据的处理操作的。
在批模式下面，水印对应的是不产生作用的。但是设置一个WatermarkStrategy任然是需要的。增加水印时间戳信息。批模式下面可以指定最大的水印时间间隔的，对应的数据可以理解为完美的使用水印间隔的。
水印时间的意义，对应的意思是如下的：时间是timestamp-延迟时间<t。对应的不允许水印时间层面的t<T的延迟的。需要注意的是同样的一个数据，两次不同的时间处理的话，
对应的时间戳是不一样的。批模式下面的timers机制也是有用的，但是触发的机制对应的都是在输入的结束阶段触发的，所以，意义不大。

stream的执行模式中，flink使用checkpoint作为错误的恢复机制。flink的stream任务失败的话，对应的会从checkpoint成功的地方启动的。这个会造成很大的性能损耗的，相比较于batch而言。
在batch模式下面，flink会根据stage来启动的，只需要启动失败的stage的任务的，前面已经完成的任务是不会重新启动的，从而节约了很多的资源的。
spark Streaming以及flink的streaming的特点是会持续的占用资源的，如果可以动态的调节资源的占用和使用的话,是会存在很大的性能提升的。
需要注意的是flink的exactly-once保证是通过checkpoint来保证的。那么在batch模式下面如何保证对应的exactly-once的语义的。
水印策略实现机制：
1:水印策略主要是用于对应的source上面的。推荐使用这种操作的。

###处理逻辑操作实现
1.数据源的空闲间隔控制:
#当超过了配置时间之后,下游将不再处于等待状态的。增加了空闲检测机制的。下游水印会不断的增加的，从而解决相关的问题的。
WatermarkStrategy
        .<Tuple2<Long, String>>forBoundedOutOfOrderness(Duration.ofSeconds(20))
        .withIdleness(Duration.ofMinutes(1));
2.水印对其机制:
当单个或者部分分区产生数据的速度明显的多余其他的分区的话，这个时候下游处理的operator会产生背压的效果的。
可以启用水印对其机制
#配置水印对其策略，避免出现数据消费不一致的问题。
WatermarkStrategy
        .<Tuple2<Long, String>>forBoundedOutOfOrderness(Duration.ofSeconds(20))
        .withWatermarkAlignment("alignment-group-1", Duration.ofSeconds(20), Duration.ofSeconds(1));
需要注意的是过量的水印的话，会降低程序的处理性能的。不需要频繁的创建水印。
PunctuatedAssigner:对应的是根据数据本身的特征来生成水印的。比如携带有某些特征标志的。
PeriodicAssigner:对应的是根据配置的间隔时间定期的生产相关的水印数据的。可以通过配置参数，ExecutionConfig.setAutoWatermarkInterval(...)来控制水印生成的频率特性的。
kafka connector的分区水印对其机制:
flink中的operators是如何处理水印的:operator需要先处理watermark然后让数据往下流动的，这个处理过程中，包括了窗口触发的相关的内容。
当所有触发的数据生成了之后，才会存在水印往下继续传递的操作的。
WatermarkStrategy.forMonotonousTimestamps(); #单调递增水印
WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10));#规定延迟的水印,需要提前知道对应的延迟时间的。


#状态和容错机制实现:flink的有状态程序
在operator中使用相关的状态；
1.keyed state:需要在使用keyby之后。推荐使用lamada表达式来生成对应的selector选择器的。
Keyed State仅仅能够用于KeyedStream中的。
ValueState：对应的仅仅包含了单个值的state，后续的操作使用update执行更新操作实现。
ListState:
ReducingState:
AggregatingState:聚合结果
MapState:
需要注意的是从state中获取的数值取决于输入数据对应的key的，不同的调用的情况下获取得到对应的数值是不一样的。这个是需要确认和记忆的。
很关键的操作的。
#state的引用手柄:StateDescriptor
1.ValueStateDescriptor:
2.ListStateDescriptor:
3.AggregatingStateDescriptor:
4.ReducingStateDescriptor:
5.MapStateDescriptor:
使用RuntimeContext可以访问相关的state的数据的。对应的RuntimeContext是存在于richFunction中的
所以，最好是使用richFunction来进行访问操作。
1）state的超时时间控制:
#对应的可以在open方法中进行设置的。
StateTtlConfig ttlConfig = StateTtlConfig
    .newBuilder(Time.seconds(1))
    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
    .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
    .build();
ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("text state", String.class);
stateDescriptor.enableTimeToLive(ttlConfig);
#对应的state的状态清理操作:在snapshot阶段执行失效的state的清理操作。对应的可以称之为全量快照
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
StateTtlConfig ttlConfig = StateTtlConfig
    .newBuilder(Time.seconds(1))
    .cleanupFullSnapshot()
    .build();
#实现增量更新操作,增量更新配置操作实现
import org.apache.flink.api.common.state.StateTtlConfig;
 StateTtlConfig ttlConfig = StateTtlConfig
    .newBuilder(Time.seconds(1))
    .cleanupIncrementally(10, true)
    .build();
需要注意的是state的清除操作对应的是在state的访问或者是操作之后执行的,如果不存在前面的行为的话,失效的state是存在的。
增量清除state的操作会增加程序处理的延迟的。增量清除当前仅适用于heap堆中的state的，存储中的暂时不支持。
#对于存储在rocks db中的state的数据而言，可以采用如下的方式实现清除操作的。
import org.apache.flink.api.common.state.StateTtlConfig;
StateTtlConfig ttlConfig = StateTtlConfig
    .newBuilder(Time.seconds(1))
    .cleanupInRocksdbCompactFilter(1000) #使用压缩过滤清除操作
    .build();
#operator state:对应的绑定到了operator上面的状态数据的。需要是并行的operator的
kafka的operator:state中保存了topic的分区以及对应的offset信息。这种主要是用于sink/source端的
其他的场景下面暂时是用不到的。
Broadcast State ：是一种特殊的operator state的。广播流的场景对应的是如下的:一个stream的所有的数据需要广播到下面的所有的operator中的
在所有的子任务中维护相同的状态信息。

checkpoint可以理解为对应的类似redis的缓存一样的数据的。
并发的情况下，需要使用到flink的并发锁的控制的。并发控制实现和管理操作。我们使用的sourceFunction对应的都是单个并发度的实现操作的。
@Override
public void run(SourceContext<Long> ctx) {
    final Object lock = ctx.getCheckpointLock();
    while (isRunning) {
        // output and state update are atomic
        synchronized (lock) {
            ctx.collect(offset);
            offset += 1;
        }
    }
}
flink中不存在跨任务的交流操作的。broadcast对应的是可以实现相关的数据的读写更新操作实现的。
可以在flink的open方法中调用http的接口完成相关的数据的恢复和保存操作的。典型的示例代码如下的：
1.单个的并发度的数据操作；2.在代码中使用相关的http的操作实现的。3注册一个timer实现相关的任务的定时调度操作；
需要注意的是广播变量播放到下游的话是没有顺序的，不能够根据数据的顺序来保证其顺序特性的，这个是很关键的。无序特性。
还有需要注意的是，在执行checkpoint的时候，对应的broadcast的数据也是会执行保存的。会增加存储的消耗，但是确保了广播数据不会出现多或者是遗漏的情况的。
还有需要主要的是broadcast变量的数据是存储在内存中的，不是储存在rocksdb中的，这个需要注意的。
state可以保证处理数据之间的共享部分，checkpoint可以保证这种state的容错机制的。从而保证flink程序的无错运行。

###特性可以描述为如下的:分布式的状态存储以及数据存储的。
需要执行checkpoint的话，需要满足如下的条件的:
1.持久化的数据源，可以重放特定时间的一定量的数据的；
2.持久化的状态存储,最好是使用分布式的存储。比如hdfs,s3,gfs,nfs,ceph.

默认的情况下,checkpoint是被禁止的，启用checkpoint的话需要如下的配置:
1.enableCheckpointing(n);
2.checkpoint storage:存储,推荐使用持久化的存储;
3.exactly-once vs. at-least-once:配置一致性的控制;
4.checkpoint timeout:超时时间控制;
5.minimum time between checkpoints:checkpoint之间的间隔;
6.tolerable checkpoint failure number:能够容忍的检查点失败的次数;
7.number of concurrent checkpoints:并发检查点的个数。
8.externalized checkpoints:外部存储
9.unaligned checkpoints:检查点对其,仅仅适用于exactly-once的checkpoint以及在背压的情况下使用的。并且只有一个并发的checkpoint。
10.checkpoints with finished tasks:通常情况下,任务完成之后，flink还是会执行checkpoint的,可以禁止对这部分的task执行checkpoint操作。
#####下面是一个典型的checkpoint的配置操作:
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
// start a checkpoint every 1000 ms
env.enableCheckpointing(1000);
// advanced options:
// set mode to exactly-once (this is the default)
env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
// make sure 500 ms of progress happen between checkpoints
env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
// checkpoints have to complete within one minute, or are discarded
env.getCheckpointConfig().setCheckpointTimeout(60000);
// only two consecutive checkpoint failures are tolerated
env.getCheckpointConfig().setTolerableCheckpointFailureNumber(2);
// allow only one checkpoint to be in progress at the same time
env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
// enable externalized checkpoints which are retained
// after job cancellation
env.getCheckpointConfig().setExternalizedCheckpointCleanup(
    ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
// enables the unaligned checkpoints
env.getCheckpointConfig().enableUnalignedCheckpoints();
// sets the checkpoint storage where checkpoint snapshots will be written
env.getCheckpointConfig().setCheckpointStorage("hdfs:///my/checkpoint/dir");
// enable checkpointing with finished tasks
Configuration config = new Configuration();
config.set(ExecutionCheckpointingOptions.ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH, true);
env.configure(config);
#state相关的配置
state.backend.incremental:state的增量备份配置
state.checkpoint-storage:
state.backend.local-recovery
state.checkpoint-storage
state.checkpoints.dir
state.checkpoints.num-retained
state.savepoints.dir
state.storage.fs.memory-threshold
state.storage.fs.write-buffer-size
taskmanager.state.local.root-dirs
#checkpoint的配置
默认的情况下，checkpoint是配置在jobManager的内存中的。对于巨大的state而言，推荐checkpoint存储在高可用的生产级别的文件系统中。
对处理过程的支持,当前仅支持不含迭代的处理流的。需要启动对于迭代流的支持的话，需要使用如下的选项：
env.enableCheckpointing(interval, CheckpointingMode.EXACTLY_ONCE, force = true).
但是任然需要主要的是，迭代流的数据丢失问题的。
####对于部分完成的任务不再执行checkpoint操作
Configuration config = new Configuration();
config.set(ExecutionCheckpointingOptions.ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH, false);
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
checkpoint采用两阶段提交的方式，但是两阶段提交存在一个问题，当第一阶段一直无法满足的话，第二阶段会一直处于等待状态的。
####激活可查询的state Activating Queryable State
1.查询state对应的会破坏服务器的稳定性的，不建议使用的。
####配置state backend
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.setStateBackend(...);
######Accumulators & Counters
IntCounter,LongCounter,DoubleCounter
Histogram
累加器是使用在不同的operator中的，不同的operator中是可以使用相同名称的累加器的。
需要注意的是累加器仅仅是在全部的任务结束之后才可以使用的，所以，无法结束的任务的话，累计器的结果是无法使用的。
计数器 counter是累加器Accumulators的一个特例的，只能在job结束之后才可以获取统计结果的。
####flink相关的算子:operator
1.map:输入一个输出一个
2.flatMap:输入一个，输出0个，1个或者是多个
3.Filter:对每一个元素生成相关的boolean函数，保留true类型的函数;
4.KeyBy:相同key的数据会分配到同样的分区中，keyby的实现是hash分区的；
5.Reduce:对应的是在keyby分组之后，在单一的key上执行滚动，最终生成唯一的数值。基于keyedStream
6.Window:基于已经分组的keyStream上面操作，
7.WindowAll:基于常规的数据stream进行window操作，
dataStream.windowAll(TumblingEventTimeWindows.of(Time.seconds(5)));
8.Window Apply:可以基于windowStream或者是windowAllStream实现操作。
windowedStream.apply(new WindowFunction<Tuple2<String,Integer>, Integer, Tuple, Window>() {
    public void apply (Tuple tuple,
            Window window,
            Iterable<Tuple2<String, Integer>> values,
            Collector<Integer> out) throws Exception {
        int sum = 0;
        for (value t: values) {
            sum += t.f1;
        }
        out.collect (new Integer(sum));
    }
});
// applying an AllWindowFunction on non-keyed window stream
allWindowedStream.apply (new AllWindowFunction<Tuple2<String,Integer>, Integer, Window>() {
    public void apply (Window window,
            Iterable<Tuple2<String, Integer>> values,
            Collector<Integer> out) throws Exception {
        int sum = 0;
        for (value t: values) {
            sum += t.f1;
        }
        out.collect (new Integer(sum));
    }
});
9.WindowReduce:在window内部实现元素的聚合操作实现。
windowedStream.reduce (new ReduceFunction<Tuple2<String,Integer>>() {
    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
        return new Tuple2<String,Integer>(value1.f0, value1.f1 + value2.f1);
    }
});
10.Union:两个stream的聚合操作实现。一个stream聚合自身的话，会得到同样的两倍的数据。
不同的话，union会得到聚合stream中的所有的元素的。
11.Window Join:join操作是根据key以及对应的window实现join操作的。
12.Interval Join ：基于指定的key在特定的时间段范围之内实现窗口的join操作实现的。
flink sql中的join操作实现机制如下：flink会根据key将相同的key的数据发到同样的一个分区中，然后使用双层的for循环来组合数据的，
join实际上调用了底层的cogroup的join操作的，这样的话，可以解决底层的join的原理相关的问题的。
会先将join的两个stream合并起来的，成为一个stream的，后续的话，会根据key进行分组的话，分到不同的分区进行操作的。
13.Window CoGroup：对应的会将两个stream合并成为一个stream的，然后在特定的窗口之上执行合并操作实现的。
14.Connect:连接这两个stream,但是任然保留了每一个stream的数据和类型的。
15.CoMap, CoFlatMap:对应的在connect的连接流上执行map以及flatMap操作实现。
connectedStreams.map(new CoMapFunction<Integer, String, Boolean>() {
    @Override
    public Boolean map1(Integer value) {
        return true;
    }
    @Override
    public Boolean map2(String value) {
        return false;
    }
});
connectedStreams.flatMap(new CoFlatMapFunction<Integer, String, String>() {
   @Override
   public void flatMap1(Integer value, Collector<String> out) {
       out.collect(value.toString());
   }
   @Override
   public void flatMap2(String value, Collector<String> out) {
       for (String word: value.split(" ")) {
         out.collect(word);
       }
   }
});

###在转换之后,可以执行底层数据的分区操作实现。
1.partitionCustom：用户自定义分区策略;
2.shuffle:随机分区操作
3.rescale:负载均衡的方式将上游的数据发送到下游。
4.broadcast:将数据广播到下游的每一个分区。
5.Task Chaining:flink存在有任务链条的机制的，可以使用同样的线程来提高效率的。
如果需要打断这样的链条的话，可以存在有如下的方式的：
StreamExecutionEnvironment.disableOperatorChaining():禁止使用operator的链条机制
someStream.map(...).startNewChain():在对应的operator之后开启新的链条，只能在转换之后操作
flink的资源的管理使用的是slot的,实现对于slot的管理可以提高相关的效率的。
6.设置slot的资源分享组:flink会将同样的一个分享组加入到同样的一个slot中的。
7.可以给算子增加名称或者是描述信息，后续可以用于定位信息
someStream.filter(...).setName("filter").setDescription("x in (1, 2, 3, 4) and y > 1");

































