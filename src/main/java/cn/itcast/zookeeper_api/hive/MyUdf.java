package cn.itcast.zookeeper_api.hive;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 自定义hive函数
 */
public class MyUdf extends UDF {

    public static void main(String[] args) {
        new MyUdf().evaluate(new Text("hello"));
    }

    /**
     * 模拟hive的upper方法，将小写转化为大写
     * 将字符串的第一个字符转化为大写
     */
    public Text evaluate(final Text line) {
        String content = line.toString();
        if (null == content || "".equals(content)) {
            // 判断字符串是空串还是null。
            System.out.println("输入的是null或者是empty的空的字符串");
            // 返回一个空的字符串进行操作
            return new Text("");
        } else {
            String finalContent = content.substring(0, 1).toUpperCase()
                    + content.substring(1, content.length());
            return new Text(finalContent);
        }
    }
}
