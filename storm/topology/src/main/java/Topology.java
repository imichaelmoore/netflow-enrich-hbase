import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import org.apache.storm.LocalCluster;

import java.util.UUID;

/**
 * Created by moorema1 on 12/10/16.
 */


public class Topology {

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
        builder.setBolt("print", new EnrichmentBolt()).shuffleGrouping("kafka-spout");

//        LocalCluster cluster = new LocalCluster();

        StormSubmitter.submitTopology("KafkaStormSample", config, builder.createTopology());
    }

    
}