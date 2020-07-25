package cn.itcast.zookeeper_api.phoneCalc.phonepartition;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 上行流量数据排序对应的bean。排序操作的话，对应的排序字段需要放置到key中参与排序的。
 */
public class FlowBean implements WritableComparable<FlowBean> {
    /**
     * 上行流量
     */
    private int upFlow;
    /**
     * 对应的是下行流量
     */
    private int downFlow;
    /**
     * 对应的是上行流量包的大小
     */
    private int upCountFlow;
    /**
     * 对应的是下行流量包的大小
     */
    private int downCountFlow;
    /**
     * 手机号实现操作逻辑
     */
    private String phoneNum;

    @Override
    public int compareTo(FlowBean o) {
        // 降序排列操作
        return o.upFlow - this.upFlow;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.upFlow);
        out.writeInt(this.downFlow);
        out.writeInt(this.upCountFlow);
        out.writeInt(this.downCountFlow);
        out.writeUTF(this.phoneNum);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readInt();
        this.downFlow = in.readInt();
        this.upCountFlow = in.readInt();
        this.downCountFlow = in.readInt();
        this.phoneNum = in.readUTF();
    }

    public int getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(int upFlow) {
        this.upFlow = upFlow;
    }

    public int getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(int downFlow) {
        this.downFlow = downFlow;
    }

    public int getUpCountFlow() {
        return upCountFlow;
    }

    public void setUpCountFlow(int upCountFlow) {
        this.upCountFlow = upCountFlow;
    }

    public int getDownCountFlow() {
        return downCountFlow;
    }

    public void setDownCountFlow(int downCountFlow) {
        this.downCountFlow = downCountFlow;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return phoneNum +
                "\t" + upFlow +
                "\t" + downFlow +
                "\t" + upCountFlow +
                "\t" + downCountFlow;
    }
}
