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
#############################使用线程池处理对应的callback#########
1.org.apache.flink.util.concurrent.Executors.directExecutor()获取回调的线程池
或者是使用
com.google.common.util.concurrent.MoreExecutors.directExecutor()
#需要注意的是异步操作可以在除了source以外任何的operator中操作的,不要在source中使用异步操作。
################Source的相关的概念###############
1.source包含了如下的内容:
1)split:切割组件,将对应的log或者文件等切分成为多个部分进行，这样可以进行分布式的协作以及并行读取文件
2)SourceReader:读取split之后的文件，并行读物文件和处理文件
3)SplitEnumerator:生成splits并且将对应的splits使用负载均衡的方式提交到SourceReader中来解决问题。
source是包含了上述的三个核心的组件的。flink的dataSource使用统一api的方式支持无界流的sourceUI及有界的batch stream的。
区别在于无界流的splits是不固定的，而有界stream的splits是固定的。
source中的Format定义了如何处理对应的文件。需要主要的是SourceReader需要的时候才会执行SplitEnumerator的操作的，形成逻辑上的splits的。
无界stream的source需要不断的检查是否有新的文件生成，是否需要切分新的splits的内容的。不会返回对应的no more splits的提示的。
Kafka Source：其中kafka中的一个partition对应的就是一个split。当kafka的partition发生变化的时候，SplitEnumerator就会发现对应的split增加了。
因为kafka的partition的数据是一直持续的，不会结束的，所以，对应的SourceReader是没有到达结尾的。
在使用kafka作为数据源的时候，可以将flink的并行度设置成为topic的partition的，这样的话能够充分的发挥flink的并发性能的。
source的本质有点类似于factory的相关的模式的。
source包含了如下的组件:
1)Split Enumerator
2)Source Reader
3)Split Serializer
4)Enumerator Checkpoint Serializer
SplitEnumerator:主要包含了如下的几个部分:
1)SourceReader的注册处理:
2)SourceReader的错误处理:SourceReader失败的话，需要回收对应的split assignments。后续继续处理对应的split
3)SourceEvent处理:接受source的event事件信息,sourceReader可以通过这种方式来注册source事件给SplitEnumerator，来维持一个全局的试图信息。
4)Split discovery and assignment:使用事件机制发现的新的splits，进行SourceReader注册，失败注册等。
SplitEnumerator完成上述4个问题需要借助于SplitEnumeratorContext。SplitEnumerator在整个的source中是充当了对应的大脑的角色的。
SourceReader:存在于taskManager中,使用拉取的方式从SplitEnumerator中拉取数据的。
sourceReader通过对应的pollNext拉取数据信息，pollNext对应的返回相关的拉取的状态信息的:
1)MORE_AVAILABLE:还存在更多的数据需要进行拉取操作;
2)NOTHING_AVAILABLE:当前没有数据需要拉取,后续可能存在数据需要拉取的。
3)END_OF_INPUT:数据拉取到了结尾。
SourceReader拉取数据的方式是使用时间循环的方式来拉取数据的，如非必要，不要采用阻塞的方式拉取数据。
每一个SourceReader的状态数据，都对应的维护在了SourceSplits中的,对应的在调用snapshotState()的时候会对应的触发相关的操作实现的。
这样的话，对应的允许SourceSplits注册到另外的一个SourceReaders上去的。sourceReader通过对应的SourceReaderContext来发送事件信息。
SourceReader api是一个低级别的api来让用户手动的处理splits，并且定义线程池模型来抓取和处理数据记录。为了进一步的降低难度和促进source的开发
flink官方提供了SourceReaderBase，官方强烈建议使用SourceReaderBase来完成相关的操作。
SourceReader api:需要手动的实现异步split的reading，并且是一个全程异步的操作的。然而，实际上大多数的平台是阻塞的。
例如kafkaConsumer的阻塞的poll(),分布式文件系统(hdfs,s3)等都采用的是阻塞io的操作的。为了实现兼容的异步的api,同步操作需要在独立的线程中执行的，然后将数据传递给异步的reader中实现操作的。
SplitReader是一个高级别的api,用于同步的读和拉取,例如文件读取和kafka等。核心的主类是SourceReaderBase,消费SplitReader并且在SplitReader内部创建一个拉取的线程，支持多种不同的消费线程模型。
SplitReader主要关注的是从外部系统读取数据的操作的。仅仅提供了三个方法:
1)A blocking fetch method:阻塞方法,返回对应的RecordsWithSplitIds
2)non-blocking的方法来处理splits的change变化。
3)non-blocking:非阻塞的方法用于唤醒阻塞的fetch operation操作。
SplitFetcherManager:帮助和创建了 SplitFetchers的线程池，其中每一个的SplitFetcher对应的是一个SplitReader。同时决定了如何将一个SplitReader交给一个SplitFetcher。
source阶段会完成Event Time的赋值以及Watermark的生成,在source端可以使用TimestampAssigner以及WatermarkGenerator来处理时间戳生成和水印操作
并且这种水印以及时间戳的生成建议是放在source端生成的，其他的阶段不建议使用水印和时间戳机制进行操作。
Event Timestamps生成的两种方式:
1)SourceReader携带有的source record timestamp ，方式是通过SourceOutput.collect(event, timestamp)
对应的是根据数据源相关的，比如Kafka, Kinesis, Pulsar, or Pravega
没有source端的timestamp的话，比如file的话，是不会存在这一部分的时间戳数据的。对应的数据是空的。使用之前需要观察一下。
2)使用TimestampAssigner来执行时间戳的生成操作,TimestampAssigner可以抽取到原始的时间戳以及event时间内容的，同时可以从单独的某个字段中抽取得到对应的时间戳信息的。
需要注意的是对于原始的不包含有timestamp的source而言,这个时候选择source端的timestamp的话，对应的stamp是默认数值LONG_MIN (=-9,223,372,036,854,775,808)，所有对于文件而言，我们需要自定义TimestampAssigner的。
Watermark Generation:水印生成
在streaming执行的过程中,水印生成器是活跃的。在批处理的过程中水印生成器是无效的。
flink会针对于独立的单个的split支持运行watermark generators的。这样的话，可以针对当的split可以观察水印时间过程。这样的话可以处理空闲分区拖慢整个应用的操作的。
需要注意的是,如果需要生成对应的能够基于单个的split生成相关的水印的话，需要实现对应的接口的。需要将不同splits的数据输出到不同的输出当中。这个时候可以使用ReaderOutput.createOutputForSplit来实现对应的
为每一个split创建单独的Output的操作的,需要注意的是当对应的split处理完成之后，需要手动的释放相关的Output的，可以使用如下的操作的:ReaderOutput.releaseOutputForSplit完成对应的Output的释放操作。

flink可以增加任何数量的侧向输出流。侧向输出流中的数据不要求和主输出流保持一直，并且不同的侧向输出流也不要求保持一样。
1.使用如下的方式:
OutputTag<String> outputTag = new OutputTag<String>("side-output") {};
2.可以使用如下的function对应的生成侧向输出流:
ProcessFunction
KeyedProcessFunction
CoProcessFunction
KeyedCoProcessFunction
ProcessWindowFunction
ProcessAllWindowFunction
3.下面是使用示例代码:
3-1)使用示例代码如下:
DataStream<Integer> input = ...;
final OutputTag<String> outputTag = new OutputTag<String>("side-output"){};
SingleOutputStreamOperator<Integer> mainDataStream = input
  .process(new ProcessFunction<Integer, Integer>() {
      @Override
      public void processElement(
          Integer value,
          Context ctx,
          Collector<Integer> out) throws Exception {
        // emit data to regular output
        out.collect(value);
        // emit data to side output
        ctx.output(outputTag, "sideout-" + String.valueOf(value));
      }
    });
2.获取得到侧向输出流数据:
final OutputTag<String> outputTag = new OutputTag<String>("side-output"){};
SingleOutputStreamOperator<Integer> mainDataStream = ...;
DataStream<String> sideOutputStream = mainDataStream.getSideOutput(outputTag);
3.延迟到达的数据也是可以使用侧向输出流的。
SingleOutputStreamOperator<T> result = input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .allowedLateness(<time>)
    //迟到的数据输入到侧向输出流中进行操作管理。可以将丢失的数据写入到hbase中，后续从hbase中进行进一步的消费管理的。
    .sideOutputLateData(lateOutputTag)
    .<windowed transformation>(<window function>);

#解决flink应用的参数传递的问题：
1.下面是使用flink的参数传递的示例代码:
#####从指定路径中获取属性信息#######
String propertiesFilePath = "/home/sam/flink/myjob.properties";
ParameterTool parameter = ParameterTool.fromPropertiesFile(propertiesFilePath);
#####从文件中获取属性信息#######
File propertiesFile = new File(propertiesFilePath);
ParameterTool parameter = ParameterTool.fromPropertiesFile(propertiesFile);
#####从文件流中获取属性信息#######
InputStream propertiesFileInputStream = new FileInputStream(file);
ParameterTool parameter = ParameterTool.fromPropertiesFile(propertiesFileInputStream);
#####从命令函数参数中获取#######
ParameterTool parameter = ParameterTool.fromArgs(args);
#####从文件系统中获取#######
ParameterTool parameter = ParameterTool.fromSystemProperties();
需要指定参数-Dinput=hdfs:///mydata.
在flink项目中使用参数:
ParameterTool parameters = // ...
parameter.getRequired("input");
parameter.get("output", "myDefaultValue");
parameter.getLong("expectedCount", -1L);
parameter.getNumberOfParameters();
ParameterTool parameters = ParameterTool.fromArgs(args);
int parallelism = parameters.get("mapParallelism", 2);
DataStream<Tuple2<String, Integer>> counts = text.flatMap(new Tokenizer()).setParallelism(parallelism);
甚至可以将ParameterTool当作参数,传递给对应的函数。
1.全局注册参数,后续在函数中使用的
ParameterTool parameters = ParameterTool.fromArgs(args);
// set up the execution environment
final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.getConfig().setGlobalJobParameters(parameters);
public static final class Tokenizer extends RichFlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
	ParameterTool parameters = (ParameterTool)
	   //全局获取对应的配置参数信息
	getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
	parameters.getRequired("input");
2.问题:其他的相关的工具也是可以使用的，比如对应的客户端或者是其他的也是可以支持的。
需要关注一下外部的配置参数的话,在flink中内部是如何使用的，这个需要观察一下具体的使用方式的。

#################flink中自定义函数的测试操作和实现##############
1.自定义函数和对应的测试用例1:
public class IncrementMapFunction implements MapFunction<Long, Long> {
    @Override
    public Long map(Long record) throws Exception {
        return record + 1;
    }
}
###########执行对应的测试用例操作和实现管理###########
public class IncrementMapFunctionTest {
    @Test
    public void testIncrement() throws Exception {
        // instantiate your function
        IncrementMapFunction incrementer = new IncrementMapFunction();
        // call the methods that you have implemented
        assertEquals(3L, incrementer.map(2L));
    }
}
2.测试用例二:测试自定义的udf函数功能和实现特性
public class IncrementFlatMapFunctionTest {
    @Test
    public void testIncrement() throws Exception {
        // instantiate your function
        IncrementFlatMapFunction incrementer = new IncrementFlatMapFunction();
        Collector<Integer> collector = mock(Collector.class);
        // call the methods that you have implemented
        incrementer.flatMap(2L, collector);
        //verify collector was called with the right output
        Mockito.verify(collector, times(1)).collect(3L);
    }
}
3.flink关于state以及对应的timer的测试用例使用:
使用这些测试用例的话,对应的需要一些其他的测试依赖和数据的。
OneInputStreamOperatorTestHarness (for operators on DataStreams)
KeyedOneInputStreamOperatorTestHarness (for operators on KeyedStreams)
TwoInputStreamOperatorTestHarness (for operators of ConnectedStreams of two DataStreams)
KeyedTwoInputStreamOperatorTestHarness (for operators on ConnectedStreams of two KeyedStreams)
1)下面是测试用例的:
OneInputStreamOperatorTestHarness:用于DataStreams的测试
public class StatefulFlatMapTest {
    private OneInputStreamOperatorTestHarness<Long, Long> testHarness;
    private StatefulFlatMap statefulFlatMapFunction;
    @Before
    public void setupTestHarness() throws Exception {
        //instantiate user-defined function
        statefulFlatMapFunction = new StatefulFlatMapFunction();
        // wrap user defined function into a the corresponding operator
        testHarness = new OneInputStreamOperatorTestHarness<>(new StreamFlatMap<>(statefulFlatMapFunction));
        // optionally configured the execution environment
        testHarness.getExecutionConfig().setAutoWatermarkInterval(50);
        // open the test harness (will also call open() on RichFunctions)
        testHarness.open();
    }
    @Test
    public void testingStatefulFlatMapFunction() throws Exception {
        //push (timestamped) elements into the operator (and hence user defined function)
        testHarness.processElement(2L, 100L);
        //trigger event time timers by advancing the event time of the operator with a watermark
        testHarness.processWatermark(100L);
        //trigger processing time timers by advancing the processing time of the operator directly
        testHarness.setProcessingTime(100L);
        //retrieve list of emitted records for assertions
        assertThat(testHarness.getOutput(), containsInExactlyThisOrder(3L));
        //retrieve list of records emitted to a specific side output for assertions (ProcessFunction only)
        //assertThat(testHarness.getSideOutput(new OutputTag<>("invalidRecords")), hasSize(0))
    }
}
2)测试用例二:
OneInputStreamOperatorTestHarness:用于测试dataStream进行测试操作
public class StatefulFlatMapFunctionTest {
    private OneInputStreamOperatorTestHarness<String, Long, Long> testHarness;
    private StatefulFlatMap statefulFlatMapFunction;
    @Before
    public void setupTestHarness() throws Exception {
        //instantiate user-defined function
        statefulFlatMapFunction = new StatefulFlatMapFunction();
        // wrap user defined function into a the corresponding operator
        testHarness = new KeyedOneInputStreamOperatorTestHarness<>(new StreamFlatMap<>(statefulFlatMapFunction), new MyStringKeySelector(), Types.STRING);
        // open the test harness (will also call open() on RichFunctions)
        testHarness.open();
    }
}
3)测试用例三:
org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorTest
org.apache.flink.streaming.api.functions.sink.filesystem.LocalStreamingFileSinkTest
4)测试用户定义的ProcessFunction
public static class PassThroughProcessFunction extends ProcessFunction<Integer, Integer> {
	@Override
	public void processElement(Integer value, Context ctx, Collector<Integer> out) throws Exception {
        out.collect(value);
	}
}
###测试对应的自定义的ProcessFunction函数实现相关的操作实现的。可以使用这个ProcessFunctionTestHarnesses更加轻易的进行测试操作
可以用于测试KeyedProcessFunction,KeyedCoProcessFunction,BroadcastProcessFunction,
public class PassThroughProcessFunctionTest {
    @Test
    public void testPassThrough() throws Exception {
        //instantiate user-defined function
        PassThroughProcessFunction processFunction = new PassThroughProcessFunction();
        // wrap user defined function into a the corresponding operator
        OneInputStreamOperatorTestHarness<Integer, Integer> harness = ProcessFunctionTestHarnesses
        	.forProcessFunction(processFunction);
        //push (timestamped) elements into the operator (and hence user defined function)
        harness.processElement(1, 10);
        //retrieve list of emitted records for assertions
        assertEquals(harness.extractOutputValues(), Collections.singletonList(1));
    }
}
###测试对应的flink的job操作
1)需要引入相关的依赖:
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-test-utils</artifactId>
    <version>1.15.0</version>
    <scope>test</scope>
</dependency>
2)测试的项目和代码样例:
public class IncrementMapFunction implements MapFunction<Long, Long> {
    @Override
    public Long map(Long record) throws Exception {
        return record + 1;
    }
}
####对应的执行代码用例操作和实现#####
public class ExampleIntegrationTest {
     @ClassRule
     public static MiniClusterWithClientResource flinkCluster =
         new MiniClusterWithClientResource(
             new MiniClusterResourceConfiguration.Builder()
                 .setNumberSlotsPerTaskManager(2)
                 .setNumberTaskManagers(1)
                 .build());
    @Test
    public void testIncrementPipeline() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // configure your test environment
        env.setParallelism(2);
        // values are collected in a static variable
        CollectSink.values.clear();
        // create a stream of custom elements and apply transformations
        env.fromElements(1L, 21L, 22L)
                .map(new IncrementMapFunction())
                .addSink(new CollectSink());
        // execute
        env.execute();
        // verify your results
        assertTrue(CollectSink.values.containsAll(2L, 22L, 23L));
    }
    // create a testing sink
    private static class CollectSink implements SinkFunction<Long> {
        // must be static
        public static final List<Long> values = Collections.synchronizedList(new ArrayList<>());
        @Override
        public void invoke(Long value, SinkFunction.Context context) throws Exception {
            values.add(value);
        }
    }
}
使用的建议:
1.source以及对应的sink要求是可以配置的,不要写死在代码中。
2.建议本地测试的时候使用并行度为1的进行测试。
parallelism > 1
3.推荐使用@ClassRule而不是@Rule进行测试操作。
4.测试flink集群的checkpoint以及对应的抛出异常的时候的处理操作的话,可以在mini cluster中启用checkpoint
测试flink相关的机制和确保机制的实现，需要本地上执行确保想要的和最终输出的结果是一直的结果的。
flink的测试需要加强和关注相关的特性的,这个方式是一个很重要的特性的。加强flink代码的本地测试功能和输出操作控制实现。

#########lamada表达式的学习和理解#########
1.lamada表达式的限制因素:
lamada表达式存在的问题是:lamada表达式在运行的时候会进行泛型擦除操作。flink中关于lamada表达式的操作会需要指定对应的返回值的类型的
1)示例代码如下:
input.flatMap((Integer number, Collector<String> out) -> {
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < number; i++) {
        builder.append("a");
        out.collect(builder.toString());
    }
})
// provide type information explicitly
//指定返回值的类型，原因在于flatMap的返回值是无法推断的。
//void flatMap(T value, Collector<O> out) throws Exception;
.returns(Types.STRING)
// prints "a", "a", "aa", "a", "aa", "aaa"
.print();
2)示例代码二:
env.fromElements(1, 2, 3)
    .map(i -> Tuple2.of(i, i))    // no information about fields of Tuple2
    .print();
// use the explicit ".returns(...)"
//解决这种办法的措施包括如下的两种情况:
1)使用returns显式的指定返回数值的类型。
2)使用内部类的方式指定返回值的类型。
env.fromElements(1, 2, 3)
    .map(i -> Tuple2.of(i, i))
    .returns(Types.TUPLE(Types.INT, Types.INT))
    .print();
// use a class instead
env.fromElements(1, 2, 3)
    .map(new MyTuple2Mapper())
    .print();
public static class MyTuple2Mapper extends MapFunction<Integer, Tuple2<Integer, Integer>> {
    @Override
    public Tuple2<Integer, Integer> map(Integer i) {
        return Tuple2.of(i, i);
    }
}
// use an anonymous class instead
env.fromElements(1, 2, 3)
    .map(new MapFunction<Integer, Tuple2<Integer, Integer>> {
        @Override
        public Tuple2<Integer, Integer> map(Integer i) {
            return Tuple2.of(i, i);
        }
    })
    .print();
// or in this example use a tuple subclass instead
env.fromElements(1, 2, 3)
    .map(i -> new DoubleTuple(i, i))
    .print();
public static class DoubleTuple extends Tuple2<Integer, Integer> {
    public DoubleTuple(int f0, int f1) {
        this.f0 = f0;
        this.f1 = f1;
    }
}

################flink运行时的配置因素################
1)执行期的配置:
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
ExecutionConfig executionConfig = env.getConfig();
###设置失败的时候重新执行的次数,在程序中不建议使用的,推荐使用
getNumberOfExecutionRetries() / setNumberOfExecutionRetries(int numberOfExecutionRetries)
###指定任务重启的时间间隔,可以和全面的getNumberOfExecutionRetries()等配合起来一起使用的,不建议使用,推荐使用重启策略来执行操作。
getExecutionRetryDelay() / setExecutionRetryDelay(long executionRetryDelay)
#设置运行模式,默认是PIPELINED的
getExecutionMode() / setExecutionMode()
#使用kryo序列化方式
enableForceKryo() / disableForceKryo
#使用avro序列化方式
enableForceAvro() / disableForceAvro().
#允许对象重用,会提升性能,但是会报错的。
enableObjectReuse() / disableObjectReuse()
#获取或者是设置全局参数,可以传递全部的参数的，因为这个是可以在所有的用户自定义的函数中使用的。
getGlobalJobParameters() / setGlobalJobParameters()
#注册指定类型的序列化器作为kryo的序列化器
addDefaultKryoSerializer(Class<?> type, Serializer<?> serializer)
#注册指定类型为kyro的序列化器
addDefaultKryoSerializer(Class<?> type, Class<? extends Serializer<?>> serializerClass)
registerTypeWithKryoSerializer(Class<?> type, Serializer<?> serializer)
registerKryoType(Class<?> type)
registerPojoType(Class<?> type)
#设置连续取消任务的时间间隔。
setTaskCancellationInterval (long interval)
具体的特别的参数,可以参考下面的网址:https://my.oschina.net/lfxu/blog/5444559
可以使用这个来传递全局的参数的。setGlobalJobParameters


#flink项目集群环境运行
1.需要使用如下的语句获取执行环境,否则对应的是local模式的
StreamExecutionEnvironment.getExecutionEnvironment()
2.需要指定main-class以及对应的jar文件的
main-class 或者是program-class,两者都存在的时候会优先使用program-class。


#flink的并行执行程序操作
1.如果想要使用savePoint的话,可以考虑设置最大的并行度。
并行度的设置方式:
1)操作符级别:operator
final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
DataStream<String> text = [...];
DataStream<Tuple2<String, Integer>> wordCounts = text
    .flatMap(new LineSplitter())
    .keyBy(value -> value.f0)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .sum(1).setParallelism(5);
wordCounts.print();
env.execute("Word Count Example");
2)执行环境级别:
flink默认的话会给对应的operator设置相关的并发级别的.可以自定义相关的并发度级别来实现覆盖默认配置操作
final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
###在环境级别设置并行度
env.setParallelism(3);
DataStream<String> text = [...];
DataStream<Tuple2<String, Integer>> wordCounts = [...];
wordCounts.print();
env.execute("Word Count Example");
###客户端级别设置并行度 Client Level
使用这种方式,可以实现flink任务的工程化创建和提交操作实现的.yarn部署方式的操作存在很大的局限性的。
1)使用-p参数进行设置
./bin/flink run -p 10 ../examples/*WordCount-java*.jar
//这种情况的话,对应的可以自定义提交很多的flink的任务的,而不是需要一个个的配置任何和进行调度操作的
try {
    PackagedProgram program = new PackagedProgram(file, args);
    //对应的获取jobManager的地址的,提交相关的client的配置信息的
    InetSocketAddress jobManagerAddress = RemoteExecutor.getInetFromHostport("localhost:6123");
    Configuration config = new Configuration();
    Client client = new Client(jobManagerAddress, config, program.getUserCodeClassLoader());
    // set the parallelism to 10 here
    client.run(program, 10, true);
} catch (ProgramInvocationException e) {
    e.printStackTrace();
}
###系统级别的并行度
1.配置文件中配置:./conf/flink-conf.yaml 配置并行度参数 parallelism.default
2.设置最大程度的并行度参数:setMaxParallelism()
operatorParallelism + (operatorParallelism / 2) 初略的可以设置并行度指标,128~32768


###CEP:复杂事件匹配模式,在无界的数据流中发现重要数据.flink cep默认的是不会和flink clutser一起的。
flink cep的使用需要满足如下的条件:
1)引入相关的依赖:
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-cep</artifactId>
    <version>1.15.0</version>
</dependency>
2)定义对应的pattern信息:
DataStream<Event> input = ...;
//定义搜索匹配的pattern信息,pattern对应的是一组链条。
Pattern<Event, ?> pattern = Pattern.<Event>begin("start").where(
        new SimpleCondition<Event>() {
            @Override
            public boolean filter(Event event) {
                return event.getId() == 42;
            }
        }
    ).next("middle").subtype(SubEvent.class).where(
        new SimpleCondition<SubEvent>() {
            @Override
            public boolean filter(SubEvent subEvent) {
                return subEvent.getVolume() >= 10.0;
            }
        }
    ).followedBy("end").where(
         new SimpleCondition<Event>() {
            @Override
            public boolean filter(Event event) {
                return event.getName().equals("end");
            }
         }
    );
PatternStream<Event> patternStream = CEP.pattern(input, pattern);
DataStream<Alert> result = patternStream.process(
    new PatternProcessFunction<Event, Alert>() {
        @Override
        public void processMatch(
                Map<String, List<Event>> pattern,
                Context ctx,
                Collector<Alert> out) throws Exception {
            out.collect(createAlertFrom(pattern));
        }
    });
#核心或者是关键因素在于定义pattern模型,pattern模型对应的是由多个简单的模型来组成的。
其实质是定义pattern的graph结构的,使用条件可以将一个模式传递给另外的一个模式。需要注意的是模式的名称不能包含:
1.首先定义简单的模式;
a b+ c? d: a后面存在1到多个的b,0到1个的c,然后是一个d
pattern.oneOrMore():flink表示的是可以出现1或者多个;
pattern.times(#ofTimes):出现固定次数
pattern.times(#fromTimes, #toTimes):出现指定范围次数
pattern.greedy():循环条件出现贪婪模式,但是对于group而言,不存在
pattern.optional():对应的表示可有可无。
下面是使用示例:
// expecting 4 occurrences  出现4次
start.times(4);
// expecting 0 or 4 occurrences  出现4次或者不出现，即0次或者4次
start.times(4).optional();
// expecting 2, 3 or 4 occurrences 出现2-4之间的，包括2,3,4
start.times(2, 4);
// expecting 2, 3 or 4 occurrences and repeating as many as possible
start.times(2, 4).greedy();
// expecting 0, 2, 3 or 4 occurrences
start.times(2, 4).optional();
// expecting 0, 2, 3 or 4 occurrences and repeating as many as possible
start.times(2, 4).optional().greedy();
// expecting 1 or more occurrences
start.oneOrMore();
// expecting 1 or more occurrences and repeating as many as possible
start.oneOrMore().greedy();
// expecting 0 or more occurrences
start.oneOrMore().optional();
// expecting 0 or more occurrences and repeating as many as possible
start.oneOrMore().optional().greedy();
// expecting 2 or more occurrences
start.timesOrMore(2);
// expecting 2 or more occurrences and repeating as many as possible
start.timesOrMore(2).greedy();
// expecting 0, 2 or more occurrences
start.timesOrMore(2).optional()
// expecting 0, 2 or more occurrences and repeating as many as possible
start.timesOrMore(2).optional().greedy();
####下面定义conditions
pattern.where()
pattern.or()
pattern.until()
##使用示例代码
middle.oneOrMore()
    //限制初始类型的子类型,这种就是在初始类型中继续对其包含的子类型进行进一步的过滤操作
    .subtype(SubEvent.class)
    .where(new IterativeCondition<SubEvent>() {
        @Override
        public boolean filter(SubEvent value, Context<SubEvent> ctx) throws Exception {
            //名称以foo开始
            if (!value.getName().startsWith("foo")) {
                return false;
            }
            double sum = value.getPrice();
            //获取之前符合条件的元素,计算之前的元素和当前的元素的求和数值
            for (Event event : ctx.getEventsForPattern("middle")) {
                sum += event.getPrice();
            }
            //比较之前的数值和当前的数值的求和是否小于5.0级别。
            return Double.compare(sum, 5.0) < 0;
        }
    });
    #简单条件
    start.where(new SimpleCondition<Event>() {
        @Override
        public boolean filter(Event value) {
            return value.getName().startsWith("foo");
        }
    });
###需要注意的是顺序调用where条件的话,对应的是and操作逻辑的
需要实现or操作逻辑的话,可以使用or()的条件实现操作。
#下面是使用or条件实现的操作，对应的可以实现条件的or组合操作
pattern.where(new SimpleCondition<Event>() {
    @Override
    public boolean filter(Event value) {
        return ...; // some condition
    }
}).or(new SimpleCondition<Event>() {
    @Override
    public boolean filter(Event value) {
        return ...; // or condition
    }
});
#条件停止操作,循环的stop条件
#常见的几种条件
1)where条件
pattern.where(new IterativeCondition<Event>() {
    @Override
    public boolean filter(Event value, Context ctx) throws Exception {
        return ...; // some condition
    }
});
2)where条件之后是or条件
pattern.where(new IterativeCondition<Event>() {
    @Override
    public boolean filter(Event value, Context ctx) throws Exception {
        return ...; // some condition
    }
}).or(new IterativeCondition<Event>() {
    @Override
    public boolean filter(Event value, Context ctx) throws Exception {
        return ...; // alternative condition
    }
});
3)until条件:
pattern.oneOrMore().until(new IterativeCondition<Event>() {
    @Override
    public boolean filter(Event value, Context ctx) throws Exception {
        return ...; // alternative condition
    }
});
4)匹配子类型
pattern.subtype(SubEvent.class);
5)oneOrMore():模式可以出现一到多次的结果
如果程序中使用了 oneOrMore 或者 oneOrMore().optional()方法，则必须 指定终止条件，否则模式中的规则会一直循环下去
6)timesOrMore(#times):出现至少多少次数
timesOrMore(#times)
7)times(#ofTimes):精确匹配多少次
8)times(#fromTimes, #toTimes):出现指定范围的次数
9)optional():模式是可选的。
10)贪婪模式:greedy()
pattern.oneOrMore().greedy();
pattern.oneOrMore().optional();
2.定义复杂的模式;
1)组合条件的编写需要开始以初始模式开始
#创建初始模式
Pattern<Event, ?> start = Pattern.<Event>begin("start");
flink cep的一致性条件:
1)Strict Contiguity:严格一致性。元素一个接着一个的匹配,中间不允许出现不匹配的元素
2)Relaxed Contiguity:可以忽略匹配元素中间的不匹配元素,不匹配元素是存在的
3)Non-Deterministic Relaxed Contiguity:进一步的放宽匹配条件,忽略匹配条件的附件条件
###模式如下:
1)next(), for strict,
2)followedBy(), for relaxed, and
3)followedByAny(), for non-deterministic relaxed contiguity.
4)notNext(),不直接相连
5)notFollowedBy():事件不会介于两者之间
notFollowedBy()不用用于模式的结尾
not模式之前不能存在有可选模式
// strict contiguity  严格模式
Pattern<Event, ?> strict = start.next("middle").where(...);
// relaxed contiguity 松弛模式
Pattern<Event, ?> relaxed = start.followedBy("middle").where(...);
// non-deterministic relaxed contiguity
Pattern<Event, ?> nonDetermin = start.followedByAny("middle").where(...);
// NOT pattern with strict contiguity
Pattern<Event, ?> strictNot = start.notNext("not").where(...);
// NOT pattern with relaxed contiguity
Pattern<Event, ?> relaxedNot = start.notFollowedBy("not").where(...);
#下面使用示例代码来说明对应的逻辑匹配规则:
输入元素:"a", "c", "b1", "b2"
模式 "a b"
strict:要求a后面是b,无法匹配元素
Relaxed:要求a之后是b,中间允许出现不匹配的元素,符合条件的是 "a  b1"
Non-Deterministic Relaxed: a之后出现b,对a和b之间不做任何限制,对应的匹配到的元素是"a b1"以及"a b2"
这是一种极为松弛的情况,可以忽略相关的内容。
pattern.within():在指定时间之内必须出现。一个模式序列只能有一个within语句的,当出现多个within()之后,
会取最小的一个模式来作为最终的within的结果
next.within(Time.seconds(10));  #示例情况如下

输入元素:"a", "b1", "d1", "b2", "d2", "b3" "c"
输入模式:"a b+ c"
strict模式:输出结果为 "a b3 c"
Relaxed模式:{a b1 c}, {a b1 b2 c}, {a b1 b2 b3 c}, {a b2 c}, {a b2 b3 c}, {a b3 c}  至少保留了先后顺序
Non-Deterministic Relaxed:{a b1 c}, {a b1 b2 c}, {a b1 b3 c}, {a b1 b2 b3 c}, {a b2 c}, {a b2 b3 c}, {a b3 c} 匹配中间可以缺少元素。还可以不保持顺序来组合
####
oneOrMore()以及times()对应的是relax模式的,如果想要严格模式的话,需要使用consecutive()
consecutive():对应的会在匹配元素之间增加严格连续性的检查的，其结果和next是相似的.
###下面这段代码描述的约束规则是如下的：
模式:"c a+ b" 其中a和b之间是严格的,c和a之间没有限制
输入:C D A1 A2 A3 D A4 B
匹配结果如下:
1.使用consecutive()输出结果如下:要求A是连续的。对应的其实还是relaxed模式的,只是存在约束的。
{C A1 B}, {C A1 A2 B}, {C A1 A2 A3 B},后面的A3以及A4不是之间联系的，存在间隔D
2.不使用consecutive()的数据结果如下:follow会忽略掉不匹配的元素的。
{C A1 B}, {C A1 A2 B}, {C A1 A2 A3 B}, {C A1 A2 A3 A4 B}.
Pattern.<Event>begin("start").where(new SimpleCondition<Event>() {
  @Override
  public boolean filter(Event value) throws Exception {
    return value.getName().equals("c");
  }
})
.followedBy("middle").where(new SimpleCondition<Event>() {
  @Override
  public boolean filter(Event value) throws Exception {
    return value.getName().equals("a");
  }
}).oneOrMore().consecutive()
.followedBy("end1").where(new SimpleCondition<Event>() {
  @Override
  public boolean filter(Event value) throws Exception {
    return value.getName().equals("b");
  }
});
#下面的模式如下:
模式:"c a+ b"
输入:C D A1 A2 A3 D A4 B
allowCombinations:non-deterministic relaxed contiguitym,对应的是左侧的连续性的。
1)不使用allowCombinations：对应的是followBy。可以理解为扩展的操作实现，不断的扩展
{C,A1,B},{C,A1,A2,B},{C,A1,A2,A3,B},{C,A1,A2,A3,A4,B}
2)使用allowCombinations,对应的是non-deterministic relaxed。
输出结果如下:对应的可以理解为一个组合的操作。随机组合操作的。
{C,A1,B},{C,A1,A2,B},{C,A1,A3,B},{C,A1,A4,B},{C,A1,A2,A3,B},{C,A1,A2,A4,B}{C,A1,A2,A3,A4,B},
Pattern.<Event>begin("start").where(new SimpleCondition<Event>() {
  @Override
  public boolean filter(Event value) throws Exception {
    return value.getName().equals("c");
  }
})
.followedBy("middle").where(new SimpleCondition<Event>() {
  @Override
  public boolean filter(Event value) throws Exception {
    return value.getName().equals("a");
  }
}).oneOrMore().allowCombinations()
.followedBy("end1").where(new SimpleCondition<Event>() {
  @Override
  public boolean filter(Event value) throws Exception {
    return value.getName().equals("b");
  }
});
#####更加高级的特性:模式pattern的组合操作
1.模式组合之间可以使用如下的操作符号:begin, followedBy, followedByAny and next
//定义模式一:
Pattern<Event, ?> start = Pattern.begin(
    Pattern.<Event>begin("start").where(...).followedBy("start_middle").where(...)
);
//定义模式二:
// strict contiguity
Pattern<Event, ?> strict = start.next(
    Pattern.<Event>begin("next_start").where(...).followedBy("next_middle").where(...)
).times(3);
//定义模式三:
// relaxed contiguity
Pattern<Event, ?> relaxed = start.followedBy(
    Pattern.<Event>begin("followedby_start").where(...).followedBy("followedby_middle").where(...)
).oneOrMore();
//定义模式四:
// non-deterministic relaxed contiguity
Pattern<Event, ?> nonDetermin = start.followedByAny(
    Pattern.<Event>begin("followedbyany_start").where(...).followedBy("followedbyany_middle").where(...)
).optional();

###pattern模式
#begin(#name) 定义一个起始的pattern
Pattern<Event, ?> start = Pattern.<Event>begin("start");
begin(#pattern_sequence) 定义一个起始的pattern
Pattern<Event, ?> start = Pattern.<Event>begin(
    //对应的是pattern_sequence序列信息
    Pattern.<Event>begin("start").where(...).followedBy("middle").where(...)
);
next(#name):严格模式追加后续的pattern模式
Pattern<Event, ?> next = start.next("middle");
next(#pattern_sequence):严格模式追加一个pattern模式
Pattern<Event, ?> next = start.next(
    //内部定义的strict模式的序列信息
    Pattern.<Event>begin("start").where(...).followedBy("middle").where(...)
);
followedBy(#name):relaxed contiguity追加的序列信息
Pattern<Event, ?> followedBy = start.followedBy("middle");
Pattern<Event, ?> followedBy = start.followedBy(
    //followedBy追加的模式序列信息
    Pattern.<Event>begin("start").where(...).followedBy("middle").where(...)
);
followedByAny(#name):non-deterministic relaxed contiguity 追加模式序列操作
Pattern<Event, ?> followedByAny = start.followedByAny("middle");
followedByAny(#pattern_sequence):同上对应的是一个序列的
Pattern<Event, ?> next = start.next(
    //non-deterministic relaxed contiguity 追加一个序列
    Pattern.<Event>begin("start").where(...).followedBy("middle").where(...)
);
notNext():追加负向模式,和next类似，要求后续的模式不能是对应的模式。
Pattern<Event, ?> notNext = start.notNext("not");
notFollowedBy():负向模式,要求relaxed contiguity后面不能是某些模式
Pattern<Event, ?> notFollowedBy = start.notFollowedBy("not");
within(time):指定时间间隔之内,下一个模式需要满足条件
pattern.within(Time.seconds(10));

######################匹配跳过策略######################
skip策略:
1.NO_SKIP:不跳过任何的匹配,任何的匹配都会产生结果;
2.SKIP_TO_NEXT:
3.SKIP_PAST_LAST_EVENT:
4.SKIP_TO_FIRST:
5.SKIP_TO_LAST:
下面根据指定的输入来比较几种模式的相关的区别信息:
输入信息:b1 b2 b3 c
模式信息:b+ c
no_skip的结果:{b1,b2,b3,c},{b2,b3,c},{b3,c}.不会跳过任何的输出结果的。
SKIP_TO_NEXT的结果:{b1,b2,b3,c},{b2,b3,c},{b3,c}，不会跳过任何的输出结果。
SKIP_PAST_LAST_EVENT:删除部分匹配的结果,{b1,b2,b3,c}
对应的跳过策略的话,需要参考文档https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/libs/cep/钻研的。
####指定匹配策略和是实现操作
AfterMatchSkipStrategy skipStrategy = ...;
Pattern.begin("patternName", skipStrategy);
AfterMatchSkipStrategy.skipToFirst(patternName).throwExceptionOnMiss();
#运用cep理论实现操作
1)指定dataStream,指定pattern,指定EventComparator对应的用于元素的排序操作实现。
DataStream<Event> input = ...;
Pattern<Event, ?> pattern = ...;
EventComparator<Event> comparator = ...; // optional
PatternStream<Event> patternStream = CEP.pattern(input, pattern, comparator);


#从模式中筛选数据
1.实现PatternProcessFunction用于处理对应的模式数据
class MyPatternProcessFunction<IN, OUT> extends PatternProcessFunction<IN, OUT> {
    @Override
    public void processMatch(Map<String, List<IN>> match, Context ctx, Collector<OUT> out) throws Exception;
        //match中的key对应的是match的pattern的key的,List对应的是匹配到的结果的。
        IN startEvent = match.get("start").get(0);
        IN endEvent = match.get("end").get(0);
        out.collect(OUT(startEvent, endEvent));
    }
}
2.处理pattern超时的相关的问题,可以将超时的元素输出到sideOutput中的
class MyPatternProcessFunction<IN, OUT> extends PatternProcessFunction<IN, OUT> implements TimedOutPartialMatchHandler<IN> {
    //对应的处理匹配到的元素
    @Override
    public void processMatch(Map<String, List<IN>> match, Context ctx, Collector<OUT> out) throws Exception;
    }
    //处理延时到达的元素
    @Override
    public void processTimedOutMatch(Map<String, List<IN>> match, Context ctx) throws Exception;
        IN startEvent = match.get("start").get(0);
        ctx.output(outputTag, T(startEvent));
    }
}
#收集延迟到达的数据。
PatternStream<Event> patternStream = CEP.pattern(input, pattern);
OutputTag<String> lateDataOutputTag = new OutputTag<String>("late-data"){};
SingleOutputStreamOperator<ComplexEvent> result = patternStream
    .sideOutputLateData(lateDataOutputTag)
    .select(
        new PatternSelectFunction<Event, ComplexEvent>() {...}
    );
DataStream<String> lateData = result.getSideOutput(lateDataOutputTag);
##对应的执行相关的元素的调用操作实现
TimeContext.currentProcessingTime 对应的获取当前的处理时间。当前处理的时间是优于System.currentTimeMillis()时间戳的设置的
####下面对应的是一个典型的使用示例代码和实践操作上下####
StreamExecutionEnvironment env = ...;
DataStream<Event> input = ...;
DataStream<Event> partitionedInput = input.keyBy(new KeySelector<Event, Integer>() {
	@Override
	public Integer getKey(Event value) throws Exception {
		return value.getId();
	}
});
//定义pattern操作,start---->middle------>end执行操作实现
Pattern<Event, ?> pattern = Pattern.<Event>begin("start")
	.next("middle").where(new SimpleCondition<Event>() {
		@Override
		public boolean filter(Event value) throws Exception {
			return value.getName().equals("error");
		}
	}).followedBy("end").where(new SimpleCondition<Event>() {
		@Override
		public boolean filter(Event value) throws Exception {
			return value.getName().equals("critical");
		}
	}).within(Time.seconds(10));
PatternStream<Event> patternStream = CEP.pattern(partitionedInput, pattern);
DataStream<Alert> alerts = patternStream.select(new PatternSelectFunction<Event, Alert>() {
	@Override
	public Alert select(Map<String, List<Event>> pattern) throws Exception {
		return createAlert(pattern);
	}
});
##############分析和处理状态数据##############
1.使用flink状态相关的api
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-state-processor-api</artifactId>
    <version>1.15.0</version>
</dependency>
2.flink 有状态任务的组成
flink的job对应的是由多个operator组成的,可能包含了一个或者多个的source operator,一些实际执行处理的operator,
然后是一到多个的sink operator的操作符的。
每一个operator都可以运行一个或者是多个的task,并且每一个的operator对应的都可以和一个或者是多种类型的state协作操作。
每一个的operator对应的都是包含了一个或者多个的operator的state的信息的,对应的使用的是list的数据结构最终包含到了操作符的任务列表中的。
如果对应的operator对应的是keyed stream的话,他可以包含0,1或者是多个的keyed state的,对应的是作用于对应个的key上面的。
keyed stream对应的是一个分布式的keyed-value的map结构的。
对应的flink的operator对应的是根据task来实现绑定的，我们可以将对应的savepoint或者是keypoint对应的理解为一个数据库的。
每一个操作符对应的是一个namespace或者称之为命名空间的。operator的每一个state对应的映射成为namespace下面的一张表的，表中有且仅有单列数据
并且该列数据中保存了该operator对应的所有的task对应的state状态数据信息。
所有的keyed state对应的包含了两列数据，一个对应的是key,另外对应的是key对应的keyed state的数据的。

########################设计对应的逻辑体系和结构体系的########################
整个的逻辑体系设计是如下的:
1.operator对应的是数据库namespace;
2.operator下面的每一个的state对应的是一个table的;
3.table里面对应的是如下规定的:
对于不是keyed-stream的话,对应的只会包含一列数据的
对于keyed stream的话,结构包含了两个数据的，一个对应的是key,另外对应的是key相关联的state的信息的。
需要注意的是单个的state的operator对于该operator的所有的task是可见的,对于其他的operator是不可见的，也就是说
不存在对应的跨越namespace的state分享的,所以不同的operator的话,对应的是不能够共享state信息的。
需要关注的是flink的operator的自定义的uid的生成策略和相关实现机制,怎么生成operator的uid信息。
需要注意的是操作符operator状态对应的是non-keyed state的。

keyed state以及partitioned state对应的都是和key相关的,
使用SavepointWriter向state中写入数据的话,对应的是需要batch模式的数据和要求的。
需要注意的是flink写入hadoop的文件的话,是需要遵从相关的hadoopInputFormat的概念的,所以,写入文件需要遵从相关的输入格式的
flink对应的写入hadoop的文件的话，需要的是hadoop的bucket的相关的api机制来实现的。
flink接入新的数据源,对应的只需要接入相关InputFormat的。

flink以及spark相关的扩展操作可以参考对应的很多的第三方的相关的组件和信息的,这个是值得关注的。
1.flink-connectors;
2.Apache bahir相关的第三方的扩展操作实现的；
3.flink使用异步数据源访问外部的数据库或者是web服务器的接口实现相关的查询操作实现的。对于比较耗时的异步的数据源的话
对应的可以将数据源转换成为异步的数据源实现调用操作实现的
对于需要的第三方的接口调用的话，可以使用上面的异步调用的方式外加上对应的状态数据的存储和保存的,是可以实现相关的优秀的机制的。
我们可以将这种对数据库的查询或者是调用的第三方的接口转换成为异步的数据源实现操作处理逻辑的。
在对应的RichAsyncFunction的function中实现相关的异步调用机制的。

需要注意的是flink的savepoint对应的是处理flink任务的暂停,恢复等的操作，设置savepoint的时候,肯定是存在对应的flink的job的相关的信息的。
hudi的java源码中对应的是存在java-client的相关的信息的,使用java操作hudi的相关的代码可以参考对应的hudi的client的源代码实现相关的操作的。
hudi本身也是基于大数据hadoop上面的一个管理层的,其本质上没有很明显的太大的差别的。




























