package cn.itcast.zookeeper_api.join.onejoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 实现对应的业务逻辑实现
 */
public class ReduceJoinMainJob extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new ReduceJoinMainJob(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job reduceJoinMain = Job.getInstance(super.getConf(), "reduceJoinMain");
        reduceJoinMain.setJarByClass(ReduceJoinMainJob.class);
        //  设置输入
        reduceJoinMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(reduceJoinMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\join\\input"));
        //  设置map阶段的参数
        reduceJoinMain.setMapperClass(ReduceJoinMapper.class);
        reduceJoinMain.setMapOutputKeyClass(Text.class);
        reduceJoinMain.setMapOutputValueClass(Text.class);

        reduceJoinMain.setReducerClass(ReduceJoinReducer.class);
        reduceJoinMain.setOutputKeyClass(Text.class);
        reduceJoinMain.setOutputValueClass(Text.class);
        reduceJoinMain.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(reduceJoinMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\join\\output"));
        boolean res = reduceJoinMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
