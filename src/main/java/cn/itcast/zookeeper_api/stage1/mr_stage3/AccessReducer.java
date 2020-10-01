package cn.itcast.zookeeper_api.stage1.mr_stage3;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * join操作实现获取到初步的独立数的信息
 */
public class AccessReducer extends Reducer<Text, Text, AccessLogBean, NullWritable> {


    /**
     * 同一个ip对应的分区的内容全部到到这边了。。
     */
    // AccessLogBean  192.168.1.11	2017-11-20 10:02	url1	1		3
    // 192.168.1.11,2017-11-20 10:04,url1
    @Override
    protected void reduce(Text text, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<AccessLogBean> vists = new ArrayList<>();
        List<AccessLogBean> logins = new ArrayList<>();
        values.forEach(x -> judgeContent(x, vists, logins, text));
        //  对于访客记录进行操作管理，根据时间进行升序排操作
        Collections.sort(logins, new Comparator<AccessLogBean>() {
            @Override
            public int compare(AccessLogBean o1, AccessLogBean o2) {
                int result = o1.getUser().compareTo(o2.getUser());
                if (result == 0) {
                    return o1.getTime().compareTo(o2.getTime());
                } else {
                    return result;
                }
            }
        });
        //  将任意的两行记录对应的视为相关的信息。上线和下线的信息完整
        List<AccessLogBean> remove = new ArrayList<>();
        for (int i = 0; i < logins.size(); i += 2) {
            AccessLogBean accessLogBean = logins.get(i);
            AccessLogBean accessLogBean1 = logins.get(i + 1);
            accessLogBean.setTime1(accessLogBean1.getTime());
            remove.add(accessLogBean1);
        }
        logins.removeAll(remove);
        //  下面对于访客进行过滤操作。计算每一个url对应的独立访客的人数最多的10个url
        for (int i = 0; i < logins.size(); i++) {
            AccessLogBean accessLogBean = logins.get(i);
            String time = accessLogBean.getTime();
            String time1 = accessLogBean.getTime1();
            String user = accessLogBean.getUser();
            vists.stream().forEachOrdered(x -> {
                try {
                    addUser(x, time, time1, user);
                } catch (ParseException e) {
                    System.out.println("时间格式转化异常");
                }
            });
        }
        Set<AccessLogBean> set = new HashSet<>();
        for (int i = 0; i < vists.size(); i++) {
            AccessLogBean accessLogBean = vists.get(i);
            if (set.size() == 0) {
                set.add(accessLogBean);
            } else {
                boolean result = checkIfEquals(set, accessLogBean);
                if (!result) {
                    set.add(accessLogBean);
                }
            }
        }
        Iterator<AccessLogBean> iterator = set.iterator();
        while (iterator.hasNext()) {
            context.write(iterator.next(), NullWritable.get());
        }
    }

    /**
     * 指定元素相等的逻辑进行排查操作
     */
    private boolean checkIfEquals(Set<AccessLogBean> set, AccessLogBean accessLogBean) {
        for (AccessLogBean x : set) {
            boolean res = singalCheck(x, accessLogBean);
            if (res) {
                return true;
            }
        }
        return false;
    }

    private boolean singalCheck(AccessLogBean x, AccessLogBean accessLogBean) {
        String url = x.getUrl();
        String user = x.getUser();
        if (url.equals(accessLogBean.getUrl()) && user.equals(accessLogBean.getUser())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据时间段匹配对应的用户信息
     */
    private void addUser(AccessLogBean x, String time, String time1, String user) throws ParseException {
        String time2 = x.getTime();
        boolean result = timeBetween(time2, time, time1);
        if (result) {
            x.setUser(user);
        }
    }

    private boolean timeBetween(String time2, String time, String time1) throws ParseException {
        //2017-11-20 11:30,2
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse2 = simpleDateFormat.parse(time2);
        Date parse = simpleDateFormat.parse(time);
        Date parse1 = simpleDateFormat.parse(time1);
        if ((parse2.after(parse) && parse2.before(parse1)) || parse2.compareTo(parse) == 0 || parse2.compareTo(parse1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void judgeContent(Text x, List<AccessLogBean> vists, List<AccessLogBean> logins, Text text) {
        String content = x.toString();
        String[] split = content.split(",");
        AccessLogBean accessLogBean = new AccessLogBean();
        if (split[0].equals(text.toString())) {
            accessLogBean.setUrl(split[2]);
            accessLogBean.setIp(split[0]);
            accessLogBean.setTime(split[1]);
            accessLogBean.setCount(1);
            vists.add(accessLogBean);
        } else {
            accessLogBean.setUser(split[0]);
            accessLogBean.setIp(split[1]);
            accessLogBean.setTime(split[2]);
            accessLogBean.setOper(Integer.parseInt(split[3]));
            logins.add(accessLogBean);
        }
    }
}
