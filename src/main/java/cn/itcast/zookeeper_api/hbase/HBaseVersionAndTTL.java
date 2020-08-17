package cn.itcast.zookeeper_api.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseVersionAndTTL {


    public static void main(String[] args) throws IOException, InterruptedException {
        //操作hbase，向hbase的表中增加一条数据，并且设置数据的上界和下界，以及设置数据的ttl过期时间
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        Connection connection = ConnectionFactory.createConnection(configuration);
        //  创建一个hbase表
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf("version_hbase");
        // 表不存在的话，对应的执行创建表的操作实现
        if (!admin.tableExists(tableName)) {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            HColumnDescriptor f1 = new HColumnDescriptor("f1");
            // 设置版本的上界和下界。最大版本保留5个，最小保留3个版本的。这3个版本永远不会过期的。
            f1.setMinVersions(3);
            f1.setMaxVersions(5);
            //  设置f1的最大存活时间为30秒
            f1.setTimeToLive(30);
            hTableDescriptor.addFamily(f1);
            admin.createTable(hTableDescriptor);
        }
        Table table = connection.getTable(tableName);
        Put put = new Put("1".getBytes());
        //  需要多个版本的话，一定要增加时间戳参数的.
        put.addColumn("f1".getBytes(), "name".getBytes(), System.currentTimeMillis(), "zhangsan".getBytes());
        table.put(put);
        Thread.sleep(1000);
        Put put2 = new Put("1".getBytes());
        put2.addColumn("f1".getBytes(), "name".getBytes(), System.currentTimeMillis(), "zhangsan2".getBytes());
        table.put(put2);
        Thread.sleep(1000);
        Put put3 = new Put("1".getBytes());
        put3.addColumn("f1".getBytes(), "name".getBytes(), System.currentTimeMillis(), "zhangsan3".getBytes());
        table.put(put3);
        Thread.sleep(1000);
        Put put4 = new Put("1".getBytes());
        put4.addColumn("f1".getBytes(), "name".getBytes(), System.currentTimeMillis(), "zhangsan4".getBytes());
        table.put(put4);
        Thread.sleep(1000);
        Put put5 = new Put("1".getBytes());
        put5.addColumn("f1".getBytes(), "name".getBytes(), System.currentTimeMillis(), "zhangsan5".getBytes());
        table.put(put5);
        Thread.sleep(1000);
        Put put6 = new Put("1".getBytes());
        // 针对于某一条数据设置过期时间.但是不能设置maxVersion以及minVersion
        //put6.setTTL(1000);
        put6.addColumn("f1".getBytes(), "name".getBytes(), System.currentTimeMillis(), "zhangsan6".getBytes());
        table.put(put6);

        // 获取多个版本的对象数据
        Get get = new Get("1".getBytes());
        //get.setMaxVersions(5); //  不加上任何参数的话，对应的是所有的版本的。
        get.setMaxVersions();
        Result result = table.get(get);
        printResult(result);
        table.close();
        connection.close();
    }

    private static void printResult(Result result) {
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            // 获取cell对应的rowkey
            String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
            System.out.println(rowkey);
            //  获取cell对应的列族的名称
            String columnArray = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
            System.out.println(columnArray);
            //  获取列的名称
            String columnName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            if (columnArray.equals("f1") && (columnName.equals("id") | columnName.equals("age"))) {
                int value = Bytes.toInt(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                //获取到列对应的数值
                System.out.println(value);
            } else {
                String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                //获取到列对应的数值
                System.out.println(value);
            }
            // 获取列的名称
            System.out.println(columnName);
        }
    }
}
