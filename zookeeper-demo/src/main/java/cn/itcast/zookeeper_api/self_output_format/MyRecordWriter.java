package cn.itcast.zookeeper_api.self_output_format;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * K,  k1
 * V   v1
 */
public class MyRecordWriter extends RecordWriter<Text, NullWritable> {
    /**
     * 好文件的输出流
     */
    private FSDataOutputStream fsDataOutputStream;
    /**
     * 坏文件的输出流
     */
    private FSDataOutputStream fsDataOutputStream1;

    /**
     * 对应的生成有参数和无参数的构造器
     */
    public MyRecordWriter(FSDataOutputStream fsDataOutputStream, FSDataOutputStream fsDataOutputStream1) {
        this.fsDataOutputStream = fsDataOutputStream;
        this.fsDataOutputStream1 = fsDataOutputStream1;
    }


    public MyRecordWriter() {
    }

    /**
     * text 对应的是行文本数据
     */
    @Override
    public void write(Text text, NullWritable text2) throws IOException, InterruptedException {
        //  从行文本信息中获取第9个字段
        String[] split = text.toString().split("\t");
        int numDigit = Integer.parseInt(split[9]);
        if (numDigit == 0 || numDigit == 1) {
            //  对应的是好评或者是中评数据
            fsDataOutputStream.write(text.toString().getBytes());
            fsDataOutputStream.write("\r\n".getBytes());
        } else {
            //  对应的是差评数据
            fsDataOutputStream1.write(text.toString().getBytes());
            fsDataOutputStream1.write("\r\n".getBytes());
        }
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        if (fsDataOutputStream != null) {
            fsDataOutputStream.close();
        }
        if (fsDataOutputStream1 != null) {
            fsDataOutputStream1.close();
        }
    }
}
