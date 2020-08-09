package cn.itcast.zookeeper_api.stage1.mr_stage2;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AccessLogBean implements WritableComparable<AccessLogBean> {

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private  String ip;
    private  String  time;
    private  String  url;
    private  Integer count;

    /**
     * 设置排序规则和业务代码实现逻辑.根据ip地址实现排序规则
     * */
    @Override
    public int compareTo(AccessLogBean o) {
        //  根据ip的升序，ip一样的话，根据url进行升序排列
        int result= this.getIp().compareTo(o.getIp());
        if (result==0){
            return this.getUrl().compareTo(o.getUrl());
        }else {
            return  result;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
      out.writeUTF(this.ip);
      out.writeUTF(this.time);
      out.writeUTF(this.url);
      out.writeInt(this.count);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
      this.ip=in.readUTF();
      this.time=in.readUTF();
      this.url=in.readUTF();
      this.count=in.readInt();
    }

    @Override
    public String toString() {
        return
                 ip + '\t' +
                 time + '\t' +
                 url + '\t'
                +count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
