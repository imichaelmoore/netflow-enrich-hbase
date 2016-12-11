/**
 * Created by moorema1 on 12/10/16.
 */
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.UUID;

public class Topology {

    public static void main(String[] args) throws Exception{
        Config config = new Config();
        config.setDebug(true);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        config.setNumWorkers(20);
        config.setMaxSpoutPending(5000);

        String zkConnString = "zookeeper:2181";
        String topic = "netflow";
        BrokerHosts hosts = new ZkHosts(zkConnString);

        SpoutConfig kafkaSpoutConfig = new SpoutConfig (hosts, topic, "/" + topic,
                UUID.randomUUID().toString());
        kafkaSpoutConfig.bufferSizeBytes = 1024 * 1024 * 4;
        kafkaSpoutConfig.fetchSizeBytes = 1024 * 1024 * 4;
        // kafkaSpoutConfig.forceFromStart = true;
        kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout(kafkaSpoutConfig));
        builder.setBolt("word-counter", new CountBolt()).shuffleGrouping("kafka-spout");

        StormSubmitter.submitTopology("KafkaStormSample", config, builder.createTopology());
    }

    
}