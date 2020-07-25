package cn.itcast.zookeeper_api.common_friends_step1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Step1JobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new Step1JobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job step1Main = Job.getInstance(super.getConf(), "step1Main");
        step1Main.setJarByClass(Step1JobMain.class);
        //  设置输入
        step1Main.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(step1Main, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\common_friends_step1\\input\\friends.txt"));
        //  设置map阶段的参数
        step1Main.setMapperClass(Step1Mapper.class);
        step1Main.setMapOutputKeyClass(Text.class);
        step1Main.setMapOutputValueClass(Text.class);

        step1Main.setReducerClass(Step1Reduce.class);
        step1Main.setOutputKeyClass(Text.class);
        step1Main.setOutputValueClass(Text.class);
        step1Main.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(step1Main, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\common_friends_step1\\output"));
        boolean res = step1Main.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
