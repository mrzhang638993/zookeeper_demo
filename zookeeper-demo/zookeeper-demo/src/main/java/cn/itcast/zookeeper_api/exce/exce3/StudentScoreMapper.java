package cn.itcast.zookeeper_api.exce.exce3;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生成绩排序操作逻辑,求每个学生的总分和平均分，并按总分降序排序
 * KEYIN,LongWritable
 * VALUEIN,Text
 * KEYOUT, Text
 * VALUEOUT StudentScoreBean
 */
public class StudentScoreMapper extends Mapper<LongWritable, Text, StudentScoreBean, NullWritable> {

    /**
     * 执行map过滤逻辑
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 执行key的过滤逻辑操作
        String[] values = value.toString().split("\t");
        StringBuilder sb = new StringBuilder();
        sb.append(values[0]).append("\t").append(values[1]).append("\t").append(values[2]);
        List<Integer> scoreList = new ArrayList<>();
        scoreList.add(Integer.valueOf(values[3]));
        scoreList.add(Integer.valueOf(values[4]));
        scoreList.add(Integer.valueOf(values[5]));
        StudentScoreBean studentScoreBean = new StudentScoreBean();
        studentScoreBean.setStuKey(sb.toString());
        studentScoreBean.setScoreList(scoreList);
        context.write(studentScoreBean, NullWritable.get());
    }
}
