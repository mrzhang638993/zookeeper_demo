package cn.itcast.zookeeper_api.common_friends_step1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  k2  B
 * VALUEIN,  v2  A
 * KEYOUT,  k3  B-E
 * VALUEOUT  v3 A
 */
public class Step1Reduce extends Reducer<Text, Text, Text, Text> {

    /**
     * 实现reduce的业务逻辑和操作体现
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  遍历元素，将集合中的每一个元素进行拼接操作，得到k3
        // v2  对应的是k3
        // （k3,v3）写入到上下文中
        StringBuffer sb = new StringBuffer();
        for (Text value : values) {
            //  最终的数据多了一个-符号信息。
            sb.append(value.toString()).append("-");
        }
        String content = sb.toString();
        int index = content.lastIndexOf("-");
        String sub = content.substring(0, index);
        context.write(new Text(sub), key);
    }
}
