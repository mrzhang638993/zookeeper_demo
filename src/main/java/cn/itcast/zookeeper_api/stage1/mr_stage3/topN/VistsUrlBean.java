package cn.itcast.zookeeper_api.stage1.mr_stage3.topN;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VistsUrlBean implements WritableComparable<VistsUrlBean> {

    private  String url;
    private  Integer count;
    private  String  user;

    @Override
    public int compareTo(VistsUrlBean o) {
        return this.count-o.getCount();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.url);
        out.writeInt(this.count);
        out.writeUTF(this.user);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.url=in.readUTF();
        this.count=in.readInt();
        this.user=in.readUTF();
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
                 count+"\001";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
