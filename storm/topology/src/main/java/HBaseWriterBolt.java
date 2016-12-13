import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;


public class HBaseWriterBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;

    public void execute(Tuple tuple) {

        Configuration conf = HBaseConfiguration.create();

        conf.set("hbase.zookeeper.quorum", "zookeeper");
        conf.set("hbase.zookeeper.property.clientPort","2181");

        try {
            HTable hTable = new HTable(conf, "netflow");
            Put p = new Put(toBytes(UUID.randomUUID().toString()));
            p.add(Bytes.toBytes("metadata"), Bytes.toBytes("received"), Bytes.toBytes("received"));
            hTable.put(p);
            hTable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
    }

    public void declareOutputFields(OutputFieldsDeclarer arg0) {
    }
}
