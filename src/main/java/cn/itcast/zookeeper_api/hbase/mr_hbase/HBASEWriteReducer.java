package cn.itcast.zookeeper_api.hbase.mr_hbase;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 *KEYIN, Text,
 * VALUEIN, NullWritable
 * KEYOUT ImmutableBytesWritable  对应的是rowkey，需要写出去的数据封装到put对象中
 * */
public class HBASEWriteReducer extends TableReducer<Text, NullWritable, ImmutableBytesWritable> {

    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        String content = key.toString();
        String[] value = content.split("\t");
        //  怎么构建需要写入的对象
        Put put=new Put(value[0].getBytes());
        put.addColumn("f1".getBytes(),"name".getBytes(),value[1].getBytes());
        put.addColumn("f1".getBytes(),"age".getBytes(),value[2].getBytes());
        //  构建之后怎么写入到hbase,第一个参数是ImmutableBytesWritable
        context.write(new ImmutableBytesWritable(value[0].getBytes()),put);
    }
}
