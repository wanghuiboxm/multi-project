package utils;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wanghb on 2017/4/11.
 */
public class ESClient {
    private static TransportClient client;
    private final static String DEFAULT_INDEX = "huyu_zhibo";
    private final static String DEFAULT_TYPE = "anchor_info";

    static {
        try {
//            Settings settings = Settings.builder().put("cluster.name", "tj-es-staging").build();
//            client = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("tj-data-es-staging01.kscn"), 9300))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("tj-data-es-staging02.kscn"), 9300))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("tj-data-es-staging03.kscn"), 9300));
            Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.17.55.239"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static TransportClient getClient() {
        return client;
    }

    /**
     * 增加一个索引
     */
    @Test
    public void createIndex() {
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate("zhibo_test").get();
        System.out.println(createIndexResponse.isAcknowledged());
    }


    @Test
    public void deleteIndex() {
        DeleteIndexResponse indexResponse = client.admin().indices().prepareDelete("zhibo_test").get();
        System.out.println(indexResponse.isAcknowledged());
    }

    /**
     * 给所以增加Mapping
     */
    @Test
    public void addMapping() {
        XContentBuilder contentBuilder = null;
        try {
            contentBuilder = XContentFactory.jsonBuilder()
                    .startObject().field("properties")
                    .startObject()
                    .field("name").startObject().field("index", "not_analyzed").field("type", "string").endObject()
                    .field("age").startObject().field("index", "not_analyzed").field("type", "integer").endObject()
                    .endObject()
                    .endObject();
            System.out.println(contentBuilder.string());

            PutMappingRequest mappingRequest = Requests.putMappingRequest("zhibo_test").source(contentBuilder).type("user_info");
            PutMappingResponse mappingResponse = client.admin().indices().putMapping(mappingRequest).actionGet();
            System.out.println(mappingResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个doc
     */
    @Test
    public void addDoc() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "zhangsan")
                        .field("age", "18")
                        .endObject();

        IndexResponse indexResponse = client.prepareIndex().setIndex("zhibo_test").setType("user_info")
                .setSource(builder.string()).get();
//                .setId() //如果没有设置id，es会自动生成一个
        System.out.println(indexResponse.status().getStatus());
    }

    /**
     * 搜索
     */
    @Test
    public void searchDoc() {

        SearchResponse response = client.prepareSearch("zhibo_test").setTypes("user_info")
                .setQuery(QueryBuilders.matchQuery("age", "18")).get();
        System.out.println(response.toString());
        SearchResponse response1 = client.prepareSearch("").addAggregation(AggregationBuilders.dateHistogram("ss").interval(111).field("")).get();
    }

    @Test
    public void searchDoc2() {

        GetResponse response = client.prepareGet().setIndex("zhibo_test").setType("user_info").setId("AVtc_k_yFF3UCGW8ecsp").execute().actionGet();
        System.out.println(response.toString());
    }

    @Test
    public void testSearch() {
        SearchRequestBuilder search = client.prepareSearch("zhibo_test").setTypes("user_info");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        search.setQuery(boolQueryBuilder).addAggregation();
    }
}
