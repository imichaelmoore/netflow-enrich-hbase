import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;


public class FlowCounter implements IRichBolt {

    int counter;
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.counter = 0;
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        counter++;
    }

    @Override
    public void cleanup() {
        Configuration conf = HBaseConfiguration.create();

        conf.set("hbase.zookeeper.quorum", "zookeeper");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "hbase:60000");


        HTable hTable = null;
        try {
            hTable = new HTable(conf, "counters");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Get g = new Get(toBytes("all_flows"));
        int CurrentCounter = 0;
        try {
            Result r = hTable.get(g);
            CurrentCounter = new Integer(String.valueOf(r.getValue(toBytes("key"), toBytes("total_flows"))));
        } catch (IOException e) {}

        Put p = new Put(toBytes("all_flows"));
        p.add(toBytes("key"), toBytes("total_flows"), toBytes(CurrentCounter));

        try {
            hTable.put(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
