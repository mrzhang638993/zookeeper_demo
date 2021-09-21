package cn.itcast.zookeeper_api.es.hbase;

import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ES2Hbase {

    public static void main(String[] args) throws IOException {
        //  获取excel文件的全部的内容
        List<Article> exceInfo = ExcelUtil.getExceInfo();
        Connection connection = hbaseConnection();
        //  批量保存数据到es中
        // save2es(exceInfo);
        //  数据保存到hbase中去.保存到hbase的es.hase中的
        //save2hbase(exceInfo,connection);
        //  关键字进行搜索查询： 搜索机器人查询操作
        TransportClient connect = getConnect();
        //  获取es的查询结果
        SearchResponse searchResponse = connect.prepareSearch("articles").setTypes("article")
                .setQuery(QueryBuilders.termQuery("title", "机器人"))
                .get();
        //根据es的查询结果获取到对应的id，到hbase中根据id查询到相关的数据的
        List<String> ids = new ArrayList<>();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String id = (String) sourceAsMap.get("id");
            ids.add(id);
        }
        // 根据id到hbase中执行查询操作
        Table table = connection.getTable(TableName.valueOf("es:hbase"));
        List<Get> gets = new ArrayList<>();
        for (String id : ids) {
            Get get = new Get(id.getBytes());
            gets.add(get);
        }
        Result[] results = table.get(gets);
        for (Result result : results) {
            List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                String name = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                if (name.equals("content")) {
                    byte[] bytes = CellUtil.cloneValue(cell);
                    System.out.println("=====" + Bytes.toString(cell.getQualifierArray()) + "====" + Bytes.toString(bytes));
                }
            }
        }
        connection.close();
    }

    public static void save2hbase(List<Article> exceInfo, Connection connection) throws IOException {
        //  问题: connection无法关闭的
        Table table = hbaseClient(connection);
        List<Put> puts = new ArrayList<>();
        for (Article article : exceInfo) {
            Put put = new Put(article.getId().getBytes());
            put.addColumn("f1".getBytes(), "title".getBytes(), article.getTitle().getBytes());
            put.addColumn("f1".getBytes(), "from".getBytes(), article.getFrom().getBytes());
            put.addColumn("f1".getBytes(), "times".getBytes(), article.getTimes().getBytes());
            put.addColumn("f1".getBytes(), "readCounts".getBytes(), article.getReadCounts().getBytes());
            put.addColumn("f1".getBytes(), "content".getBytes(), article.getContent().getBytes());
            puts.add(put);
        }
        table.put(puts);
        table.close();
    }

    public static Table hbaseClient(Connection connection) throws IOException {
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf("es:hbase");
        // 创建namespace
        NamespaceDescriptor build = NamespaceDescriptor.create("es").addConfiguration("name", "jim").build();
        admin.createNamespace(build);
        //  对应的表不存在的话，创建表
        if (!admin.tableExists(tableName)) {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            //  创建列族信息
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor("f1");
            //设置版本的上界和下界
            hColumnDescriptor.setMaxVersions(1);
            hColumnDescriptor.setMinVersions(1);
            // 设置块大小 //  设置2m的大小
            hColumnDescriptor.setBlocksize(2048 * 1024);
            hColumnDescriptor.setCacheBloomsOnWrite(true);
            //设置数据的压缩
            //hColumnDescriptor.setCompactionCompressionType(Compression.Algorithm.SNAPPY);
            hTableDescriptor.addFamily(hColumnDescriptor);
            admin.createTable(hTableDescriptor);
        }
        admin.close();
        Table table = connection.getTable(TableName.valueOf("es:hbase"));
        return table;
    }

    public static Connection hbaseConnection() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection;
    }

    public static void save2es(List<Article> exceInfo) throws UnknownHostException {
        TransportClient connect = getConnect();
        // es的批量保存
        BulkRequestBuilder bulkRequestBuilder = connect.prepareBulk();
        Gson gson = new Gson();
        for (Article article : exceInfo) {
            String content = gson.toJson(article);
            IndexRequestBuilder indexRequestBuilder = connect.prepareIndex("articles", "article", article.getId()).setSource(content, XContentType.JSON);
            bulkRequestBuilder.add(indexRequestBuilder);
        }
        // 执行es的批量保存数据的操作
        BulkResponse bulkItemResponses = bulkRequestBuilder.get();
        connect.close();
    }

    public static TransportClient getConnect() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "myes").build();
        TransportAddress node01 = new TransportAddress(InetAddress.getByName("node01"), 9300);
        TransportAddress node02 = new TransportAddress(InetAddress.getByName("node02"), 9300);
        TransportAddress node03 = new TransportAddress(InetAddress.getByName("node03"), 9300);
        TransportClient client = new PreBuiltTransportClient(settings).
                addTransportAddress(node01)
                .addTransportAddress(node02)
                .addTransportAddress(node03);
        return client;
    }


}
