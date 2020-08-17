package cn.itcast.zookeeper_api.stage1.mr_stage1;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AccessGroup extends WritableComparator {

    public AccessGroup() {
        super(AccessLogBean.class, true);
    }

    /**
     * 实现分组内部的top的操作逻辑
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        //3.1对形参做强制类型转换
        //3.2指定分组规则
        AccessLogBean first = (AccessLogBean) a;
        AccessLogBean second = (AccessLogBean) b;
        // 指定分组内部根据url进行排序。
        return first.getIp().compareTo(second.getIp());
    }
}
