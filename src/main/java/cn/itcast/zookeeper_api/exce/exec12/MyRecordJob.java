package cn.itcast.zookeeper_api.exce.exec12;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MyRecordJob extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new MyRecordJob(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job recordJob = Job.getInstance(super.getConf(), "recordJob");
        recordJob.setJarByClass(MyRecordJob.class);

        //  对应的执行相关的业务逻辑实现
        recordJob.setInputFormatClass(MySelfInput.class);
        MySelfInput.addInputPath(recordJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec12\\input\\score.txt"));
        //  设置mapper极段的参数
        recordJob.setMapperClass(MyRecordMap.class);
        recordJob.setMapOutputKeyClass(ScoreWritable.class);
        recordJob.setMapOutputValueClass(NullWritable.class);
        //设置输出的参数和路径
        recordJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(recordJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec12\\output"));
        boolean res = recordJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
