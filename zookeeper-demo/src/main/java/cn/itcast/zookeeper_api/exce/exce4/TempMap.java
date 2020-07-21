package cn.itcast.zookeeper_api.exce.exce4;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TempMap extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * 温度数据(k1,v1)  转化为(k2,v2)
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split(" ");
        int val = getIntValue(values[1]);
        if (val != 0) {
            context.write(new Text(values[0]), new IntWritable(val));
        }
    }

    private int getIntValue(String value) {
        if (value.equals("99999")) {
            return 0;
        } else {
            String[] values = value.split("\\+");
            String content = values[0];
            char firstChar = content.charAt(0);
            while (firstChar == '0') {
                content = content.replace("0", "");
                firstChar = content.charAt(0);
            }
            return Integer.valueOf(content);
        }
    }
}
