package cn.itcast.zookeeper_api.hbase.weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class NameSpaceAndTablesCreate {

    public static void main(String[] args) throws IOException {
        NameSpaceAndTablesCreate nameSpaceAndTablesCreate=new NameSpaceAndTablesCreate();
        // 初始化命名空间
        nameSpaceAndTablesCreate.initNamespace();
        //  创建三张hbase表
        nameSpaceAndTablesCreate.createWeiboContent();
        nameSpaceAndTablesCreate.createRelationTable();
        nameSpaceAndTablesCreate.createTableReceiveEmails();
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
            info.setMinVersions(1);
            info.setMaxVersions(1);
            info.setBlockCacheEnabled(true);
            //  设置2m的大小
            hTableDescriptor.addFamily(info);
            admin.createTable(hTableDescriptor);
        }
        admin.close();
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
