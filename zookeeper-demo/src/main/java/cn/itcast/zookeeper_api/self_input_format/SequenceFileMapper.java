package cn.itcast.zookeeper_api.self_input_format;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * KEYIN, k1    NullWritable
 * VALUEIN, v1   BytesWritable
 * KEYOUT,  k2   Text
 * VALUEOUT   v2  BytesWritable
 * SequenceInputStream 对应的key是file名称的
 */
public class SequenceFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {


    /**
     * 执行map阶段的业务逻辑操作和实现
     */
    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        // 生成k2 Text(对应的是文件名称) ,v2  BytesWritable
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        context.write(new Text(fileName), value);
    }
}
