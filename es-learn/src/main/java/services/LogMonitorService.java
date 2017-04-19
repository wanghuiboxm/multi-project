package services;

import bo.Kpi;
import bo.RecFlagBean;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.junit.Test;
import utils.ESUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by wanghb on 2017/4/18.
 */
public class LogMonitorService {
    public static final String INDEX_ALIAS = "hy_zhibo_flow"; //使用的别名
    public static final String INDEX_TYPE = "flow_moniter";
    public static final String INDEX_PREFIX = "hy_zhibo_flow-";

    //创建推荐打点索引
    public boolean createRecFlagIndex(String index) {
        boolean indexStat = ESUtil.createIndexIfAbsent(index);
        boolean mapStat = ESUtil.putMap2Index(index, INDEX_TYPE, getMapSource());
        System.out.println("indexStat="+indexStat+", mapStat="+mapStat);
        return indexStat && mapStat;
    }

    private String getMapSource() {
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("flow_moniter")
                    .startObject()
                    .field("properties")
                    .startObject()
                    .field("ac_ts").startObject().field("type", "date").endObject()
                    .field("ts").startObject().field("type", "date").endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            System.out.println(contentBuilder.string());

            return contentBuilder.string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addDoc(RecFlagBean bean) {
        String index = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        IndexResponse indexResponse = ESUtil.getClient().prepareIndex().setIndex(INDEX_PREFIX+index).setType(INDEX_TYPE)
                .setSource(JSONObject.toJSONString(bean)).get();
        System.out.println(indexResponse.status());
        return indexResponse.status().getStatus()==1;
    }

    public void addTestData() {
        for(int i=0; i<1000; ++i) {
            RecFlagBean bean = new RecFlagBean();
            Random random = new Random();
            bean.setAppid(10007);
            long uuid = random.nextInt(10000000);
            long zuid = random.nextInt(10000000);
            long time = System.currentTimeMillis() - random.nextInt(1000000);
            bean.setZuid(zuid+"");
            bean.setRoom_id(zuid+"_"+time);
            bean.setStyle(random.nextInt(20)+"");
            bean.setType(random.nextInt(5)+1);
            bean.setTs(time);
            bean.setAc_ts(time);
            bean.setUuid(uuid);
            bean.setChannel_id(random.nextInt(10));
            bean.setSection_id(random.nextInt(100));
            bean.setPosition(random.nextInt(50));
            if(bean.getType() == 3) { //送礼物
                bean.setGift_id(random.nextInt(100));
                bean.setGift_worth(random.nextInt(10000));
            }
            if(bean.getType() == 4) {//离开房间
                bean.setBeg_time(time-random.nextInt(10000000));
                bean.setEnd_time(time);
                bean.setView_time(bean.getEnd_time() - bean.getBeg_time());
            }
            addDoc(bean);
        }
    }

    public void getKpiByZuid(long zuid) {
        SearchRequestBuilder srb = ESUtil.getClient().prepareSearch(INDEX_ALIAS).setTypes(INDEX_TYPE).setSize(0);
        TermsAggregationBuilder typeAggBuilder = AggregationBuilders.terms("typeAgg").field("type").minDocCount(1);
        HistogramAggregationBuilder dataAggBuilder = AggregationBuilders.histogram("dateAgg").field("ts").interval(90000);
        AvgAggregationBuilder priceAggBuilder = AggregationBuilders.avg("priceAvg").field("gift_worth");
        SumAggregationBuilder viewTimeBuilder = AggregationBuilders.sum("viewSum").field("view_time");
        dataAggBuilder.subAggregation(priceAggBuilder).subAggregation(viewTimeBuilder);
//        typeAggBuilder.subAggregation(dataAggBuilder);
        dataAggBuilder.subAggregation(typeAggBuilder);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must().add(QueryBuilders.termQuery("zuid", zuid));
        SearchResponse searchResponse = srb.setQuery(boolQueryBuilder).addAggregation(dataAggBuilder).get();
        System.out.println(searchResponse);

    }

    public Map<Long, Kpi> getPvKpi(QueryBuilder query, int timeInterval) {
        SearchRequestBuilder srb = ESUtil.getClient().prepareSearch(INDEX_ALIAS).setTypes(INDEX_TYPE).setSize(0);
        HistogramAggregationBuilder dataAggBuilder = AggregationBuilders.histogram("dateAgg")
                .field("ts").interval(900000).offset(1492444800000L).minDocCount(1);
        TermsAggregationBuilder typeAggBuilder = AggregationBuilders.terms("typeAgg").field("type").minDocCount(1);
        SumAggregationBuilder viewTimeBuilder = AggregationBuilders.sum("viewSum").field("view_time");
        SumAggregationBuilder ticketAggBuilder = AggregationBuilders.sum("ticketSum").field("gift_worth");
        typeAggBuilder.subAggregation(viewTimeBuilder).subAggregation(ticketAggBuilder);
        dataAggBuilder.subAggregation(typeAggBuilder);
        SearchResponse searchResponse = srb.addAggregation(dataAggBuilder).get();
        System.out.println(searchResponse);
        Histogram dateAgg = searchResponse.getAggregations().get("dateAgg");
        Map<Long, Kpi> kpiMap = new HashMap<>();
        if(dateAgg != null) {
            for (Histogram.Bucket bucket : dateAgg.getBuckets()) {
                Double k = (Double) bucket.getKey();
                Date beginTime = new Date(k.longValue());
                Kpi pvKpi = new Kpi();
                pvKpi.setBeginTime(beginTime);
                Terms typeAgg = bucket.getAggregations().get("typeAgg");
                if (typeAgg != null) {
                    for (Terms.Bucket type : typeAgg.getBuckets()) {
                        if ((long) type.getKey() == 1) {
                            pvKpi.setExposure(type.getDocCount());
                        } else if ((long) type.getKey() == 2) {
                            pvKpi.setClick(type.getDocCount());
                        } else if ((long) type.getKey() == 3) { //送星票
                            Sum ticket = type.getAggregations().get("ticketSum");
                            pvKpi.setView(Double.valueOf(ticket.getValue()).longValue());
                        } else if ((long) type.getKey() == 4) { //观看
                            Sum view = type.getAggregations().get("viewSum");
                            pvKpi.setTicket(Double.valueOf(view.getValue()).longValue());
                        } else if ((long) type.getKey() == 5) {
                            pvKpi.setFocus(type.getDocCount());
                        }
                    }
                    kpiMap.put(beginTime.getTime(), pvKpi);
                }
            }
        }
        System.out.println(kpiMap.toString());
        return kpiMap;
    }

    public void getUvKpi(QueryBuilder query, int timeInterval) {
        SearchRequestBuilder srb = ESUtil.getClient().prepareSearch(INDEX_ALIAS).setTypes(INDEX_TYPE).setSize(0);
        HistogramAggregationBuilder dataAggBuilder = AggregationBuilders.histogram("dateAgg")
                .field("ts").interval(900000).offset(1492444800000L).minDocCount(1);
        TermsAggregationBuilder typeAggBuilder = AggregationBuilders.terms("typeAgg").field("type")
                .minDocCount(1).order(Terms.Order.term(true));
//        CardinalityAggregationBuilder uuidUvCount = AggregationBuilders.cardinality("uv_count").field("uuid");
//        typeAggBuilder.subAggregation(uuidUvCount);
        dataAggBuilder.subAggregation(typeAggBuilder);
        SearchResponse searchResponse = srb.addAggregation(dataAggBuilder).get();
        System.out.println(searchResponse);
        Histogram dateAgg = searchResponse.getAggregations().get("dateAgg");
        Map<Long, Kpi> kpiMap = new HashMap<>();
        if(dateAgg != null) {
            for(Histogram.Bucket bucket : dateAgg.getBuckets()) {
                Double k = (Double) bucket.getKey();
                Date beginTime = new Date(k.longValue());
                Kpi uvKpi = new Kpi();
                uvKpi.setBeginTime(beginTime);
                Terms typeAgg = bucket.getAggregations().get("typeAgg");
                if(typeAgg != null) {
                    for(Terms.Bucket type: typeAgg.getBuckets()) {
                        System.out.println("key="+type.getKey().getClass()+", count="+type.getDocCount());
                        if((long)type.getKey() == 1) {
                            uvKpi.setExposure(type.getDocCount());
                        } else if((long)type.getKey() == 2){
                            uvKpi.setClick(type.getDocCount());
                        } else if((long)type.getKey()==3) {
                            uvKpi.setView(type.getDocCount());
                        } else if((long)type.getKey() == 4) {
                            uvKpi.setTicket(type.getDocCount());
                        } else if((long)type.getKey() == 5) {
                            uvKpi.setFocus(type.getDocCount());
                        }
                    }
                }
                kpiMap.put(beginTime.getTime(), uvKpi);
            }
        }
        System.out.println(kpiMap.toString());
    }

    @Test
    public void test() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(createRecFlagIndex(INDEX_PREFIX+sdf.format(new Date())));
//        addTestData();
//        getKpiByZuid(6012440);
//        getPvKpi(null, 0);
        getUvKpi(null, 111);
    }

}
