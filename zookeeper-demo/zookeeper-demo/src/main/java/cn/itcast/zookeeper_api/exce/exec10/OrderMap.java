package cn.itcast.zookeeper_api.exce.exec10;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * KEYIN,  k1
 * VALUEIN,  v1
 * KEYOUT,  k2
 * VALUEOUT  v2
 */
public class OrderMap extends Mapper<LongWritable, Text, Text, Text> {

    private Map map = new HashMap();

    /**
     * 实现map过滤操作，小表链接大表，采用的是map端的join操作
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("    ");
        String productId = split[1];
        String content = (String) map.get(productId);
        context.write(new Text(productId), new Text(content + "\t" + value));
    }

    /**
     * 实现资源加载操作
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //  获取缓存的文件，只有一个缓存文件的
        URI[] cacheFiles = context.getCacheFiles();
        Configuration conf = context.getConfiguration();
        FileSystem fs = FileSystem.newInstance(cacheFiles[0], conf);
        FSDataInputStream in = fs.open(new Path(cacheFiles[0]));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String content = null;
        while ((content = br.readLine()) != null) {
            String[] split = content.split("    ");
            map.put(split[0], split[1]);
        }
        br.close();
        fs.close();
    }
}
