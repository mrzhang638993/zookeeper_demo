package cn.itcast.zookeeper_api.common_friends_step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * KEYIN, k1 LongWritable 0
 * VALUEIN, v1  Text A:B,C,D,E,F
 * KEYOUT,  k2 Text  B
 * VALUEOUT  v2 Text A
 */
public class Step1Mapper extends Mapper<LongWritable, Text, Text, Text> {

    /**
     * 执行step1的map阶段的操作逻辑实现
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //  拆分行文本数据,冒号进行拆分，冒号左边对应的是v2，
        //  冒号右边需要按照逗号进行拆分,单个的元素作为k2实现操作的。
        // 将k2,v2 写入上下文中执行操作
        String[] split = value.toString().split(":");
        Text v2 = new Text(split[0]);
        String[] split1 = split[1].split(",");
        int len = split1.length;
        for (int i = 0; i < len; i++) {
            Text k2 = new Text(split1[i]);
            context.write(k2, v2);
        }
    }
}
