package cn.itcast.zookeeper_api.common_friends_step2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;


/**
 * KEYIN, k1 LongWritable
 * VALUEIN, v1 Text
 * KEYOUT,  k2 Text
 * VALUEOUT  v2 Text
 */
public class Step2Mapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        // 需要将split[1]中的元素进行两两合并的操作逻辑实现
        String[] split1 = split[0].split("-");
        //  合并形成两两的元素实现逻辑操作
        int len = split1.length;

        Arrays.sort(split1);
        int j = 0;
        //  怎么实现元素的两两组合逻辑，涉及对应的算法逻辑。
        for (int i = 0; i < len - 1; i++) {
            for (j = i + 1; j < len; j++) {
                StringBuilder sb = new StringBuilder();
                sb.append(split1[i]).append("-").append(split1[j]);
                context.write(new Text(sb.toString()), new Text(split[1]));
            }
        }

    }
}
