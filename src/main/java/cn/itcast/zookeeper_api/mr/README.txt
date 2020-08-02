etl阶段的3步走：
1.首先需要对于数据进行清洗操作，WeblogPreProcess对应的实现数据的清洗操作实现；
2.第二步，需要对于清理之后的数据进行pageviews的点击流模型进行创建操作的，创建会话id，筛选出来关键的数据的
3.第三步：基于在第二步的基础上，将第二步的数据进行进一步的数据处理，以sessionid作为基础创建进行visits点击流模型的创建操作。

前置操作
1.项目的规划阶段，设置网站埋点需要的数据，包括相关的字段，这个过程中，需要对于nginx的源码进行操作，增加对于中文的支持的
2.使用flume的相关的工具，收集日志实现相关的数据操作，将文件上传到hdfs中
3.针对于flume上传的数据，需要进行etl数据分析操作，进行上面的etl阶段的3步走。创建相关的pageviews模型和vists模型。

#  需要注意的是业务输出对象的bean，需要重写tostring方法逻辑实现操作。
#  mapreduce 对应的是离线分析操作的，spark以及flink对应的是实时分析操作的。
网站分析的pageviews模型的关键点:
1.识别单个客户的每一步操作，操作持续的时间
2.创建不同的sessionid，需要根基时间创建sessionid实现创建操作(业界默认的是30分钟)。
对应的map-reduce阶段的key以及value的类型如下：
k1   LongWritable(文本的偏移量)    v1 Text
k2   Text(ip地址，可以是其他的识别单个客户的内容), WebLogBean(自定义的bean，包含业务数据和sessonid)

网站分析的vists的模型的关键点
1.识别会话，单个会话的所有的数据全部保存在一起进行排序操作，根据时间的升序排列
2.计算单个会话的持续时间，包含会话的起始页面和会话的结束页面
对应的mapreduce阶段的key以及value的类型如下：
K1 LongWritable(文本的偏移量),  v1 Text(行文本记录),
k2 Text(sessionid),    v2 PageViewsBean(自定义的业务输出对象)


# 逻辑上这个flume上传操作可以一直进行的。系统埋点数据的。
# mr操作需要交给定义调度任务执行的，执行mapreduce操作的话，对应的是昨天的日志文件数据的。需要结合oozie执行定时调度操作的。




