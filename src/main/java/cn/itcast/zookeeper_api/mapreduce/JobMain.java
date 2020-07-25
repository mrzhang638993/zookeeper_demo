package cn.itcast.zookeeper_api.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Job的主类
 */
public class JobMain extends Configured implements Tool {


    private static void judgePathIsExists(Path path) throws URISyntaxException, IOException, InterruptedException {
        //  本地的hdfs的配置
        FileSystem fs = FileSystem.getLocal(new Configuration());
        //  集群模式配置
        //FileSystem fs=FileSystem.get(new URI("hdfs://node01:8020"),new Configuration());
        fs.deleteOnExit(path);
        fs.close();
    }

    public static void main(String[] args) throws Exception {
        // 启动job任务
        int result = ToolRunner.run(new Configuration(), new JobMain(), args);
        // 进程执行结果
        System.exit(result);
    }

    /**
     * 需要对于job进行描述
     */
    @Override
    public int run(String[] args) throws Exception {
        // 创建Job任务对象
        // 获取配置类对象
        Job wordCount = Job.getInstance(super.getConf(), "wordCount");
        //  有些jar执行运行会出现错误的，需要执行如下的操作逻辑
        wordCount.setJarByClass(JobMain.class);

        //  指定job任务的8个步骤
        //  步骤1：指定文件的读取方式和读取路径
        wordCount.setInputFormatClass(TextInputFormat.class);
        //  设置远程输入路径信息
        TextInputFormat.addInputPath(wordCount, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\input\\"));
        //  指定本地的输入路径
        //TextInputFormat.addInputPath(wordCount,new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\input\\"));
        // 步骤二：指定map阶段的处理方式和数据类型
        wordCount.setMapperClass(WordCountMapper.class);
        // 设置map阶段的k2的类型,v2的类型
        wordCount.setMapOutputKeyClass(Text.class);
        wordCount.setMapOutputValueClass(LongWritable.class);
        //  步骤三，四，五，六：采用默认的方式进行处理
        // 步骤七： 指定reduce阶段的处理方式和数据类型。自定义reduce的逻辑，将（k2,v2）转化为（k3，v3）
        wordCount.setReducerClass(WordCountReducer.class);
        //  k3类型以及v3类型
        wordCount.setOutputKeyClass(Text.class);
        wordCount.setOutputValueClass(LongWritable.class);
        //  步骤八：设置输出类型,设置输出的路径
        wordCount.setOutputFormatClass(TextOutputFormat.class);
        Path path = new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\output\\");
        //  判断path是否存在，存在的话删除path,不存在的话创建path
        judgePathIsExists(path);
        TextOutputFormat.setOutputPath(wordCount, path);
        // 指定本地的输出路径,输出路径不能存在，否则会报错的。
        //TextOutputFormat.setOutputPath(wordCount,new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\output\\"));
        // 等待任务结束
        boolean result = wordCount.waitForCompletion(true);
        return result ? 0 : 1;
    }
}
