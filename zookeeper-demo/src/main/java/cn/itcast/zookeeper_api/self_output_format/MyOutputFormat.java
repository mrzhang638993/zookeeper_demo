package cn.itcast.zookeeper_api.self_output_format;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * K,  k1
 * V   v1
 */
public class MyOutputFormat extends FileOutputFormat<Text, NullWritable> {


    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        //  获取目标文件的输出流(2个)
        //  将输出流传给MyRecordWriter
        //  对应的是k3,k3的类型
        Configuration conf = taskAttemptContext.getConfiguration();
        FileSystem fs = FileSystem.get(conf);
        //  指定真实的数据存放的路径和数据
        FSDataOutputStream fsDataOutputStream = fs.create(new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\self_output_format\\good_commoms\\good.txt"));
        FSDataOutputStream fsDataOutputStream1 = fs.create(new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\self_output_format\\bad_commoms\\bad.txt"));
        MyRecordWriter myRecordWriter = new MyRecordWriter(fsDataOutputStream, fsDataOutputStream1);
        return myRecordWriter;
    }
}
