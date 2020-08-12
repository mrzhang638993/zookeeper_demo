package cn.itcast.zookeeper_api.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseOperate {

    private  Connection connection;
    private Admin admin;
    private  Table table;

    @Before
    public   void init() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        //  指定 hbase的zk连接地址
        configuration.set("hbase.zookeeper.querum","node01:2181,node02:2181,node03:2181");
        //  获取hbase的连接
        connection = ConnectionFactory.createConnection(configuration);
        //获取管理员对象,创建hbase的数据表
        admin = connection.getAdmin();
    }

    /**
     *  创建hbase表格 user,列族f1，f2
     * */
    @Test
    public  void   createTable() throws IOException {
        HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf("myuser"));
        HColumnDescriptor f1 = new HColumnDescriptor("f1");
        HColumnDescriptor f2 = new HColumnDescriptor("f2");
        tableDescriptor.addFamily(f1);
        tableDescriptor.addFamily(f2);
        admin.createTable(tableDescriptor);
    }

    /**
     * 向myuser表中增加数据。
     * hbase中的添加和更新的操作是一样的，有数据就更新，没有数据就插入操作。
     * */
    @Test
    public   void addData() throws IOException {
        table = connection.getTable(TableName.valueOf("myuser"));
        Put put = new Put("0001".getBytes());
        put.addColumn("f1".getBytes(),"id".getBytes(), Bytes.toBytes(1));
        put.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("zhangsan"));
        put.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(18));
        put.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("地球人"));
        put.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("13392112455"));
        table.put(put);
    }

    /**
     * 批量插入数据到hbase中
     * */
    @Test
    public void insertBatchData() throws IOException {
        //获取表
        table = connection.getTable(TableName.valueOf("myuser"));
        //创建put对象，并指定rowkey
        Put put = new Put("0002".getBytes());
        put.addColumn("f1".getBytes(),"id".getBytes(),Bytes.toBytes(1));
        put.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("曹操"));
        put.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(30));
        put.addColumn("f2".getBytes(),"sex".getBytes(),Bytes.toBytes("1"));
        put.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("沛国谯县"));
        put.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("16888888888"));
        put.addColumn("f2".getBytes(),"say".getBytes(),Bytes.toBytes("helloworld"));

        Put put2 = new Put("0003".getBytes());
        put2.addColumn("f1".getBytes(),"id".getBytes(),Bytes.toBytes(2));
        put2.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("刘备"));
        put2.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(32));
        put2.addColumn("f2".getBytes(),"sex".getBytes(),Bytes.toBytes("1"));
        put2.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("幽州涿郡涿县"));
        put2.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("17888888888"));
        put2.addColumn("f2".getBytes(),"say".getBytes(),Bytes.toBytes("talk is cheap , show me the code"));


        Put put3 = new Put("0004".getBytes());
        put3.addColumn("f1".getBytes(),"id".getBytes(),Bytes.toBytes(3));
        put3.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("孙权"));
        put3.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(35));
        put3.addColumn("f2".getBytes(),"sex".getBytes(),Bytes.toBytes("1"));
        put3.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("下邳"));
        put3.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("12888888888"));
        put3.addColumn("f2".getBytes(),"say".getBytes(),Bytes.toBytes("what are you 弄啥嘞！"));

        Put put4 = new Put("0005".getBytes());
        put4.addColumn("f1".getBytes(),"id".getBytes(),Bytes.toBytes(4));
        put4.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("诸葛亮"));
        put4.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(28));
        put4.addColumn("f2".getBytes(),"sex".getBytes(),Bytes.toBytes("1"));
        put4.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("四川隆中"));
        put4.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("14888888888"));
        put4.addColumn("f2".getBytes(),"say".getBytes(),Bytes.toBytes("出师表你背了嘛"));

        Put put5 = new Put("0005".getBytes());
        put5.addColumn("f1".getBytes(),"id".getBytes(),Bytes.toBytes(5));
        put5.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("司马懿"));
        put5.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(27));
        put5.addColumn("f2".getBytes(),"sex".getBytes(),Bytes.toBytes("1"));
        put5.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("哪里人有待考究"));
        put5.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("15888888888"));
        put5.addColumn("f2".getBytes(),"say".getBytes(),Bytes.toBytes("跟诸葛亮死掐"));


        Put put6 = new Put("0006".getBytes());
        put6.addColumn("f1".getBytes(),"id".getBytes(),Bytes.toBytes(5));
        put6.addColumn("f1".getBytes(),"name".getBytes(),Bytes.toBytes("xiaobubu—吕布"));
        put6.addColumn("f1".getBytes(),"age".getBytes(),Bytes.toBytes(28));
        put6.addColumn("f2".getBytes(),"sex".getBytes(),Bytes.toBytes("1"));
        put6.addColumn("f2".getBytes(),"address".getBytes(),Bytes.toBytes("内蒙人"));
        put6.addColumn("f2".getBytes(),"phone".getBytes(),Bytes.toBytes("15788888888"));
        put6.addColumn("f2".getBytes(),"say".getBytes(),Bytes.toBytes("貂蝉去哪了"));
        List<Put> listPut = new ArrayList<Put>();
        listPut.add(put);
        listPut.add(put2);
        listPut.add(put3);
        listPut.add(put4);
        listPut.add(put5);
        listPut.add(put6);
        table.put(listPut);
        table.close();
    }

    /**
     * hbase查询数据操作
     * 根据rowkey进行查询操作
     * */
    @Test
    public void  queryData() throws IOException {
        table = connection.getTable(TableName.valueOf("myuser"));
         //  对应的是不bytes的代码的,0003对应的是rowkey的
        Get get=new Get("0003".getBytes());
        //  查询操作的时候增加列族查询操作实现
        get.addFamily("f1".getBytes());
        //  设置列族f1，以及列的名称id的数值
        get.addColumn("f1".getBytes(),"id".getBytes());
        //  封装了所有的结果数据的。
        Result result = table.get(get);
        //  获取结果中所有的cells数值
        printResult(result);
    }

    /**
     * 通过scan进行查询操作
     * */
    @Test
    public void scanQuery() throws IOException {
        table = connection.getTable(TableName.valueOf("myuser"));
        Scan scan=new Scan();
        //  scan扫描的话，包括前面的0004 不包括后面的0006的。全表扫描的话就是没有任何的条件的过滤的
        scan.setStartRow("0004".getBytes());
        scan.setStopRow("0006".getBytes());
        ResultScanner scanner = table.getScanner(scan);
        for (Result result :scanner) {
            printResult(result);
        }
    }

    /**
     * scan的过滤器查询.
     * 通过过滤器，可以实现更加强大的hbase的过滤功能和实现逻辑
     * 小于0003的
     * */
    @Test
    public  void  filterQuery() throws IOException {
        table = connection.getTable(TableName.valueOf("myuser"));
        Scan scan=new Scan();
        RowFilter rowFilter = new RowFilter(CompareOperator.LESS, new BinaryComparator(Bytes.toBytes("0003")));
        scan.setFilter(rowFilter);
        ResultScanner results = table.getScanner(scan);
        for(Result result:results){
            printResult(result);
        }
    }

    /**
     * scan的进一步的过滤操作实现
     * */
    @Test
    public void familyFilter(){
       // 对应的实现相关的代码查询操作
    }

    private  void  printResult(Result result){
        List<Cell> cells = result.listCells();
        for (Cell cell :cells) {
            // 获取cell对应的rowkey
            String rowkey= Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
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

    @After
    public  void close() throws IOException {
        if (admin!=null){
            admin.close();
        }
        if (connection!=null){
            connection.close();
        }
        if (table!=null){
            table.close();
        }
    }
}
