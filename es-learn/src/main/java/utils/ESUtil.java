package utils;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wanghb on 2017/4/18.
 */
public class ESUtil {

    private static TransportClient client;

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

    //索引不存在则创建
    public static boolean createIndexIfAbsent(String name) {
//        TransportClient client = ESClient.getClient();
        if(isExistIndex(name))
            return false;
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(name).get();
        return createIndexResponse.isAcknowledged();
    }

    //判断一个index是否存在
    public static boolean isExistIndex(String name) {
//        TransportClient client = ESClient.getClient();
        IndicesExistsRequest request = new IndicesExistsRequest(name);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        return response.isExists();
    }

    //删除一个索引
    public static boolean deleteIndex(String name) {
//        TransportClient client = ESClient.getClient();
        DeleteIndexRequest request = new DeleteIndexRequest(name);
        DeleteIndexResponse response = client.admin().indices().delete(request).actionGet();
        return response.isAcknowledged();
    }

    //添加一个mapping到index
    public static boolean putMap2Index(String indices, String type, String source) {
//        TransportClient client = ESClient.getClient();
        PutMappingRequest request = Requests.putMappingRequest(indices).source(source).type(type);
        PutMappingResponse response = client.admin().indices().putMapping(request).actionGet();
        return response.isAcknowledged();
    }

    //更新mapping
    public static boolean updateMap(String indices, String type, XContentBuilder builder) {
//        TransportClient client = ESClient.getClient();
        PutMappingRequest request = Requests.putMappingRequest(indices).type(type).source(builder);
        PutMappingResponse response = client.admin().indices().putMapping(request).actionGet();
        return response.isAcknowledged();
    }

    public static boolean updateAlias(String index, String alias) {
//        TransportClient client = ESClient.getClient();
        IndicesAliasesResponse response = client.admin().indices().prepareAliases().addAlias(index, alias).get();
        return response.isAcknowledged();
    }

    public static boolean removealias(String index, String alias) {
//        TransportClient client = ESClient.getClient();
        IndicesAliasesResponse response = client.admin().indices().prepareAliases().removeAlias(index, alias).get();
        return response.isAcknowledged();
    }

    public static void main(String[] args) {
//        System.out.println(isExistIndex("book"));
//        System.out.println(createIndexIfAbsent("hy_zhibo_flow-2017-04-19"));
//        System.out.println(updateAlias("hy_zhibo_flow-2017-04-19", "hy_zhibo_flow"));
    }
}
