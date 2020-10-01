package cn.itcast.zookeeper_api.exce.exec13;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class ScoreGroup extends WritableComparator {

    public ScoreGroup() {
        super(ScoreBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        // 根据班级和课程进行分区操作实现
        ScoreBean first = (ScoreBean) a;
        ScoreBean second = (ScoreBean) b;
        int result = first.getStuClass().compareTo(second.getStuClass());
        if (result == 0) {
            return first.getLec().compareTo(second.getLec());
        } else {
            return result;
        }
    }
}
