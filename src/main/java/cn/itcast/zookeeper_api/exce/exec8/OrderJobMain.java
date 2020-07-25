package cn.itcast.zookeeper_api.exce.exec8;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class OrderJobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new OrderJobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job orderMain = Job.getInstance(super.getConf(), "orderMain");
        orderMain.setJarByClass(OrderJobMain.class);
        //  设置输入
        orderMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec8\\input"));
        //  设置map阶段的参数
        orderMain.setMapperClass(OrderMap.class);
        orderMain.setMapOutputKeyClass(Text.class);
        orderMain.setMapOutputValueClass(Text.class);

        orderMain.setReducerClass(OrderReduce.class);
        orderMain.setOutputKeyClass(Text.class);
        orderMain.setOutputValueClass(Text.class);
        orderMain.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec8\\output"));

        boolean res = orderMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
