package cn.itcast.zookeeper_api.exce.exce2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 执行去重的逻辑操作和代码实现
 * KEYIN , k1 LongWritable
 * VALUEIN, v1 Text
 * KEYOUT,  k2  Text
 * VALUEOUT v2   Text
 */
public class Exce2Mapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    // 执行map阶段逻辑
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(value, NullWritable.get());
    }
}
