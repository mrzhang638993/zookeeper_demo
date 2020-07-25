package cn.itcast.zookeeper_api.self_input_format;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 自定义实现recordReader
 */
public class MyRecordReader extends RecordReader<NullWritable, BytesWritable> {

    FSDataInputStream fopen = null;
    private Configuration conf = null;
    private FileSplit fileSplit = null;
    private boolean processed = false;
    private BytesWritable by = new BytesWritable();
    private FileSystem fs = null;

    /**
     * 执行初始化的操作
     */
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        // 获取configuration对象
        conf = taskAttemptContext.getConfiguration();
        //  获取path信息
        fileSplit = (FileSplit) inputSplit;
    }


    /**
     * 该方法用于获取k1,v1。是很关键的
     * k1 LongWritable
     * v1 BytesWritable
     * boolean  代表的是文件是否读取完成,会在程序读取文件的时候每一个文件读取2次的(一次是false执行操作，一次是true跳出的)。
     */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        //  文件没有读取完成
        if (!processed) {
            //  获取源文件的字节输入流
            fs = FileSystem.newInstance(conf);
            //  获取文件的输入流信息
            fopen = fs.open(fileSplit.getPath());
            // 1. 获取源文件的文件系统
            // 2.通过 fileSystem获取文件的字节输入流
            //  读取源文件数据到普通的字节数组
            // 将小文件的数据一定性的读取到字节数组中
            byte[] bytes = new byte[(int) fileSplit.getLength()];
            //  文件中的数据读取到字节数组中来进行操作
            IOUtils.readFully(fopen, bytes, 0, (int) fileSplit.getLength());
            //  将普通字节数组封装到BytesWritable字节数组中
            by.set(bytes, 0, (int) fileSplit.getLength());
            processed = true;
            return true;
        }
        return false;
    }

    /**
     * 返回k1的操作
     */
    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    /**
     * 放回v1,BytesWritable
     */
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return by;
    }

    /**
     * 获取文件的进度
     */
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return processed ? 1 : 0;
    }

    /**
     * 进行资源的释放操作
     */
    @Override
    public void close() throws IOException {
        if (fopen != null) {
            fopen.close();
        }
        if (fs != null) {
            fs.close();
        }
    }
}
