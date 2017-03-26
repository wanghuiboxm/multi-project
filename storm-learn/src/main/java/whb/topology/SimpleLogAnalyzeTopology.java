package whb.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by wanghb on 2017/3/27.
 */
public class SimpleLogAnalyzeTopology {
    //读取日志信息
    public static class LogReaderSpout extends BaseRichSpout {
        private SpoutOutputCollector collector;
        private Random random = new Random();
        private int count = 100;
        private String[] users = new String[]{"userA", "userB", "userC", "userD", "userE"};
        private String[] urls = new String[]{"url1", "url2", "url3", "url4", "url5"};

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.collector = collector;
        }

        @Override
        public void nextTuple() {
            try {
                Thread.sleep(500); //每隔500ms产生一条消息
                if (count > 0) {
                    --count;
                    collector.emit(new Values(
                            System.currentTimeMillis(),
                            users[random.nextInt(5)],
                            urls[random.nextInt(5)]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("time", "user", "url"));
        }
    }

    //统计每个用户访问的次数
    public static class LogStatBolt extends BaseRichBolt {
        private OutputCollector collector;
        private Map<String, Integer> pvMap = new HashMap<>(); //存放用户访问次数

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
        }

        @Override
        public void execute(Tuple input) {
            String user = input.getStringByField("user");

            if(pvMap.containsKey(user)) {
                pvMap.put(user, pvMap.get(user)+1);
            } else {
                pvMap.put(user, 1);
            }

            //提交流到下个spout
            collector.emit(new Values(user, pvMap.get(user)));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("user", "pv"));
        }
    }

    //处理统计后的结果
    public static class LogWriterSpout extends BaseRichBolt {

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        }

        @Override
        public void execute(Tuple input) {
            System.out.println(String.format("%s:%d", input.getStringByField("user"), input.getIntegerByField("pv")));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

        }
    }

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        //设置流id=log-reader,并行度为1
        builder.setSpout("log-reader", new LogReaderSpout(), 1);

        //接收id=log-reader的流，并行度为2，按照user字段合并流
        builder.setBolt("log-stat", new LogStatBolt(), 2).
                fieldsGrouping("log-reader", new Fields("user"));

        builder.setBolt("log-writer", new LogWriterSpout(), 1).
                localOrShuffleGrouping("log-stat");

        Config config = new Config();

        if(args!=null && args.length>0) {
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            //本地模式运行
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("test", config, builder.createTopology());
            Utils.sleep(60000);
            localCluster.killTopology("test");
            localCluster.shutdown();
        }
    }
}
