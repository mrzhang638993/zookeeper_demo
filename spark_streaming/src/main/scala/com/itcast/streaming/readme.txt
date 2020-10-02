spark  streaming 的编程模型对应的是Dstream的。是一个流式的数据的。
spark的编程模型对应的是rdd的。

spark  streaming线程至少需要两个线程：
1.接收server端的数据
2.执行执行rdd的操作


spark  streaming是一个小批量的处理引擎的。
小批量的划分依据：
1.Spark Streaming 并不是实时流, 而是按照时间切分小批量, 一个一个的小批量处理

DStream对应的是针对于RDD进行处理的。每一个RDD称之为一个批次的数据的。

