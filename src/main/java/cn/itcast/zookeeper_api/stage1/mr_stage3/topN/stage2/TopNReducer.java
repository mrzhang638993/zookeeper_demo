package cn.itcast.zookeeper_api.stage1.mr_stage3.topN.stage2;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class TopNReducer  extends Reducer<TopNBean, Text, TopNBean, NullWritable> {

    private TreeMap<TopNBean,NullWritable> flowMap;
    private  Integer topN=3;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        flowMap= new TreeMap<>();
    }

    /**
     * 求解前面的top10的信息
     * */
    @Override
    protected void reduce(TopNBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  key 到这个地方的时候不能保证有顺序的操作的。
        if (topN>0){
            context.write(key,NullWritable.get());
            topN--;
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        System.out.println(JSON.toJSONString(flowMap));
    }
}
