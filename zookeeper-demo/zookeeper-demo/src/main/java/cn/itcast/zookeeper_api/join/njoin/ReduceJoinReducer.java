package cn.itcast.zookeeper_api.join.njoin;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  k2 Text
 * VALUEIN,  v2 Text
 * KEYOUT,  k3 Text
 * VALUEOUT  k3 Text
 */
public class ReduceJoinReducer extends Reducer<Text, Text, Text, Text> {

    /**
     * 执行reduce的相关的业务逻辑实现
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  备注：单个订单对应的只有单个的产品数据的。
        String first = "";
        String second = "";
        for (Text value :
                values) {
            if (value.toString().startsWith("p")) {
                // 认为是产品数据
                first = value.toString();
            } else {
                second += value.toString() + "\t";
            }
        }
        context.write(key, new Text(first + "\t" + second));
    }
}
