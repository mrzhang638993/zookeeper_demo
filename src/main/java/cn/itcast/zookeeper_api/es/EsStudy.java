package cn.itcast.zookeeper_api.es;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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
        XContentBuilder mapping = jsonBuilder()
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
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("say", "熟悉");
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
     * 可以纠正输入错误，最大的纠正次数为2次的
     */
    @Test
    public void fuzzyQuery() {
        // 设置可以允许出现2次错误的操作的。
        FuzzyQueryBuilder termQueryBuilder = new FuzzyQueryBuilder("say", "heolo").fuzziness(Fuzziness.TWO);
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
     * 通配符查询操作
     * * 表示匹配任意多的字符
     * ? 表示匹配单个的字符
     */
    @Test
    public void wildQuery() {
        // QueryBuilders对应的工具类的，可以获取到很多的queryBuilder数据的。
        WildcardQueryBuilder say = QueryBuilders.wildcardQuery("say", "hel*");
        SearchResponse searchResponse = client.prepareSearch("indexsearch").setTypes("mysearch")
                .setQuery(say).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println("获取到系统id====" + id);
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 多条件的组合查询  boolean query
     * 条件1：查询年龄在18到28之间的男性
     * 条件2：id范围在10~13之间的数据
     * booleanQuery 组合多个条件实现查询操作。
     */
    @Test
    public void booleanQuery() {
        // 第一个条件的部分
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("age").gte(18).lte(28);
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("sex", "1");
        // 第二个查询条件
        RangeQueryBuilder idRange = new RangeQueryBuilder("id").gte(10).lte(13);
        //  组织条件实现查询操作
        SearchResponse searchResponse = client.prepareSearch("indexsearch").setTypes("mysearch")
                .setQuery(QueryBuilders.boolQuery().should(idRange).should(QueryBuilders.boolQuery().must(rangeQueryBuilder).must(matchQueryBuilder))).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println("获取到系统id====" + id);
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 分页查询
     * 使用from +size的方式实现分页操作
     */
    @Test
    public void pageQuery() {
        int pageSize = 5;
        int pageNum = 2;
        // 计算初始的index数据
        int startNum = (pageNum - 1) * pageNum;
        // 获取分页的数据
        SearchResponse searchResponse = client.prepareSearch("indexsearch").setTypes("mysearch").
                setQuery(QueryBuilders.matchAllQuery()).addSort("id", SortOrder.ASC)
                .setFrom(startNum).setSize(pageSize).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println("获取到系统id====" + id);
            System.out.println(hit.getSourceAsString());
        }
    }


    /**
     * 高亮查询操作
     * 对于查询出来的say字段对应的是hello的进行高亮的显示操作
     */
    @Test
    public void highNight() {
        MatchQueryBuilder say = QueryBuilders.matchQuery("say", "hello");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //  定义对那个字段进行高亮的显示以及设置前缀和后缀字段
        highlightBuilder.field("say").preTags("<font style='color:red'>").postTags("</font>");
        SearchResponse searchResponse = client.prepareSearch("indexsearch").
                setTypes("mysearch")
                .setQuery(say)
                .highlighter(highlightBuilder).get();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            System.out.println("======" + hit.getSourceAsString());
            // 显示的是高亮的数据内容的。
            Text[] says = hit.getHighlightFields().get("say").getFragments();
            for (Text text : says) {
                System.out.println(text);
            }
        }
    }

    /**
     * 批量添加数据
     *
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void addIndexDatas() throws IOException, ExecutionException, InterruptedException {
        //获取settings
        //配置es集群的名字
        Settings settings = Settings.builder().put("cluster.name", "myes").build();
        //获取客户端
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("node01"), 9300);

        TransportAddress transportAddress2 = new TransportAddress(InetAddress.getByName("node02"), 9300);

        TransportAddress transportAddress3 = new TransportAddress(InetAddress.getByName("node03"), 9300);
        //获取client客户端
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress).addTransportAddress(transportAddress2).addTransportAddress(transportAddress3);

        /**
         * 创建索引
         * */
        client.admin().indices().prepareCreate("player").get();
        //构建json的数据格式，创建映射
        XContentBuilder mappingBuilder = jsonBuilder()
                .startObject()
                .startObject("player")
                .startObject("properties")
                .startObject("name").field("type", "text").field("index", "true").field("fielddata", "true").endObject()
                .startObject("age").field("type", "integer").endObject()
                .startObject("salary").field("type", "integer").endObject()
                .startObject("team").field("type", "text").field("index", "true").field("fielddata", "true").endObject()
                .startObject("position").field("type", "text").field("index", "true").field("fielddata", "true").endObject()
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest request = Requests.putMappingRequest("player")
                .type("player")
                .source(mappingBuilder);
        client.admin().indices().putMapping(request).get();


        //批量添加数据开始

        BulkRequestBuilder bulkRequest = client.prepareBulk();

// either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("player", "player", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "郭德纲")
                        .field("age", 33)
                        .field("salary", 3000)
                        .field("team", "cav")
                        .field("position", "sf")
                        .endObject()
                )
        );
        bulkRequest.add(client.prepareIndex("player", "player", "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "于谦")
                        .field("age", 25)
                        .field("salary", 2000)
                        .field("team", "cav")
                        .field("position", "pg")
                        .endObject()
                )
        );
        bulkRequest.add(client.prepareIndex("player", "player", "3")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "岳云鹏")
                        .field("age", 29)
                        .field("salary", 1000)
                        .field("team", "war")
                        .field("position", "pg")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex("player", "player", "4")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "爱因斯坦")
                        .field("age", 21)
                        .field("salary", 300)
                        .field("team", "tim")
                        .field("position", "sg")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex("player", "player", "5")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "张云雷")
                        .field("age", 26)
                        .field("salary", 2000)
                        .field("team", "war")
                        .field("position", "pf")
                        .endObject()
                )
        );
        bulkRequest.add(client.prepareIndex("player", "player", "6")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "爱迪生")
                        .field("age", 40)
                        .field("salary", 1000)
                        .field("team", "tim")
                        .field("position", "pf")
                        .endObject()
                )
        );
        bulkRequest.add(client.prepareIndex("player", "player", "7")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "牛顿")
                        .field("age", 21)
                        .field("salary", 500)
                        .field("team", "tim")
                        .field("position", "c")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex("player", "player", "8")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "特斯拉")
                        .field("age", 20)
                        .field("salary", 500)
                        .field("team", "tim")
                        .field("position", "sf")
                        .endObject()
                )
        );
        BulkResponse bulkResponse = bulkRequest.get();
    }

    /**
     * es中的聚合查询操作
     * 统计每个球队中球员的数量
     */
    @Test
    public void testAggreate() {
        // 实现聚合操作实现
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的
        searchRequestBuilder.addAggregation(AggregationBuilders.terms("player_count").field("team"));
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
            }
        }
    }

    /**
     * 统计球队中每个球队每个位置的人员
     */
    @Test
    public void testTeamPosition() {
        // 实现聚合操作实现
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的
        TermsAggregationBuilder team = AggregationBuilders.terms("player_count").field("team");
        TermsAggregationBuilder position = AggregationBuilders.terms("posititon_count").field("position");
        //  增加子聚合条件查询操作
        team.subAggregation(position);
        searchRequestBuilder.addAggregation(team);
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
                //  求解每个队伍中，每个位置有多少人的
                Aggregations aggregations1 = bucket.getAggregations().get("posititon_count");
                for (Aggregation aggregation1 : aggregations1) {
                    StringTerms terms1 = (StringTerms) aggregation1;
                    //  获取到一个个的bucket的
                    List<StringTerms.Bucket> buckets1 = terms1.getBuckets();
                    for (StringTerms.Bucket bucket1 : buckets1) {
                        System.out.println("===位置==" + bucket1.getKey());
                        System.out.println("球队中每个位置有多少人" + bucket1.getDocCount());
                    }
                }
            }
        }
    }


    /**
     * 求解分组的最大值，最小值，以及平均值
     * 计算每个球队中年龄最大的值
     * 1,球队进行分组
     * 2.统计年龄最大的
     */
    @Test
    public void maxGroupValue() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的
        TermsAggregationBuilder field = AggregationBuilders.terms("player_count").field("team");
        MaxAggregationBuilder field1 = AggregationBuilders.max("max_year").field("age");
        field.subAggregation(field1);
        searchRequestBuilder.addAggregation(field);
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
                //  求解每个队伍中，每个位置有多少人的
                Aggregations aggregations1 = bucket.getAggregations();
                for (Aggregation aggregation1 : aggregations1) {
                    InternalMax internalMax = (InternalMax) aggregation1;
                    System.out.println("=====" + internalMax.getName() + "=====");
                    System.out.println("=====" + internalMax.getValue() + "=====");
                }
            }
        }
    }

    /**
     * 求解每一个球队中最小的年龄
     */
    @Test
    public void minAge() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的
        TermsAggregationBuilder field = AggregationBuilders.terms("player_count").field("team");
        MinAggregationBuilder field1 = AggregationBuilders.min("min_year").field("age");
        field.subAggregation(field1);
        searchRequestBuilder.addAggregation(field);
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
                //  求解每个队伍中，每个位置有多少人的
                Aggregations aggregations1 = bucket.getAggregations();
                for (Aggregation aggregation1 : aggregations1) {
                    InternalMin internalMax = (InternalMin) aggregation1;
                    System.out.println("=====" + internalMax.getName() + "=====");
                    System.out.println("=====" + internalMax.getValue() + "=====");
                }
            }
        }
    }

    /**
     * 求解每一个球队中年龄的平均值
     */
    @Test
    public void testAvgAge() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的
        TermsAggregationBuilder field = AggregationBuilders.terms("player_count").field("team");
        AvgAggregationBuilder field1 = AggregationBuilders.avg("avg_year").field("age");
        field.subAggregation(field1);
        searchRequestBuilder.addAggregation(field);
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
                //  求解每个队伍中，每个位置有多少人的
                Aggregations aggregations1 = bucket.getAggregations();
                for (Aggregation aggregation1 : aggregations1) {
                    InternalAvg internalMax = (InternalAvg) aggregation1;
                    System.out.println("=====" + internalMax.getName() + "=====");
                    System.out.println("=====" + internalMax.getValue() + "=====");
                }
            }
        }
    }

    /**
     * 计算每个球队球员的平均年龄，同时又要计算总年薪
     */
    @Test
    public void testTeamAvgYearAndTotalSalary() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的
        TermsAggregationBuilder field = AggregationBuilders.terms("player_count").field("team");
        AvgAggregationBuilder field1 = AggregationBuilders.avg("avg_year").field("age");
        SumAggregationBuilder field2 = AggregationBuilders.sum("total_salary").field("salary");
        field.subAggregation(field1);
        field.subAggregation(field2);
        searchRequestBuilder.addAggregation(field);
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
                //  求解每个队伍中，每个位置有多少人的
                InternalAvg aggregations1 = bucket.getAggregations().get("avg_year");
                System.out.println("=====" + aggregations1.getName() + "=====");
                System.out.println("=====" + aggregations1.getValue() + "=====");
                InternalSum aggregations2 = bucket.getAggregations().get("total_salary");
                System.out.println("===总的年薪==" + aggregations2.getName() + "=====");
                System.out.println("===总的年薪==" + aggregations2.getValue() + "=====");
            }
        }
    }

    /**
     * 聚合排序
     * 需求：计算每个球队的总的年薪，然后按照年薪进行倒序排序
     * */
    @Test
    public   void totalSalarySort(){
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("player").setTypes("player");
        //  设置根据term字段进行统计，player_count对应的是给结果取的别名的操作的.指定排序的字段。
        TermsAggregationBuilder field = AggregationBuilders.terms("player_count").field("team").order(BucketOrder.count(true));
        SumAggregationBuilder field2 = AggregationBuilders.sum("total_salary").field("salary");
        field.subAggregation(field2);
        searchRequestBuilder.addAggregation(field);
        //  触发执行，达到执行的结果
        SearchResponse searchResponse = searchRequestBuilder.get();
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms terms = (StringTerms) aggregation;
            //  获取到一个个的bucket的
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println("======" + bucket.getKey());
                System.out.println("球队中一共有多少球员" + bucket.getDocCount());
                //  求解每个队伍中，每个位置有多少人的
                InternalSum aggregations2 = bucket.getAggregations().get("total_salary");
                System.out.println("===总的年薪==" + aggregations2.getName() + "=====");
                System.out.println("===总的年薪==" + aggregations2.getValue() + "=====");
            }
        }
    }

    /**
     * elk的sql是收费功能的，需要破解的。或者是自己安装插件的。
     * java.sql.SQLInvalidAuthorizationSpecException: current license is non-compliant for [jdbc]
     * */
    @Test
    public void esJdbc() throws SQLException {
        EsDataSource dataSource = new EsDataSource();
        String address = "jdbc:es://http://node01:9200" ;
        dataSource.setUrl(address);
        Properties connectionProperties = new Properties();
        dataSource.setProperties(connectionProperties);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from library");
        while(resultSet.next()){
            String string = resultSet.getString(0);
            String string1 = resultSet.getString(1);
            int anInt = resultSet.getInt(2);
            String string2 = resultSet.getString(4);
            System.out.println(string + "\t" +  string1 + "\t" +  anInt + "\t" + string2);
        }
        connection.close();
    }
}
