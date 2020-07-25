package cn.itcast.zookeeper_api.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * mapreduced的 wordCount的代码执行计算逻辑
 */

/**
 * mapper阶段对应的相关的mapper方法,使用的都是自己的数据结构。解决序列化的问题，自己定义了自己的一套类型
 * KEYIN,  K1的类型
 * VALUEIN, V1的类型
 * KEYOUT,  K2的类型
 * VALUEOUT, V2的类型
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    /**
     * 重写map方法，将（k1,v1）转化为(k2,v2)
     * key :k1  对应的是行偏移量
     * value:v1  每一行的文本数据
     * context： 对应的是上下文对象，将各个流程之间进行联系起来。context实现流转操作的。
     * 怎么将(k1,v1) 转化为（k2,v2）
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //  (k1,v1) 转化为(k2,v2)，对应的转化为下一个阶段执行操作
        String[] values = value.toString().split(",");
        Text text = new Text();
        LongWritable write = new LongWritable();
        for (String word : values) {
            //  数据发送给下一个节点
            text.set(word);
            write.set(1);
            context.write(text, write);
        }
    }
}
