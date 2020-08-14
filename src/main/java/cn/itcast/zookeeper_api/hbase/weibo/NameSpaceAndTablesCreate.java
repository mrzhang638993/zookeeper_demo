package cn.itcast.zookeeper_api.hbase.weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
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

        nameSpaceAndTablesCreate.publishWeiboContent("1","今天天气还不错");
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
