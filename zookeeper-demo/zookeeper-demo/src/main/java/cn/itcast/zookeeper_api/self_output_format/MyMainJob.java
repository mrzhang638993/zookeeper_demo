package cn.itcast.zookeeper_api.self_output_format;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MyMainJob extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new MyMainJob(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        //  获取job对象
        Job jobMain = Job.getInstance(super.getConf(), "myMain");
        //  设置job任务(总的包括8个步骤的)
        jobMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(jobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\self_output_format\\input"));

        jobMain.setMapperClass(MyOutputFormatMapper.class);
        jobMain.setMapOutputKeyClass(Text.class);
        jobMain.setMapOutputValueClass(NullWritable.class);

        //设置输出路径和输出文件
        jobMain.setOutputFormatClass(MyOutputFormat.class);
        //  怎么设置多个路径？这里面指定的是输出的校验文件和辅助文件内容
        MyOutputFormat.setOutputPath(jobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\self_output_format\\output"));
        //  等待job执行结果
        //  生成的二进制文件对应的可以作为中间文件进行操作管理
        boolean res = jobMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
