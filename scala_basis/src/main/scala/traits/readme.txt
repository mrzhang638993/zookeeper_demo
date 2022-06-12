1.实现相关的特质的相关的代码和实现机制的。
2.继承:只能够继承单一的类的。但是可以实现多个的特质的。scala中的特质也是使用的是extends
进行操作的。java中使用的是implements的,scala中是使用的是extends的,实现多个特质的话，需要使用with实现操作
的。
3.trait中可以增加具体的字段以及抽象化的字段信息。
4.可以使用模板设计模式来实现代码更加优化的编写操作和实现机制的。对应的设计是灵活的可扩展的操作的
5.对象的trait操作,可以直接将特质混入到对象之中实现操作的。
直接在构造对象的时候使用特质来丰富对象的相关的特性的。
6.混入操作:在对象的创建过程中使用with混入特定的特质,从而使用相关的特质的实现方法。
7.在对象创建的时候对应的使用特质完成更加丰富的功能实现的。对象的混入只是会对于当前的对象产生作用的
没有混入特质的对象的话，就不会具备对应的相关的功能的。
8.scala的调用链模式实现操作:scala的调用链是实现从右向左调用相关的机制的。
那么java中的多个相同的接口的情况下其实也是可以实现这种调用链模式的，但是这种方式实现的调用链
模式存在很多的问题的，随着链条的不断的增长的话，对应的最终实现的接口的话也是会不断的增多的
同时还存在一个问题就是需要记忆对应的左右的顺序的话，这种调用链模式的话，存在很多的缺陷的。
可以使用next方法来完成更多的调用链的操作实现的。体现出来链条的执行顺序的。
9.trait对应的是可以继承一个class的。对于trait以及相关的class的话,对应的理解为一套逻辑
概念即可的,不需要过多的领会相关的其他的知识体系的。过多的理解,相关的泛化的效果就很差了。

需要注意的是keyed的元素的话,对应的key是会发送到单个的分区中实现操作的。
####window的lifecycle
1.属于本窗口的第一个元素到来的时候,窗口开始创建的；窗口结束的时间是事件时间+处理时间+允许的延迟时间的所有的元素
都已经处理了。flink可以保证基于时间的窗口的移除的，其他的类型则不保证的。
例如：5分钟的固定窗口+1分钟的延迟的，对应的会从12:00--->12:06分结束的。
每一个window对应的都是存在有一个trigger以及function的。触发器指定了窗口触发的条件的。
Evictor:对应的会在窗口触发的时候实现元素的清除操作的。
2.区分Keyed vs Non-Keyed Windows
区分的方式：是否使用了keyBy的operator操作。不使用keyed的话，数据是单个的task处理的，不存在什么并行度的
使用keyed的话，对应的是存在并行度的，有助于提交效率的。

####window
1.window对应的是无限流的核心，window会将无限流转换成为bucket，可以在这些bucket上面执行计算。
2.如何在flink中执行window以及如何从window中获益。
对于keyBy得到的keyedStream而言,可以使用window操作
对于non-keyed的stream的话,使用windowAll操作
trigger引发的数据清除，只会影响window中的数据，不会影响到相关的元数据的。所以在清除的过程中是允许源数据进入的。
window数据处理的逻辑如下:
1)数据进入到window中----->trigger触发器处理数据------->evictor处理数据------>function处理数据
non-keyed stream会将整个的stream作为一个task进行处理的，不存在并行度控制的。
keyed-stream对应的会根据keyed来进行并行操作的，相同的key会发送到相同的task上去的。
WindowAssigner:对应的会决定将元素送到那个assigner中进行操作。大多数的情况下，flink预定义了很多的window，
包含了如下的window的:tumbling windows, sliding windows, session windows and global windows
可以自定义window,实现相关的WindowAssigner,
如下:public class MyWindow extends WindowAssigner
除了global window而言，其他的window对应的都是基于时间进行处理的。包括processing time 以及event-time时间。
基于时间的窗口对应的是左闭又开的区间的,[a,b)
Tumbling Windows:固定的时间窗口，固定大小并且不滑动的窗口。需要主要的是固定时间窗口的话，可以对应的指定offset的，可以用于解决时区的问题的。
TumblingEventTimeWindows:固定事件时间，TumblingProcessingTimeWindows:固定处理时间
input
    .keyBy(<key selector>)
    //解决时区的问题,示例代码如下:Time.hours(-8)对应的延时8小时实行操作实现
    .window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)))
    .<windowed transformation>(<window function>);
Sliding Windows:滑动窗口,使用的是SlidingEventTimeWindows(事件时间),SlidingProcessingTimeWindows(处理时间)
Session Windows:基于activity的session来处理数据,一定时间没有数据的话，对应的会关闭窗口。使用如下的类实现操作。
EventTimeSessionWindows以及ProcessingTimeSessionWindows
EventTimeSessionWindows.withGap(Time.minutes(10)):静态的时间间隔
SessionWindowTimeGapExtractor:动态的时间间隔,实现对应的抽取时间间隔的逻辑。需要注意的是SessionWindow需要
一个trigger以及对应的window function。
Global Windows:全局window的特性是窗口不会关闭的，除非自定义trigger进行触发。否则是不会存在有任何的输出的。
#需要注意的是global window对应的是针对每一个key指定全局的window的，并不是全局使用一个window的。这个窗口会一直开启的。
input
    .keyBy(<key selector>)
    .window(GlobalWindows.create())
    .<windowed transformation>(<window function>);
#######常见的window function
常见的window  function包括了:ReduceFunction,AggregateFunction,以及对应的ProcessWindowFunction。
ReduceFunction:输入两个元素，生成一个同类型的元素。
input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .reduce(new ReduceFunction<Tuple2<String, Long>>() {
      public Tuple2<String, Long> reduce(Tuple2<String, Long> v1, Tuple2<String, Long> v2) {
        return new Tuple2<>(v1.f0, v1.f1 + v2.f1);
      }
    });
AggregateFunction:包含了三个部分，输入元素，accumulator，输出元素
private static class AverageAggregate
    implements AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Long>, Double> {
    //定义累加器
  @Override
  public Tuple2<Long, Long> createAccumulator() {
    return new Tuple2<>(0L, 0L);
  }
  //实现累加器叠加操作
  @Override
  public Tuple2<Long, Long> add(Tuple2<String, Long> value, Tuple2<Long, Long> accumulator) {
    return new Tuple2<>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
  }
  //实现数据抽取操作
  @Override
  public Double getResult(Tuple2<Long, Long> accumulator) {
    return ((double) accumulator.f0) / accumulator.f1;
  }
  //元素相加操作
  @Override
  public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
    return new Tuple2<>(a.f0 + b.f0, a.f1 + b.f1);
  }
}
ProcessWindowFunction:包含了迭代window中的所有元素的迭代器,还包含了访问时间以及状态信息的context上下文信息。
//自定义的ProcessWindowFunction实现。
public class MyProcessWindowFunction extends ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow> {
}
//需要注意的是MyProcessWindowFunction可以结合ReduceFunction以及AggregateFunction来提高聚合效率的
1.在处理函数中引入reduce操作。
input
  .keyBy(<key selector>)
  .window(<window assigner>)
  //实现高效的元素处理以及增加更新机制实现
  .reduce(new MyReduceFunction(), new MyProcessWindowFunction());
// Function definitions
private static class MyReduceFunction implements ReduceFunction<SensorReading> {
  public SensorReading reduce(SensorReading r1, SensorReading r2) {
      return r1.value() > r2.value() ? r2 : r1;
  }
}
private static class MyProcessWindowFunction
    extends ProcessWindowFunction<SensorReading, Tuple2<Long, SensorReading>, String, TimeWindow> {
  public void process(String key,
                    Context context,
                    Iterable<SensorReading> minReadings,
                    Collector<Tuple2<Long, SensorReading>> out) {
      SensorReading min = minReadings.iterator().next();
      out.collect(new Tuple2<Long, SensorReading>(context.window().getStart(), min));
  }
}
2.引入增量聚合操作实现
input
  .keyBy(<key selector>)
  .window(<window assigner>)
  .aggregate(new AverageAggregate(), new MyProcessWindowFunction());
//增量聚合操作实现
private static class AverageAggregate
    implements AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Long>, Double> {
  @Override
  public Tuple2<Long, Long> createAccumulator() {
    return new Tuple2<>(0L, 0L);
  }
  @Override
  public Tuple2<Long, Long> add(Tuple2<String, Long> value, Tuple2<Long, Long> accumulator) {
    return new Tuple2<>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
  }
  @Override
  public Double getResult(Tuple2<Long, Long> accumulator) {
    return ((double) accumulator.f0) / accumulator.f1;
  }
  @Override
  public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
    return new Tuple2<>(a.f0 + b.f0, a.f1 + b.f1);
  }
}
//ProcessWindowFunction处理元素
private static class MyProcessWindowFunction
    extends ProcessWindowFunction<Double, Tuple2<String, Double>, String, TimeWindow> {
  public void process(String key,
                    Context context,
                    Iterable<Double> averages,
                    Collector<Tuple2<String, Double>> out) {
      Double average = averages.iterator().next();
      out.collect(new Tuple2<>(key, average));
  }
}
#####在window中使用状态
1.ProcessWindowFunction中使用状态进行操作
1)只有在richContext中可以访问keyed state的；
2)使用基于window的keyed state：这个时候window中的每一个元素都有对应的state信息。
对于window中的每一个元素的state的话,可以采用如下的方式实现访问操作:
1)globalState:全局的window,不属于window的state
2)windowState:当前window范围的state信息。
#################################################
triggers:定义了窗口什么时候准备好了，可以处理数据了，执行processFunction函数调用操作实现。
WindowAssigner对应的是存在默认的实现的，当默认的实现满足不了条件的话,可以自定义WindowAssigner。
trigger的几个主要的方法:
1)onElement:窗口中的元素的处理方法,当窗口中的元素到达的时候,触发相关的方法;
2)onEventTime:当注册的基于时间的timer触发的时候调用
3)onProcessingTime:基于processingTime的timer触发的时候调用
4)onMerge:合并两个触发器的状态
5)clear:移除窗口的时候执行清理操作实现。
可以在上面的方法中注册timer用于后续的触发操作实现的。
#################触发和清理操作################################
当触发器决定处理元素的时候，触发器触发了，对应的返回FIRE或者是FIRE_AND_PURGE状态的。
FIRE:对应的执行计算的时候还会保持window中的元素，FIRE_AND_PURGE会在触发的时候清理元素,执行元素清除操作，但是会保存window的元数据信息的。
WindowAssigners:定义了window中的元素的处理逻辑。
1)基于event-time的WindowAssigner:底层默认使用的是EventTimeTrigger作为触发器的。水印通过窗口结尾处的时候触发的。
2)GlobalWindow的trigger是NeverTrigger,对应的是不会触发window的，所以，我们需要自定义一个trigger来进行window的触发操作的。
trigger()对应的使用相关的trigger方法来实现相关的触发实现操作的。
3)可以使用自定义的trigger触发器来覆盖默认的触发器实现，自定义自己的window的触发策略实现。
#################内置的以及自定义的触发器############################
EventTimeTrigger:基于event-time的watermark
ProcessingTimeTrigger:基于processing-time
CountTrigger:基于window中的元素的数量
PurgingTrigger:传递另外的一个trigge作为触发器参数，并将其作为清理的触发器。nestedTrigger.clear(window, ctx);
###############Evictors###############################
Evictors:可以在触发器触发之后，在元素处理之前执行部分的元素的移除操作实现。可以执行一些元素的验证和检查匹配操作实现
evictBefore():对应的是在window执行之前执行调用逻辑实现
evictAfter():对应的是在window function执行之后调用逻辑实现
内置的evict如下：
1)CountEvictor:超过了一定数量的话，会从buffer的开始销毁部分数据;
2)DeltaEvictor:删除大于或者是等于限值的。
3)TimeEvictor:计算出window中的最大的时间戳,删除时间戳满足:<=max_ts - interval的元素。
默认情况下,所有的内置的evictors处理的都是在window  function之前的元素的。配置了evictor可以避免预先聚合操作。
flink中不保证window中元素的顺序的，所以，移除元素是无法保证顺序的，是一种不可控的行为的。
##############允许的延迟##################
1.处理event-time window的时候，需要考虑到延迟的。
2.使用globalWindow的话，设置的延迟是Long.MAX_VALUE。其他的是waterTimeStamp+lateness
3.迟到的数据作为侧向输出结果，
//定义侧向输出标志
final OutputTag<T> lateOutputTag = new OutputTag<T>("late-data"){};
DataStream<T> input = ...;
SingleOutputStreamOperator<T> result = input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .allowedLateness(<time>)
    //迟到的数据输入到侧向输出流中进行操作管理。可以将丢失的数据写入到hbase中，后续从hbase中进行进一步的消费管理的。
    .sideOutputLateData(lateOutputTag)
    .<windowed transformation>(<window function>);
DataStream<T> lateStream = result.getSideOutput(lateOutputTag);
使用Lateness任然可以触发计算的原因在于：定义了另外的一个trigger的，称之为late firings。这种触发的元素是作为更新操作的
所以，我们需要在这里面考虑到元素的重复特性的。需要考虑重复结果和消除重复的结果。
4.window之间是可以持续计算的，上一个window的数据是可以持续的传递到下一个operator中的，所以，结果可以重用的
DataStream<Integer> input = ...;
//定义了5秒钟的固定大小的窗口
DataStream<Integer> resultsPerKey = input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .reduce(new Summer());
//使用了上面的5秒钟大小的窗口的，由于上面已经划分了窗口,所以，这边使用windowAll来实现窗口操作的。
DataStream<Integer> globalResults = resultsPerKey
    .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
    .process(new TopKWindowFunction());
//需要注意的是，window会对经过的每一个元素执行一个元素的拷贝操作的，所以会形成巨大的state的数据的。
这个过程中需要考虑如下的因素的：
1.使用固定窗口，元素只会保存一份。相对应的滑动窗口会保留多份数据的。所以1天的滑动窗口或者是1秒的滑动窗口不是一个号的方式
2.ReduceFunction以及AggregateFunction能够显著的降低存储的空间占用，所以，不要使用ProcessWindowFunction来执行window的聚合操作
3.推荐使用Evictor来避免预聚合操作。
################window的join操作####################
了解window join的本质特性操作实现:
1.window join操作:
window join操作是处理两个stream中的同样的key，同样的window的。
//对应的两个stream划分到了同样的一个window中的，然后进行对应的key的join操作实现。
stream.join(otherStream)
    .where(<KeySelector>)
    .equalTo(<KeySelector>)
    .window(<WindowAssigner>)
    .apply(<JoinFunction>);
注意事项：
1)需要注意的是上面的join对应的是inner-join操作的，当两个stream基于某一个key没有不关联的
话，对应的是没有结果输出的；
2)无法join的元素的话,对应的元素还是会存在于window中的。
###########下面是几种不同类型的join操作的###########
1.固定窗口的join操作：Tumbling Window Join
相同key，相同window的数据，基于相同的key可以连接的话，会长生数据，类似于inner-join操作机制的。
DataStream<Integer> orangeStream = ...;
DataStream<Integer> greenStream = ...;
orangeStream.join(greenStream)
    //第一个stream的选择条件
    .where(<KeySelector>)
    //第二个stream的选择条件
    .equalTo(<KeySelector>)
    //指定基于固定窗口执行join操作
    .window(TumblingEventTimeWindows.of(Time.milliseconds(2)))
    //选择条件符合之后的apply操作实现。
    .apply (new JoinFunction<Integer, Integer, String> (){
        @Override
        public String join(Integer first, Integer second) {
            return first + "," + second;
        }
    });
2.滑动窗口的join操作:Sliding Window Join
基于同样的key以及同样的滑动窗口的话，会执行join操作的。没有join上的会没有元素的输出的
DataStream<Integer> orangeStream = ...;
DataStream<Integer> greenStream = ...;
orangeStream.join(greenStream)
    //指定join条件
    .where(<KeySelector>)
    //指定join条件
    .equalTo(<KeySelector>)
    //指定滑动窗口执行join操作
    .window(SlidingEventTimeWindows.of(Time.milliseconds(2) /* size */, Time.milliseconds(1) /* slide */))
    //指定join操作
    .apply (new JoinFunction<Integer, Integer, String> (){
        @Override
        public String join(Integer first, Integer second) {
            return first + "," + second;
        }
    });
3.会话窗口join:Session Window Join
同样的key且满足完全满足会话条件的话，执行join操作。也是基于inner-join的机制来join的
DataStream<Integer> orangeStream = ...;
DataStream<Integer> greenStream = ...;
orangeStream.join(greenStream)
    //定义join条件
    .where(<KeySelector>)
    //定义join条件
    .equalTo(<KeySelector>)
    //指定基于session window实现join操作
    .window(EventTimeSessionWindows.withGap(Time.milliseconds(1)))
    //执行joinFunction操作
    .apply (new JoinFunction<Integer, Integer, String> (){
        @Override
        public String join(Integer first, Integer second) {
            return first + "," + second;
        }
    });
4.Interval Join
基于相同的key，并且stream b中的元素位于stream a指定的上下限的时间区域范围之内，可以执行join操作
b.timestamp ∈ [a.timestamp + lowerBound; a.timestamp + upperBound]
或者可以理解为如下的：a.timestamp + lowerBound <= b.timestamp <= a.timestamp + upperBound
其执行的join方式对应的也是inner-join操作的，不满足inner-join操作的话，元素也是不会输出的。
并且这种Interval Join只是支持基于event-time的。
DataStream<Integer> orangeStream = ...;
DataStream<Integer> greenStream = ...;
orangeStream
    .keyBy(<KeySelector>)
    .intervalJoin(greenStream.keyBy(<KeySelector>))
    .between(Time.milliseconds(-2), Time.milliseconds(1))
    .process (new ProcessJoinFunction<Integer, Integer, String(){
        @Override
        public void processElement(Integer left, Integer right, Context ctx, Collector<String> out) {
            out.collect(left + "," + right);
        }
    });
上面是关于flink相关的窗口的join机制的体现的，那么我们常见的mysql的各种jojn操作以及mongodb等的join操作是如何实现的？
##################################
##########处理函数################
1.ProcessFunction:
可以访问如下的内容：
1)events:事件;
2)state:基于容错或者是持久化操作,仅用于keyed stream
3)timers:基于event-time或者是processing-time,但是也是基于keyed stream的
ProcessFunction处理所有的input stream中的元素的,其可以访问对应的keyed state以及对应的定时器的。
可以通过RuntimeContext来访问对应的state实现容错机制。
定时器可以实现对于processing time以及event time的变化做出反馈操作。TimerService可以用于注册定时器的，
注意:需要访问keyed state的话，需要定义基于keyed stream的ProcessFunction的。
stream.keyBy(...).process(new MyProcessFunction());
##############低级别的join操作####################
1.实现更低级别的两个输入流的join操作,可以使用CoProcessFunction或者是KeyedCoProcessFunction
因为他们可以绑定两个输入流的元素，同时可以调用不同的处理方法来调用不同的输入流中的元素的。比如
processElement1或者是processElement2来调用不同的输入流中的元素。
2.join操作通常执行如下的模式的:
1)在一个输入流上面创建state对象或者是在相同的scope上;
2)基于输入的元素来执行state的更新操作;
3)基于另外一个stream的元素的输入,探测状态和生成join之后的结果;
下面是一个典型的processFunction的操作示例代码
DataStream<Tuple2<String, String>> stream = ...;
DataStream<Tuple2<String, Long>> result = stream
    .keyBy(value -> value.f0)
    .process(new CountWithTimeoutFunction());
//定义状态存储的数据信息
public class CountWithTimestamp {
    public String key;
    public long count;
    public long lastModified;
}
//定义KeyedProcessFunction的具体的实现
public class CountWithTimeoutFunction
        extends KeyedProcessFunction<Tuple, Tuple2<String, String>, Tuple2<String, Long>> {
    private ValueState<CountWithTimestamp> state;
    //初始化ValueStateDescriptor
    @Override
    public void open(Configuration parameters) throws Exception {
        state = getRuntimeContext().getState(new ValueStateDescriptor<>("myState", CountWithTimestamp.class));
    }
    //处理状态ValueState,并且注册一个timer
    @Override
    public void processElement(
            Tuple2<String, String> value,
            Context ctx,
            Collector<Tuple2<String, Long>> out) throws Exception {
        // retrieve the current count
        CountWithTimestamp current = state.value();
        if (current == null) {
            current = new CountWithTimestamp();
            current.key = value.f0;
        }
        // update the state's count
        current.count++;
        // set the state's timestamp to the record's assigned event time timestamp
        current.lastModified = ctx.timestamp();
        // write the state back
        state.update(current);
        // schedule the next timer 60 seconds from the current event time
        ctx.timerService().registerEventTimeTimer(current.lastModified + 60000);
    }
   //调度定时器的相关的方法，可以在这个地方完成其他的调用操作实现的，比如httpClient的调用操作。
   可以将每一次调动的结果对应的存储到state中去的,后续的话，可以根据对应的存储的状态来实现数据的恢复的。
    @Override
    public void onTimer(
            long timestamp,
            OnTimerContext ctx,
            Collector<Tuple2<String, Long>> out) throws Exception {
        // get the state for the key that scheduled the timer
        CountWithTimestamp result = state.value();
        // check if this is an outdated timer or the latest timer
        if (timestamp == result.lastModified + 60000) {
            // emit the state on timeout
            out.collect(new Tuple2<String, Long>(result.key, result.count));
        }
    }
}
3.KeyedProcessFunction:基于keyed的processFunction操作实现
基于event-time或者是processing-time的timer最终是TimerService进行管理调度的。如果基于某个时间戳注册了
多个timer的话，只会触发一次的timer的。
flink同步调用onTimer以及对应的processElement元素的，所以，不存在对应的并发修改的问题。
timer是具备容错特性的,checkpoint对应的也是具备容错特性的。
checkpoint的processing-time timers会在错误恢复的时候触发的，比如，应用从savePoint中重启的时候或者是应用从失败中重启的时候。
需要注意的是timer是和checkpoint同步的，大量的timer的话会增加checkpoint的时间的，因为定时器是checkpoint的state的一部分的
当然在结合其他的操作等的情况下，是可以异步的。
####################定时器的合并操作###############
timer属于checkpoint的一部分的，可以使用如下的方式来降低timer的数量，达到合并timer的效果的。
1)降低timer的精度，比如可以将定时器的精度从毫秒提升到秒的精度来降低定时器的数量的
long coalescedTime = ((ctx.timestamp() + timeout) / 1000) * 1000; //从毫秒提升到了秒级别
ctx.timerService().registerProcessingTimeTimer(coalescedTime);
示例2：
long coalescedTime = ctx.timerService().currentWatermark() + 1;
ctx.timerService().registerEventTimeTimer(coalescedTime);
2)定时器的移除操作:
long timestampOfTimerToStop = ...;
ctx.timerService().deleteProcessingTimeTimer(timestampOfTimerToStop);
或者是这样
long timestampOfTimerToStop = ...;
ctx.timerService().deleteEventTimeTimer(timestampOfTimerToStop);
###################外部数据源使用异步io的方式##########################
使用异步io数据源的前提是source提供了异步的client。使用异步的数据源需要如下的三个步骤：
1)AsyncFunction的实现,用于处理异步请求;
2)回调结果获取得到对应的ResultFuture;
3)在数据源上面执行异步io的转换操作
//定义异步数据源的实现，RichAsyncFunction的一个实现
class AsyncDatabaseRequest extends RichAsyncFunction<String, Tuple2<String, String>> {
    /** The database specific client that can issue concurrent requests with callbacks */
    private transient DatabaseClient client;
    @Override
    public void open(Configuration parameters) throws Exception {
        client = new DatabaseClient(host, post, credentials);
    }
    @Override
    public void close() throws Exception {
        client.close();
    }
    @Override
    public void asyncInvoke(String key, final ResultFuture<Tuple2<String, String>> resultFuture) throws Exception {
        // issue the asynchronous request, receive a future for result
        final Future<String> result = client.query(key);
        // set the callback to be executed once the request by the client is complete
        // the callback simply forwards the result to the result future
        //获取异步执行的结果
        CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    return result.get();
                } catch (InterruptedException | ExecutionException e) {
                    // Normally handled explicitly.
                    return null;
                }
            }
        }).thenAccept( (String dbResult) -> {
            //真正获取异步执行结果的地方是这个地方。
            resultFuture.complete(Collections.singleton(new Tuple2<>(key, dbResult)));
        });
    }
    //重写相关的timeOut的实现操作
    @Override
        public void timeout(String input, ResultFuture<Tuple2<String, String>> resultFuture) throws Exception {

        }
}
// create the original stream
DataStream<String> stream = ...;
// apply the async I/O transformation。在这个地方执行异步数据源的转换操作
DataStream<Tuple2<String, String>> resultStream =
    AsyncDataStream.unorderedWait(stream, new AsyncDatabaseRequest(), 1000, TimeUnit.MILLISECONDS, 100);
#######重要参数#########
1.Timeout:异步请求的超时时间;
2.Capacity:定义了同一个时刻可以执行请求的数量。
3.需要注意的是超时的话,对应的默认是会抛出异常和停止任务的。如果需要处理超时导致的任务重启的话
需要重写相关的或者是不产生任何的输出结果,调用如下的操作:ResultFuture.complete(Collections.emptyList())
4.输出结果的顺序:
1)无序:AsyncDataStream.unorderedWait
2)有序:AsyncDataStream.orderedWait 按照请求的顺序执行或者是超时。











