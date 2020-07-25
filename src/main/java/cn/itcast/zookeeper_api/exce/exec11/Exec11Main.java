package cn.itcast.zookeeper_api.exce.exec11;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 实现mainJob操作
 */
public class Exec11Main extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new Exec11Main(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        //  对应的逻辑实现
        Job exec11Main = Job.getInstance(super.getConf(), "exec11Main");
        // 设置输入的操作逻辑
        exec11Main.setInputFormatClass(CombineTextInputFormat.class);
        CombineFileInputFormat.setMaxInputSplitSize(exec11Main, 1024 * 1024 * 128);
        CombineFileInputFormat.addInputPath(exec11Main, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec11\\input"));
        //  设置文件最大128m的大小
        //  下面对应的执行相关的业务逻辑实现
        exec11Main.setMapperClass(CombinesMapper.class);
        exec11Main.setMapOutputKeyClass(Text.class);
        exec11Main.setMapOutputValueClass(NullWritable.class);
        //  对应的实现相干的数据和代码业务逻辑实现
        //  设置文件的输出路径和操作逻辑
        exec11Main.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(exec11Main, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec11\\output"));
        //  判断exec11Main的执行状态操作
        boolean res = exec11Main.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
