package cn.itcast.zookeeper_api.exce.exec14;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SortReduce extends Reducer<SortBean, Text, Text, Text> {


    @Override
    protected void reduce(SortBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        for (Text value : values) {
            String[] s = value.toString().split(" ");
            sb.append(s[1]).append(",");
        }
        int index = sb.lastIndexOf(",");
        String sub = sb.substring(0, index);
        context.write(new Text(key.getSort()), new Text(sub));
    }
}
