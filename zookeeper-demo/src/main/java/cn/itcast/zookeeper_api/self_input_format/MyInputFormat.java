package cn.itcast.zookeeper_api.self_input_format;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * 自定义的inputReader实现自定义的inputReader实现操作逻辑
 */
public class MyInputFormat extends FileInputFormat<NullWritable, BytesWritable> {


    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        // 1.创建自定义的RecordReader对象
        MyRecordReader myRecordReader = new MyRecordReader();
        //  传递参数给RecordReader对象
        myRecordReader.initialize(inputSplit, taskAttemptContext);
        return myRecordReader;
    }

    /**
     * 文件是否需要切片分割
     */
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }
}
