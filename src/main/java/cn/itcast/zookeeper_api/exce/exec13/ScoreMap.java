package cn.itcast.zookeeper_api.exce.exec13;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN, LongWritable
 * VALUEIN,  Text
 * KEYOUT,   SortBean
 * VALUEOUT    Text
 */
public class ScoreMap extends Mapper<LongWritable, Text, ScoreBean, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setStuClass(split[0]);
        scoreBean.setLec("语文");
        scoreBean.setAvgScore(Double.valueOf(split[3]));
        context.write(scoreBean, value);
        scoreBean.setLec("数学");
        scoreBean.setAvgScore(Double.valueOf(split[4]));
        context.write(scoreBean, value);
        scoreBean.setLec("英语");
        scoreBean.setAvgScore(Double.valueOf(split[5]));
        context.write(scoreBean, value);
    }
}
