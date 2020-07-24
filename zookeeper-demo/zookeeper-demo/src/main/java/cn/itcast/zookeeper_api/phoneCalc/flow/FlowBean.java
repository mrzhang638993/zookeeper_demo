package cn.itcast.zookeeper_api.phoneCalc.flow;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 流量bean的封装类数据,仅仅需要实现序列化的接口即可
 */
public class FlowBean implements Writable {
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

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.upFlow);
        out.writeInt(this.downFlow);
        out.writeInt(this.upCountFlow);
        out.writeInt(this.downCountFlow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readInt();
        this.downFlow = in.readInt();
        this.upCountFlow = in.readInt();
        this.downCountFlow = in.readInt();
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

    /**
     * 这个方法很关键,涉及到输出的操作逻辑的。
     */
    @Override
    public String toString() {
        return
                upFlow +
                        "\t" + downFlow +
                        "\t" + upCountFlow +
                        "\t" + downCountFlow;
    }
}
