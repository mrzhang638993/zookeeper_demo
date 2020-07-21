package cn.itcast.zookeeper_api.exce.exec8;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  k2
 * VALUEIN,  v2
 * KEYOUT,  k3
 * VALUEOUT v3
 */
public class OrderReduce extends Reducer<Text, Text, NullWritable, Text> {

    /**
     * 执行reduce的业务逻辑
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String firstValue = "";
        String secondValue = "";
        for (Text value : values
        ) {
            String content = value.toString();
            if (content.startsWith("order")) {
                firstValue = content;
            } else {
                secondValue += content;
            }
        }
        // 输出的时候不需要key进行操作
        //context.write(key,new Text(firstValue+","+secondValue));
        context.write(NullWritable.get(), new Text(firstValue + "," + secondValue));
    }
}
