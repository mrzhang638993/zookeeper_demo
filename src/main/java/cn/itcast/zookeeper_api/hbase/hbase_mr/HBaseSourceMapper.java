package cn.itcast.zookeeper_api.hbase.hbase_mr;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.List;

/**
 * 读取hbase的表myuser中的数据
 * KEYOUT, k2,对应的是rowkey
 * VALUEOUT,v2，对应的是数据
 *
 * */
public class HBaseSourceMapper  extends TableMapper<Text, Put> {

    /**
     * 需求:读取f1下面的name个age的列字段
     * */
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        //  key  ImmutableBytesWritable 对应的是rowkey
        //  value  对应的是数据对象的
        byte[] bytes = key.get();
        String rowkey = Bytes.toString(bytes);
        //  获取到所有的cell。
        List<Cell> cells = value.listCells();
        Put  put=new Put(bytes);
        for(Cell cell:cells){
            // 可以使用 CellUtil.cloneFamily()
            String content= Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
            if (content.equals("f1")){
                // 对应的获取列族f1下面的列名称,name的内容以及age的内容
                String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                if (qualifier.equals("name")){
                    put.add(cell);
                }else if (qualifier.equals("age")){
                    put.add(cell);
                }
            }
        }
        //  put的内容不能为空的话。对应的才可以写入数据的。
        if (!put.isEmpty()) {
            context.write(new Text(rowkey), put);
        }
    }
}
