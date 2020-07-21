package cn.itcast.zookeeper_api.exce.exec7;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  k2
 * VALUEIN,  v2
 * KEYOUT,  k3
 * VALUEOUT  v3
 */
public class CostReduce extends Reducer<Text, Text, CostBean, NullWritable> {

    /**
     * 执行reduce阶段的业务逻辑处理和操作是按
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        CostBean costBean = new CostBean();
        costBean.setKey(key.toString());
        int totalIncome = 0;
        int totalCost = 0;
        int totalBenifit = 0;
        for (Text value :
                values) {
            // 对应的是收入和之支出数据的
            String[] split = value.toString().split("\t");
            totalIncome += Integer.parseInt(split[0]);
            totalCost += Integer.parseInt(split[1]);
        }
        totalBenifit = totalIncome - totalCost;
        costBean.setTotalCost(totalCost);
        costBean.setTotalIncome(totalIncome);
        costBean.setTotalBenifit(totalBenifit);
        context.write(costBean, NullWritable.get());
    }
}
