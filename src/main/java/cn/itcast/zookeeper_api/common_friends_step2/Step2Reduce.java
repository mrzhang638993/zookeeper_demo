package cn.itcast.zookeeper_api.common_friends_step2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,Text
 * VALUEIN, Text
 * KEYOUT, Text
 * VALUEOUT Text
 */
public class Step2Reduce extends Reducer<Text, Text, Text, Text> {


    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  实现元素的拼接操作实现
        StringBuilder sb = new StringBuilder();
        for (Text value : values
        ) {
            sb.append(value.toString()).append("-");
        }
        int index = sb.lastIndexOf("-");
        String sub = sb.substring(0, index);
        context.write(key, new Text(sub));
    }
}
