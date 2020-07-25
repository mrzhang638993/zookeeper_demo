package cn.itcast.zookeeper_api.exce.exec7;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CostJobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new CostJobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job costbMain = Job.getInstance(super.getConf(), "costbMain");
        costbMain.setJarByClass(CostJobMain.class);
        //  设置输入
        costbMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(costbMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\input\\cost.txt"));
        //  设置map阶段的参数
        costbMain.setMapperClass(CostMap.class);
        costbMain.setMapOutputKeyClass(Text.class);
        costbMain.setMapOutputValueClass(Text.class);

        costbMain.setReducerClass(CostReduce.class);
        costbMain.setOutputKeyClass(Text.class);
        costbMain.setOutputValueClass(CostBean.class);
        costbMain.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(costbMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\cost\\"));
        boolean res = costbMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
