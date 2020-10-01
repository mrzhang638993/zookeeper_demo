package cn.itcast.zookeeper_api.stage1.mr_stage3.topN.stage2;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TopNBean implements WritableComparable<TopNBean> {
    private String url;
    private Integer count;

    @Override
    public int compareTo(TopNBean o) {
        return o.getCount() - this.count;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.url);
        out.writeInt(this.count);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.url = in.readUTF();
        this.count = in.readInt();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return
                url + '\001' +
                        count;
    }
}
