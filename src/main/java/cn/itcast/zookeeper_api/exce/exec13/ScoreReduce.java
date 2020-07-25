package cn.itcast.zookeeper_api.exce.exec13;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ScoreReduce extends Reducer<ScoreBean, Text, ScoreBean, NullWritable> {

    @Override
    protected void reduce(ScoreBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // 求解每一门课程的平均分，对应的是top1的操作逻辑
        int score = 0;
        int len = 0;
        double avg = 0;
        String lec = key.getLec();
        if (lec.equals("语文")) {
            for (Text value : values) {
                String[] split = value.toString().split("\t");
                score += Integer.valueOf(split[3]);
                len++;
            }
        } else if ("数学".equals(lec)) {
            for (Text value : values) {
                String[] split = value.toString().split("\t");
                score += Integer.valueOf(split[4]);
                len++;
            }
        } else {
            for (Text value : values) {
                String[] split = value.toString().split("\t");
                score += Integer.valueOf(split[5]);
                len++;
            }
        }
        avg = score / len;
        key.setAvgScore(avg);
        context.write(key, NullWritable.get());
    }
}
