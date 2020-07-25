package cn.itcast.zookeeper_api.exce.exce1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 作业1对应的mapper
 * KEYIN, k1  对应的是LongWritable
 * VALUEIN, v1 对应的是Text
 * KEYOUT,  k2 对应的是 Text
 * VALUEOUT, v2对应的是LongWritable
 */
public class Exce1Mapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    /**
     * 实现map阶段的操作处理
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split(" ");
        Text text = new Text(values[0]);
        LongWritable val = new LongWritable(Long.valueOf(values[1]));
        context.write(text, val);
    }
}
