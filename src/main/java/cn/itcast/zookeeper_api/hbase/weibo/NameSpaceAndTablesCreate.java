package cn.itcast.zookeeper_api.hbase.weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NameSpaceAndTablesCreate {

    public static void main(String[] args) throws IOException {
        NameSpaceAndTablesCreate nameSpaceAndTablesCreate=new NameSpaceAndTablesCreate();
        // 初始化命名空间
        //nameSpaceAndTablesCreate.initNamespace();
        //  创建三张hbase表
        //nameSpaceAndTablesCreate.createWeiboContent();
        //nameSpaceAndTablesCreate.createRelationTable();
        //nameSpaceAndTablesCreate.createTableReceiveEmails();

        //nameSpaceAndTablesCreate.publishWeiboContent("2","今天天气还不错222");
        //nameSpaceAndTablesCreate.addUserAttention("1","2","3","M");
        nameSpaceAndTablesCreate.cancelConcern("1","2");
    }

    /**
     * 创建微博内容表： weibo:content
     * */
    public  void createWeiboContent() throws IOException {
        Connection connection =initConnection();
        Admin admin = connection.getAdmin();
        TableName tableName=TableName.valueOf("weibo:content");
        //  对应的表不存在的话，创建表
        if (!admin.tableExists(tableName)){
            HTableDescriptor hTableDescriptor=new HTableDescriptor(tableName);
            //  创建列族信息
            HColumnDescriptor hColumnDescriptor=new HColumnDescriptor("info");
            //设置版本的上界和下界
            hColumnDescriptor.setMaxVersions(1);
            hColumnDescriptor.setMinVersions(1);
            // 设置块大小 //  设置2m的大小
            hColumnDescriptor.setBlocksize(2048*1024);
            hColumnDescriptor.setCacheBloomsOnWrite(true);
            //设置数据的压缩
            //hColumnDescriptor.setCompactionCompressionType(Compression.Algorithm.SNAPPY);
            hTableDescriptor.addFamily(hColumnDescriptor);
            admin.createTable(hTableDescriptor);
        }
        admin.close();
        connection.close();
    }

    /**
     *  创建用户关系表
     *  方法名	createTableRelations
     * Table Name	weibo:relations
     * RowKey	用户ID
     * ColumnFamily	attends、fans
     * ColumnLabel	关注用户ID，粉丝用户ID
     * ColumnValue	用户ID
     * Version	1个版本
     * 代码实现：
     * */
    public void createRelationTable() throws IOException {
        Connection connection =initConnection();
        Admin admin = connection.getAdmin();
        TableName tableName=TableName.valueOf("weibo:relations");
        if (!admin.tableExists(tableName)){
            //  表不存在的话。创建hbase的表数据
            HTableDescriptor hTableDescriptor=new HTableDescriptor(tableName);
            //  创建列族信息
            HColumnDescriptor attends=new HColumnDescriptor("attends");
            attends.setMinVersions(1);
            attends.setMaxVersions(1);
            attends.setBlockCacheEnabled(true);
            //  设置2m的大小
            attends.setBlocksize(2048*1024);
            HColumnDescriptor fans=new HColumnDescriptor("fans");
            fans.setMinVersions(1);
            fans.setMaxVersions(1);
            fans.setBlockCacheEnabled(true);
            fans.setBlocksize(2048*1024);
            //
            hTableDescriptor.addFamily(attends);
            hTableDescriptor.addFamily(fans);
            admin.createTable(hTableDescriptor);
        }
        admin.close();
        connection.close();
    }

    /**
     * 创建微博的收件箱表
     * Table Name	weibo:receive_content_email
     * RowKey	用户ID
     * ColumnFamily	info
     * ColumnLabel	用户ID
     * ColumnValue	取微博内容的RowKey
     * Version	1000
     * */
    public void createTableReceiveEmails() throws IOException {
        Connection connection =initConnection();
        Admin admin = connection.getAdmin();
        TableName tableName=TableName.valueOf("weibo:receive_content_email");
        if (!admin.tableExists(tableName)){
            //  表不存在的话。创建hbase的表数据
            HTableDescriptor hTableDescriptor=new HTableDescriptor(tableName);
            //  创建列族信息
            HColumnDescriptor info=new HColumnDescriptor("info");
            //  可以保存1000个记录的。一个cell中可以查看一个用户的1000条微博记录的
            info.setMinVersions(1000);
            info.setMaxVersions(1000);
            info.setBlockCacheEnabled(true);
            //  设置2m的大小
            hTableDescriptor.addFamily(info);
            admin.createTable(hTableDescriptor);
        }
        admin.close();
        connection.close();
    }

    /**
     * 用户id
     * 发布微博的内容
     * */
    public void publishWeiboContent(String userid,String content) throws IOException {
        // 步骤一：将发布的微博的内容保存到content中
        Connection connection = initConnection();
        TableName tableName = TableName.valueOf("weibo:content");
        Table table = connection.getTable(tableName);
        // 解析内容，封装put操作。获取微博的id
        Put put=new Put((userid+"_"+System.currentTimeMillis()).getBytes());
        //  解析微博的内容
        put.addColumn("info".getBytes(),"content".getBytes(),System.currentTimeMillis(),content.getBytes());
        table.put(put);
        //  步骤二： 查看用户id对应的所有的粉丝信息，需要查询relation关系
        TableName relation = TableName.valueOf("weibo:relations");
        Table relate = connection.getTable(relation);
        Get get=new Get(userid.getBytes());
        //  只查询fans的列信息
        get.addFamily("fans".getBytes());
        Result result = relate.get(get);
        List<Cell> cells = result.listCells();
        TableName email = TableName.valueOf("weibo:receive_content_email");
        Table table1 = connection.getTable(email);
        if (cells==null||cells.size()<0){
            return ;
        }
        //给weibo:receive_content_email 增加记录  其中rowkey对应的是粉丝的id的
        for(Cell cell:cells){
                //微博的粉丝。获取的是粉丝的uid
                String uid = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                // 粉丝的id作为rowkey
                Put  put1=new Put(uid.getBytes());
                //  对应的是userid作为key，微博的id作为value的，执行的是更新的操作的,内部保存了.多版本的操作必须要时间戳的。
                put1.addColumn("info".getBytes(),userid.getBytes(),System.currentTimeMillis(),(userid+"_"+System.currentTimeMillis()).getBytes());
                // 还需要对应的的修改微博的内容的.
                table1.put(put1);
        }
        table1.close();
        table.close();
        connection.close();
    }

    /**
     * 添加用户关注
     * userId  用户id
     * attentionId ：被关注的用户id
     * A关注了用户B,C,D,即A用户是B,C,D的粉丝
     * 可变参数的使用
     * 步骤一：需要在weibo:relations记录关注关系。A作为rowkey的，B,C,D作为列值保存到attends中的
     * 同时B,C,D多了一个粉丝的，需要与B,C,D作为rowkey，A作为列名和列值保存到fans中的
     * 步骤二：需要在weibo:receive_content_email中增加关注的微博记录。增加如下的记录。A的用户id作为rowkey，B,C,D作为列名称，同时对应的微博id作为列值
     *  问题：知道了B,C,D的用户id，怎么查询到对应的微博的rowkey的，可以实现前缀过滤器查询的。
     * */
    public void addUserAttention(String userId,String ...attentionIds) throws IOException {
        if (attentionIds.length==0){
            return ;
        }else {
            Connection connection = initConnection();
            TableName tableName = TableName.valueOf("weibo:relations");
            Table table = connection.getTable(tableName);
            List<Put> puts=new ArrayList<>();
            TableName content = TableName.valueOf("weibo:content");
            Table table1 = connection.getTable(content);
            FilterList filters=new FilterList();
            Scan scan=new Scan();
            for (String attenId:attentionIds) {
                //  A关注了B,C,D
                // 对应的是A的代码的
                 Put put=new Put(userId.getBytes());
                 put.addColumn("attends".getBytes(),attenId.getBytes(),attenId.getBytes());
                 puts.add(put);
                 //  对应的下面是A作为分析的内容的
                Put put1=new Put(attenId.getBytes());
                put1.addColumn("fans".getBytes(),userId.getBytes(),userId.getBytes());
                puts.add(put1);
                // 前缀匹配操作，匹配rowkey查询。
                RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(attenId + "_"));
                //PrefixFilter prefixFilter = new PrefixFilter(attenId.getBytes());
                filters.addFilter(rowFilter);
            }
            table.put(puts);
            // 下面执行的是weibo:content 的帮助信息
            scan.setFilter(filters);
            ResultScanner scanner = table1.getScanner(scan);
            if (scanner==null){
                return ;
            }else {
                TableName email = TableName.valueOf("weibo:receive_content_email");
                Table table2 = connection.getTable(email);
                List<Put>puts1=new ArrayList<>();
                for (Result result:scanner) {
                    //  获取rowkey
                    byte[] row = result.getRow();
                    String rowkey = Bytes.toString(row);
                    Put put=new Put(userId.getBytes());
                    String[] splitValue= rowkey.split("_");
                    //  还需要获取到时间戳信息。这个很关键的，涉及到版本的上限和下限的。
                    put.addColumn("info".getBytes(),splitValue[0].getBytes(),Long.valueOf(splitValue[1]),rowkey.getBytes());
                    puts1.add(put);
                }
                table2.put(puts1);
                table2.close();
            }
            table1.close();
            table.close();
            connection.close();
        }
    }


    /**
     * 用户取消关注的代码
     * 步骤一：需要在weibo:relations删除相关的关注关系。包括attend的代码以及fans的代码。
     * 步骤二：需要删除weibo:receive_content_email中的关注的微博的记录。
     * */
    public void cancelConcern(String userId,String ...attend) throws IOException {
        Connection connection = initConnection();
        TableName tableName = TableName.valueOf("weibo:relations");
        Table table = connection.getTable(tableName);
        TableName email = TableName.valueOf("weibo:receive_content_email");
        Table table2 = connection.getTable(email);
        if (attend.length<=0){
            return ;
        }
        //删除关注的对象的
        List<Delete> deletes=new ArrayList<>();
        List<Delete> deletes1=new ArrayList<>();
        for (String s : attend) {
            Delete delete=new Delete(userId.getBytes());
            delete.addColumn("attends".getBytes(),s.getBytes());
            deletes.add(delete);
            // 删除粉丝对象
            Delete delete1=new Delete(s.getBytes());
            delete1.addColumn("fans".getBytes(),userId.getBytes());
            deletes.add(delete1);
            Delete delete2=new Delete(userId.getBytes());
            delete2.addColumn("info".getBytes(),s.getBytes());
            deletes1.add(delete2);
        }
        table.delete(deletes);
        table2.delete(deletes1);
        table2.close();
        table.close();
        connection.close();
    }
    /**
     *  创建命名空间
     * */
    public  void initNamespace() throws IOException {
        Connection connection = initConnection();
        Admin admin = connection.getAdmin();
        //  创建namespace
        NamespaceDescriptor build = NamespaceDescriptor.create("weibo").addConfiguration("name", "jim").build();
        admin.createNamespace(build);
        admin.close();
        connection.close();
    }

    /**
     *  初始话连接
     * */
    private   Connection initConnection() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        Connection connection = ConnectionFactory.createConnection(configuration);
        return  connection;
    }
}
