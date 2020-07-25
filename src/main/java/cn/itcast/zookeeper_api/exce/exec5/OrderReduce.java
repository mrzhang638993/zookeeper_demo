package cn.itcast.zookeeper_api.exce.exec5;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * KEYIN,  k2 Text
 * VALUEIN,  v2 DoubleWritable
 * KEYOUT,  k3 OrderBean
 * VALUEOUT v3  NullWritable
 */
public class OrderReduce extends Reducer<Text, DoubleWritable, OrderBean, NullWritable> {

    /**
     * 执行reduce的操作逻辑
     */
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        OrderBean orderBean = new OrderBean();
        orderBean.setKey(key.toString());
        BigDecimal value = new BigDecimal(0);
        for (DoubleWritable val :
                values) {
            BigDecimal v = new BigDecimal(val.get());
            value = value.add(v);
        }
        orderBean.setNum(value.doubleValue());
        context.write(orderBean, NullWritable.get());
    }
}
