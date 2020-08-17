package cn.itcast.zookeeper_api.es;


import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.jcodings.util.Hash;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用javaapi操作es集群
 * */
public class EsStudy {

    private  TransportClient client=null;

    /**
     * 获取客户端对象
     * */
    @Before
    public void  getConnect() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name","myes").build();
        TransportAddress node01 = new TransportAddress(InetAddress.getByName("node01"), 9300);
        TransportAddress node02 = new TransportAddress(InetAddress.getByName("node02"), 9300);
        TransportAddress node03 = new TransportAddress(InetAddress.getByName("node03"), 9300);
        client=new PreBuiltTransportClient(settings).
                addTransportAddress(node01)
                .addTransportAddress(node02)
                .addTransportAddress(node03);
        System.out.println(client.toString());
    }


    @After
    public void  close(){
        if (client!=null){
            client.close();
        }
    }


    /**
     * 创建索引库操作
     * */
    @Test
    public  void   createIndex(){
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"travelying out Elasticsearch\"" +
                "}";
        //  创建请求对象
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "1").setSource(json, XContentType.JSON);
        // 指定索引创建的请求
        IndexResponse indexResponse =indexRequestBuilder .get();
    }

    /**
     * 使用map创建索引进行操作
     * */
    @Test
    public void  createIndexByMap(){
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "2");
        Map map=new HashMap<>();
        map.put("user","zhangsan");
        map.put("postDate","2020-08-11");
        map.put("message","索引的map创建操作");
        // 执行请求，获取相应操作
        IndexResponse indexResponse = indexRequestBuilder.setSource(map).get();
        String index = indexResponse.getIndex();
        System.out.println(index);
    }

    /**
     * 使用XcontentBuilder 进行操作构建操作
     * */
    @Test
    public void  xContentBuilderTest() throws IOException {
        XContentBuilder builder = new XContentFactory().jsonBuilder().startObject().
                field("name", "lisi")
                .field("age",55)
                .field("address","北京")
                .endObject();
        //  get操作触发请求的执行
        IndexResponse indexResponse = client.prepareIndex("myindex1", "article", "3").setSource(builder).get();
    }

    /**
     * java对象转化为json格式的字符串
     * */
    @Test
    public void beanIndexCreate(){
        Person person=new Person();
        person.setAddress("北京");
        person.setAge(25);
        person.setEmail("13@163.com");
        person.setPhone("13392112455");
        String content = JSON.toJSONString(person);
        //  触发请求执行操作
        IndexResponse indexResponse = client.prepareIndex("myindex1", "article", "4").setSource(content, XContentType.JSON).get();
    }

    /**
     * 批量增加数据的操作
     * */
    @Test
    public void testBatchInsert(){
        // 获取预编译的bulkBuilder对象
        BulkRequestBuilder bulkRequestBuilder=client.prepareBulk();
        
    }
}
