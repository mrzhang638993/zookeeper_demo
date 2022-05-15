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











