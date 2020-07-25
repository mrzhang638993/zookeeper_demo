package cn.itcast.zookeeper_api.self_output_format;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN,LongWritable
 * VALUEIN,  Text
 * KEYOUT, Text
 * VALUEOUT Text
 */
public class MyOutputFormatMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    /**
     * 实现对应的map逻辑处理实现
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(value, NullWritable.get());
    }
}
