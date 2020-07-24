package cn.itcast.zookeeper_api.combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;


/**
 * KEYIN, k2类型
 * VALUEIN, v2类型
 * KEYOUT,  k3类型
 * VALUEOUT  v3类型
 */
public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    /**
     * 重写reduce方法，将新的（k2,v2）转化为（k3,v3）,将（k3,v3）写入到上下文中
     * key： 对应的是k2,
     * values： 集合 新的v2
     * context： 上下文对象
     * -------------------------------
     * k2         v2
     * hello      <1,1,1>
     * ------------------------------
     * k3          v3
     * hello       3
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        //  将新的（k2,v2）转化为(k3,v3)
        Iterator<LongWritable> it = values.iterator();
        LongWritable write = new LongWritable();
        int count = 0;
        while (it.hasNext()) {
            LongWritable next = it.next();
            count += next.get();
        }
        write.set(count);
        context.write(key, write);
    }
}
