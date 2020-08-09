package cn.itcast.zookeeper_api.stage1.mr_stage2;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 *  实现分组逻辑必须要根据key实现分组操作的
 * */
public class AccessGroup extends WritableComparator {

    public  AccessGroup(){
        super(Text.class,true);
    }

    /**
     * 实现分组内部的top的操作逻辑
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        //3.1对形参做强制类型转换
        //3.2指定分组规则
        Text first = (Text) a;
        Text second = (Text) b;
        // 指定分组规则。
        return first.toString().compareTo(second.toString());
    }
}
