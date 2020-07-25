package cn.itcast.zookeeper_api.exce.exce3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class StudentJobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new StudentJobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(super.getConf(), "studentJobMain");
        job.setJarByClass(StudentJobMain.class);
        //  设置输入
        job.setInputFormatClass(TextInputFormat.class);
        //  指定目录的话，会将目录下面所有的文件全部加载进行计算操作的。
        //TextInputFormat.addInputPath(job, new Path("hdfs://node01:8020/input/sort_input/score.txt"));
        TextInputFormat.addInputPath(job, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\input\\score.txt"));
        //  设置map阶段的参数
        job.setMapperClass(StudentScoreMapper.class);
        job.setMapOutputKeyClass(StudentScoreBean.class);
        job.setOutputValueClass(NullWritable.class);
        //  设置shuffle阶段的排序逻辑参数处理逻辑,主要包括分区，排序，规约，分组，此处不需要进行设置操作
        // 设置reduce阶段的参数处理
        job.setReducerClass(StudentScoreReduce.class);
        job.setOutputKeyClass(StudentScoreBean.class);
        job.setOutputValueClass(NullWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        //TextOutputFormat.setOutputPath(job, new Path("hdfs://node01:8020/out/sort_out"));
        TextOutputFormat.setOutputPath(job, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\output1\\"));
        boolean res = job.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
