package cn.itcast.zookeeper_api.exce.exec8;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * KEYIN, k1 LongWritable
 * VALUEIN, v1 Text
 * KEYOUT,  k2 Text
 * VALUEOUT  v2 Text
 */
public class OrderMap extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        Path path = fileSplit.getPath();
        // 订单数据
        if (path.getName().startsWith("order.dat")) {
            String[] split = value.toString().split(",");
            context.write(new Text(split[1]), value);
        } else {
            // 用户数据
            String[] split = value.toString().split(",");
            context.write(new Text(split[0]), value);
        }
    }
}
