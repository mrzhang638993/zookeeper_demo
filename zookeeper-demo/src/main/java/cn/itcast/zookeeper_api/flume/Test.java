package cn.itcast.zookeeper_api.flume;

public class Test {

    /**
     * 实现unicodestr转化为str操作
     */
    private static String unicodeToString(String unicodeStr) {
        String[] split = unicodeStr.split("\\\\u");
        return split[0];
    }

    public static void main(String[] args) {
        String str = "\u5916";
        String content = unicodeToString(str);
        System.out.println(content);
    }
}
