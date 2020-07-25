package cn.itcast.zookeeper_api.phoneCalc.phonepartition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class PhonePartition extends Partitioner<FlowBean, NullWritable> {

    /**
     * flowBean  k2
     * nullWritable  v2
     * i   reduce的个数
     */
    @Override
    public int getPartition(FlowBean flowBean, NullWritable nullWritable, int i) {
        //  根据手机号码进行分区操作实现
        String phoneNum = flowBean.getPhoneNum();
        if (phoneNum.startsWith("135")) {
            return 0;
        } else if (phoneNum.startsWith("136")) {
            return 1;
        } else if (phoneNum.startsWith("137")) {
            return 2;
        } else {
            return 3;
        }
    }
}
