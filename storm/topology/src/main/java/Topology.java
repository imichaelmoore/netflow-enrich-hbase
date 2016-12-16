import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;

import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.storm.tuple.Fields;


import java.util.UUID;

/**
 * Created by moorema1 on 12/10/16.
 */


public class Topology {

    private static final Logger LOG = LoggerFactory.getLogger(Topology.class);


    public static class PrinterBolt extends BaseBasicBolt {
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            System.out.println(tuple);
            LOG.debug("Got tuple {}", tuple);
            collector.emit(tuple.getValues());
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer ofd) {
            ofd.declare(new Fields("value"));
        }

    }

    public static void main(String[] args) throws Exception{
        Config config = new Config();
        config.setDebug(true);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        config.setNumWorkers(20);
        config.setMaxSpoutPending(5000);

        String zkConnString = "zookeeper:2181";
        String topic = "netflow";

        BrokerHosts zk = new ZkHosts(zkConnString);
        SpoutConfig spoutConf = new SpoutConfig(zk, topic, "/", UUID.randomUUID().toString());
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConf);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", kafkaSpout);
        builder.setBolt("dns-resolution", new DNSResolve()).shuffleGrouping("kafka-spout");
        builder.setBolt("printer-bolt", new PrinterBolt()).shuffleGrouping("dns-resolution");
        builder.setBolt("hbase-writer", new HBaseWriterBolt()).shuffleGrouping("dns-resolution");
        builder.setBolt("flow-counter", new FlowCounter()).shuffleGrouping("dns-resolution");

        StormSubmitter.submitTopology("KafkaStormSample", config, builder.createTopology());
    }

    
}