package cn.itcast.zookeeper_api.hbase.mr_hbase;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 读取hdfs上面的文件进行处理操作
 * KEYIN, k1 LongWritable
 * VALUEIN, v1  Text
 * KEYOUT, k2  Text
 * VALUEOUT, v2  NullWritable
 */
public class HDFSMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(value, NullWritable.get());
    }
}
