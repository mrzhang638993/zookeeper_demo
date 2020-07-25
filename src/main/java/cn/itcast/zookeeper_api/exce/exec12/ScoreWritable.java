package cn.itcast.zookeeper_api.exce.exec12;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 自定义bean实现数据的疯转操作实现
 */
public class ScoreWritable implements WritableComparable<ScoreWritable> {

    private String name;
    private String id;
    private int yuwenScore;
    private int shuxueScore;
    private int englishScore;
    private int wuliScore;
    private int huaxueScore;
    //  获取总成绩和平均成绩
    private int totalScore;
    private double avgScore;

    @Override
    public int compareTo(ScoreWritable o) {
        return o.totalScore - this.totalScore;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.name);
        out.writeUTF(this.id);
        out.writeInt(this.yuwenScore);
        out.writeInt(this.shuxueScore);
        out.writeInt(this.englishScore);
        out.writeInt(this.wuliScore);
        out.writeInt(this.huaxueScore);
        out.writeInt(this.totalScore);
        out.writeDouble(this.avgScore);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.name = in.readUTF();
        this.id = in.readUTF();
        this.yuwenScore = in.readInt();
        this.shuxueScore = in.readInt();
        this.englishScore = in.readInt();
        this.wuliScore = in.readInt();
        this.huaxueScore = in.readInt();
        this.totalScore = in.readInt();
        this.avgScore = in.readDouble();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYuwenScore() {
        return yuwenScore;
    }

    public void setYuwenScore(int yuwenScore) {
        this.yuwenScore = yuwenScore;
    }

    public int getShuxueScore() {
        return shuxueScore;
    }

    public void setShuxueScore(int shuxueScore) {
        this.shuxueScore = shuxueScore;
    }

    public int getEnglishScore() {
        return englishScore;
    }

    public void setEnglishScore(int englishScore) {
        this.englishScore = englishScore;
    }

    public int getWuliScore() {
        return wuliScore;
    }

    public void setWuliScore(int wuliScore) {
        this.wuliScore = wuliScore;
    }

    public int getHuaxueScore() {
        return huaxueScore;
    }

    public void setHuaxueScore(int huaxueScore) {
        this.huaxueScore = huaxueScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                        ", id='" + id + '\'' +
                        ", yuwenScore=" + yuwenScore +
                        ", shuxueScore=" + shuxueScore +
                        ", englishScore=" + englishScore +
                        ", wuliScore=" + wuliScore +
                        ", huaxueScore=" + huaxueScore +
                        ", totalScore=" + totalScore +
                        ", avgScore=" + avgScore;
    }
}
