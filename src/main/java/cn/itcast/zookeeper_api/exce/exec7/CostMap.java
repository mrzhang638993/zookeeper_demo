package cn.itcast.zookeeper_api.exce.exec7;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


//  对应的排序操作在map阶段的后面，在reduce之前就已经进行了

/**
 * KEYIN,  k1
 * VALUEIN,  v1
 * KEYOUT,   k2
 * VALUEOUT  v2
 */
public class CostMap extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("    ");
        Text account = new Text(split[0]);
        StringBuilder sb = new StringBuilder();
        sb.append(split[1]).append("\t").append(split[2]);
        Text info = new Text(sb.toString());
        context.write(account, info);
    }
}
