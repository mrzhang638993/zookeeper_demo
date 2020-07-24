package cn.itcast.zookeeper_api.exce.exec6;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SalaryJobMain extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new SalaryJobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job salarybMain = Job.getInstance(super.getConf(), "salarybMain");
        salarybMain.setJarByClass(SalaryJobMain.class);
        //  设置输入
        salarybMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(salarybMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\input\\salary.txt"));
        //  设置map阶段的参数
        salarybMain.setMapperClass(SalaryMap.class);
        salarybMain.setMapOutputKeyClass(SalaryBean.class);
        salarybMain.setMapOutputValueClass(NullWritable.class);

        salarybMain.setReducerClass(SalaryReduce.class);
        salarybMain.setOutputKeyClass(SalaryBean.class);
        salarybMain.setOutputValueClass(NullWritable.class);
        salarybMain.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(salarybMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\salary\\"));
        boolean res = salarybMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
