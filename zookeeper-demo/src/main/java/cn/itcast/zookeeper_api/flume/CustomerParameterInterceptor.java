package cn.itcast.zookeeper_api.flume;

import org.apache.commons.compress.utils.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 实现flume的拦截器接口，实现自定义插件操作
 */
public class CustomerParameterInterceptor implements Interceptor {

    /**
     * The field_separator.指明每一行字段的分隔符
     */
    private final String fields_separator;

    /**
     * The indexs.通过分隔符分割后，指明需要那列的字段 下标
     */
    private final String indexs;

    /**
     * The indexs_separator. 多个下标的分隔符
     */
    private final String indexs_separator;

    /**
     * The encrypted_field_index. 需要加密的字段下标
     */
    private final String encrypted_field_index;

    /**
     * 字符全部执行unicode编码执行逻辑，需要进行unicode解密操作
     */
    public CustomerParameterInterceptor(String fields_separator, String indexs, String indexs_separator, String encrypted_field_index) {
        this.fields_separator = unicodeToString(fields_separator);
        this.indexs = indexs;
        this.indexs_separator = unicodeToString(indexs_separator);
        this.encrypted_field_index = unicodeToString(encrypted_field_index);
    }

    /**
     * 实现unicodestr转化为str操作
     */
    private String unicodeToString(String unicodeStr) {
        String[] split = unicodeStr.split("\\\\u");
        return split[0];
    }

    @Override
    public void initialize() {

    }

    /**
     * 实现fume 拦截器主要的核心的拦截方法
     */
    @Override
    public Event intercept(Event event) {
        /**
         * 真正的event的业务逻辑处理实现
         * */
        if (event == null) {
            return null;
        } else {
            //  获取body的内容
            String line = new String(event.getBody(), Charsets.UTF_8);
            //  一行的内容进行切割操作实现
            String[] fields_spilts = line.split(fields_separator);
            // 获取需要得到的索引的序号信息
            String[] indexs_split = indexs.split(indexs_separator);
            String newLine = "";
            //  需要设置加密之后的event的body信息内容。对应的是索引的序列号信息
            for (int i = 0; i < indexs_split.length; i++) {
                // 0,1,3,5,6 对应的是索引号信息
                int parseInt = Integer.parseInt(indexs_split[i]);
                //对加密字段进行加密
                if (!"".equals(encrypted_field_index) && encrypted_field_index.equals(indexs_split[i])) {
                    //  对加密的字段进行加密操作实现
                    newLine += StringUtils.GetMD5Code(fields_spilts[parseInt]);
                } else {
                    //  其他的字段不用进行加密操作实现管理。
                    newLine += fields_spilts[parseInt];
                }
                if (i != indexs_split.length - 1) {
                    //  字段之间使用原来的分隔符进行分割操作
                    newLine += fields_separator;
                }
            }
            event.setBody(newLine.getBytes(Charsets.UTF_8));
            return event;
        }
    }

    /**
     * 实现批量的event的拦截处理逻辑
     * 对于批量的event的每一个event执行逻辑处理
     */
    @Override
    public List<Event> intercept(List<Event> list) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Event event = list.get(i);
                intercept(event);
            }
        }
        return list;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        /**
         * The fields_separator.指明每一行字段的分隔符
         */
        private String fields_separator;

        /**
         * The indexs.通过分隔符分割后，指明需要那列的字段 下标
         */
        private String indexs;

        /**
         * The indexs_separator. 多个下标下标的分隔符
         */
        private String indexs_separator;

        /**
         * The encrypted_field. 需要加密的字段下标
         */
        private String encrypted_field_index;

        /*
         * @see org.apache.flume.conf.Configurable#configure(org.apache.flume.Context)
         */
        public void configure(Context context) {
            fields_separator = context.getString(Constants.FIELD_SEPARATOR, Constants.DEFAULT_FIELD_SEPARATOR);
            indexs = context.getString(Constants.INDEXS, Constants.DEFAULT_INDEXS);
            indexs_separator = context.getString(Constants.INDEXS_SEPARATOR, Constants.DEFAULT_INDEXS_SEPARATOR);
            encrypted_field_index = context.getString(Constants.ENCRYPTED_FIELD_INDEX, Constants.DEFAULT_ENCRYPTED_FIELD_INDEX);
        }

        /*
         * @see org.apache.flume.interceptor.Interceptor.Builder#build()
         */
        public Interceptor build() {

            return new CustomerParameterInterceptor(fields_separator, indexs, indexs_separator, encrypted_field_index);
        }
    }


    /**
     * 字符串md5加密
     */
    public static class StringUtils {
        // 全局数组
        private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

        // 返回形式为数字跟字符串
        private static String byteToArrayString(byte bByte) {
            int iRet = bByte;
            // System.out.println("iRet="+iRet);
            if (iRet < 0) {
                iRet += 256;
            }
            int iD1 = iRet / 16;
            int iD2 = iRet % 16;
            return strDigits[iD1] + strDigits[iD2];
        }

        // 返回形式只为数字
        private static String byteToNum(byte bByte) {
            int iRet = bByte;
            System.out.println("iRet1=" + iRet);
            if (iRet < 0) {
                iRet += 256;
            }
            return String.valueOf(iRet);
        }

        // 转换字节数组为16进制字串
        private static String byteToString(byte[] bByte) {
            StringBuffer sBuffer = new StringBuffer();
            for (int i = 0; i < bByte.length; i++) {
                sBuffer.append(byteToArrayString(bByte[i]));
            }
            return sBuffer.toString();
        }

        public static String GetMD5Code(String strObj) {
            String resultString = null;
            try {
                resultString = new String(strObj);
                MessageDigest md = MessageDigest.getInstance("MD5");
                // md.digest() 该函数返回值为存放哈希值结果的byte数组
                resultString = byteToString(md.digest(strObj.getBytes()));
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            return resultString;
        }
    }

    /**
     * The Class Constants.
     */
    public static class Constants {
        /**
         * The Constant FIELD_SEPARATOR.
         */
        public static final String FIELD_SEPARATOR = "fields_separator";

        /**
         * The Constant DEFAULT_FIELD_SEPARATOR.
         */
        public static final String DEFAULT_FIELD_SEPARATOR = " ";

        /**
         * The Constant INDEXS.
         */
        public static final String INDEXS = "indexs";

        /**
         * The Constant DEFAULT_INDEXS.
         */
        public static final String DEFAULT_INDEXS = "0";

        /**
         * The Constant INDEXS_SEPARATOR.
         */
        public static final String INDEXS_SEPARATOR = "indexs_separator";

        /**
         * The Constant DEFAULT_INDEXS_SEPARATOR.
         */
        public static final String DEFAULT_INDEXS_SEPARATOR = ",";

        /**
         * The Constant ENCRYPTED_FIELD_INDEX.
         */
        public static final String ENCRYPTED_FIELD_INDEX = "encrypted_field_index";

        /**
         * The Constant DEFAUL_TENCRYPTED_FIELD_INDEX.
         */
        public static final String DEFAULT_ENCRYPTED_FIELD_INDEX = "";

        /**
         * The Constant PROCESSTIME.
         */
        public static final String PROCESSTIME = "processTime";
        /**
         * The Constant PROCESSTIME.
         */
        public static final String DEFAULT_PROCESSTIME = "a";
    }
}
