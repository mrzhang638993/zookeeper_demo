package cn.itcast.zookeeper_api.es;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
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
 */
public class EsStudy {

    private TransportClient client = null;

    /**
     * 获取客户端对象
     */
    @Before
    public void getConnect() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "myes").build();
        TransportAddress node01 = new TransportAddress(InetAddress.getByName("node01"), 9300);
        TransportAddress node02 = new TransportAddress(InetAddress.getByName("node02"), 9300);
        TransportAddress node03 = new TransportAddress(InetAddress.getByName("node03"), 9300);
        client = new PreBuiltTransportClient(settings).
                addTransportAddress(node01)
                .addTransportAddress(node02)
                .addTransportAddress(node03);
        System.out.println(client.toString());
    }


    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }


    /**
     * 创建索引库操作
     */
    @Test
    public void createIndex() {
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"travelying out Elasticsearch\"" +
                "}";
        //  创建请求对象
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "1").setSource(json, XContentType.JSON);
        // 指定索引创建的请求
        IndexResponse indexResponse = indexRequestBuilder.get();
    }

    /**
     * 使用map创建索引进行操作
     */
    @Test
    public void createIndexByMap() {
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "2");
        Map map = new HashMap<>();
        map.put("user", "zhangsan");
        map.put("postDate", "2020-08-11");
        map.put("message", "索引的map创建操作");
        // 执行请求，获取相应操作
        IndexResponse indexResponse = indexRequestBuilder.setSource(map).get();
        String index = indexResponse.getIndex();
        System.out.println(index);
    }

    /**
     * 使用XcontentBuilder 进行操作构建操作
     */
    @Test
    public void xContentBuilderTest() throws IOException {
        XContentBuilder builder = new XContentFactory().jsonBuilder().startObject().
                field("name", "lisi")
                .field("age", 55)
                .field("address", "北京")
                .endObject();
        //  get操作触发请求的执行
        IndexResponse indexResponse = client.prepareIndex("myindex1", "article", "3").setSource(builder).get();
    }

    /**
     * java对象转化为json格式的字符串
     */
    @Test
    public void beanIndexCreate() {
        Person person = new Person();
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
     */
    @Test
    public void testBatchInsert() {
        // 获取预编译的bulkBuilder对象
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        Person person = new Person();
        person.setAddress("北京");
        person.setAge(25);
        person.setId(5);
        String content = JSON.toJSONString(person);


        Person person1 = new Person();
        person1.setAddress("北京");
        person1.setAge(25);
        person1.setId(6);
        String content1 = JSON.toJSONString(person);


        Person person2 = new Person();
        person2.setAddress("北京");
        person2.setAge(25);
        person2.setId(7);
        String content2 = JSON.toJSONString(person);

        Person person3 = new Person();
        person3.setAddress("北京");
        person3.setAge(25);
        person3.setId(8);
        String content3 = JSON.toJSONString(person);


        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "5").setSource(content, XContentType.JSON);
        IndexRequestBuilder indexRequestBuilder1 = client.prepareIndex("myindex1", "article", "6").setSource(content1, XContentType.JSON);
        IndexRequestBuilder indexRequestBuilder2 = client.prepareIndex("myindex1", "article", "7").setSource(content2, XContentType.JSON);
        IndexRequestBuilder indexRequestBuilder3 = client.prepareIndex("myindex1", "article", "8").setSource(content3, XContentType.JSON);

        bulkRequestBuilder.add(indexRequestBuilder)
                .add(indexRequestBuilder1)
                .add(indexRequestBuilder2)
                .add(indexRequestBuilder3).get();

    }

    /**
     * 更新索引
     */
    @Test
    public void undateIndex() {
        Map<String, String> map = new HashMap();
        map.put("phone", "19988880221");
        client.prepareUpdate("myindex1", "article", "8").setDoc(map).get();
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        client.prepareDelete("myindex1", "article", "8").get();
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteAllIndex() {
        client.admin().indices().prepareDelete("myindex1").execute().actionGet();
    }


    /**
     * 给索引查询增加数据源
     */
    //@Test
    public void queryIndex() throws IOException {
        Settings settings = Settings.builder().
                put("cluster.name", "myes")
                //  开启es的嗅探机制
                .put("client.transport.sniff", true)
                .build();
        TransportAddress node01 = new TransportAddress(InetAddress.getByName("node01"), 9300);
        TransportAddress node02 = new TransportAddress(InetAddress.getByName("node02"), 9300);
        TransportAddress node03 = new TransportAddress(InetAddress.getByName("node03"), 9300);
        client = new PreBuiltTransportClient(settings).
                addTransportAddress(node01)
                .addTransportAddress(node02)
                .addTransportAddress(node03);
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                //  这个id是数据id的内容，不是系统的id属性的
                .startObject("id").field("type", "integer").endObject()
                .startObject("name").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .startObject("age").field("type", "integer").endObject()
                .startObject("sex").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .startObject("address").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .startObject("phone").field("type", "text").endObject()
                .startObject("email").field("type", "text").endObject()
                .startObject("say").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .endObject()
                .endObject();
        PutMappingRequest putmap = Requests.putMappingRequest("indexsearch").type("mysearch").source(mapping);
        //创建索引
        client.admin().indices().prepareCreate("indexsearch").execute().actionGet();
        //为索引添加映射
        client.admin().indices().putMapping(putmap).actionGet();


        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        Person lujunyi = new Person(2, "玉麒麟卢俊义", 28, 1, "水泊梁山", "17666666666", "lujunyi@itcast.com", "hello world今天天气还不错");
        Person wuyong = new Person(3, "智多星吴用", 45, 1, "水泊梁山", "17666666666", "wuyong@itcast.com", "行走四方，抱打不平");
        Person gongsunsheng = new Person(4, "入云龙公孙胜", 30, 1, "水泊梁山", "17666666666", "gongsunsheng@itcast.com", "走一个");
        Person guansheng = new Person(5, "大刀关胜", 42, 1, "水泊梁山", "17666666666", "wusong@itcast.com", "我的大刀已经饥渴难耐");
        Person linchong = new Person(6, "豹子头林冲", 18, 1, "水泊梁山", "17666666666", "linchong@itcast.com", "梁山好汉");
        Person qinming = new Person(7, "霹雳火秦明", 28, 1, "水泊梁山", "17666666666", "qinming@itcast.com", "不太了解");
        Person huyanzhuo = new Person(8, "双鞭呼延灼", 25, 1, "水泊梁山", "17666666666", "huyanzhuo@itcast.com", "不是很熟悉");
        Person huarong = new Person(9, "小李广花荣", 50, 1, "水泊梁山", "17666666666", "huarong@itcast.com", "打酱油的");
        Person chaijin = new Person(10, "小旋风柴进", 32, 1, "水泊梁山", "17666666666", "chaijin@itcast.com", "吓唬人的");
        Person zhisheng = new Person(13, "花和尚鲁智深", 15, 1, "水泊梁山", "17666666666", "luzhisheng@itcast.com", "倒拔杨垂柳");
        Person wusong = new Person(14, "行者武松", 28, 1, "水泊梁山", "17666666666", "wusong@itcast.com", "二营长。。。。。。");

        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "1")
                .setSource(JSONObject.toJSONString(lujunyi), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "2")
                .setSource(JSONObject.toJSONString(wuyong), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "3")
                .setSource(JSONObject.toJSONString(gongsunsheng), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "4")
                .setSource(JSONObject.toJSONString(guansheng), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "5")
                .setSource(JSONObject.toJSONString(linchong), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "6")
                .setSource(JSONObject.toJSONString(qinming), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "7")
                .setSource(JSONObject.toJSONString(huyanzhuo), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "8")
                .setSource(JSONObject.toJSONString(huarong), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "9")
                .setSource(JSONObject.toJSONString(chaijin), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "10")
                .setSource(JSONObject.toJSONString(zhisheng), XContentType.JSON)
        );
        bulkRequestBuilder.add(client.prepareIndex("indexsearch", "mysearch", "11")
                .setSource(JSONObject.toJSONString(wusong), XContentType.JSON)
        );
        bulkRequestBuilder.get();
    }

    /**
     * es数据查询操作
     */
    @Test
    public void getBySystemId() {
        //  根据每条数据的系统id实现查询操作
        GetResponse documentFields = client.prepareGet("indexsearch", "mysearch", "1").get();
        String id = documentFields.getId();
        System.out.println("系统id为======" + id);
        String sourceAsString = documentFields.getSourceAsString();
        System.out.println("获取到系统的content====" + sourceAsString);
    }

    /**
     * 查询索引库中的所有的数据
     */
    @Test
    public void queryAll() {
        MatchAllQueryBuilder matchAllQueryBuilder = new MatchAllQueryBuilder();
        SearchResponse searchResponse = client.
                //  指定需要查询的索引库
                        prepareSearch("indexsearch").
                // 指定需要查询的类型
                        setTypes("mysearch").
                // 指定查询的条件
                        setQuery(matchAllQueryBuilder).
                // 执行查询操作
                        get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println("获取到的系统id是=====" + id);
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 查询年龄在18~28之间的数据
     */
    @Test
    public void yearRange() {
        RangeQueryBuilder queryBuilder = new RangeQueryBuilder("age");
        queryBuilder.gte(18).lte(28);
        SearchResponse searchResponse = client.prepareSearch("indexsearch").setTypes("mysearch")
                .setQuery(queryBuilder).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println("获取到系统id====" + id);
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 实现分词查询
     */
    @Test
    public void termQuery() {
        TermQueryBuilder termQueryBuilder=new TermQueryBuilder("say","熟悉");
        SearchResponse searchResponse = client.prepareSearch("indexsearch").setTypes("mysearch")
                .setQuery(termQueryBuilder).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println("获取到系统id====" + id);
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 实现模糊查询
     * */
    @Test
    public void fuzzyQuery(){

    }
}
