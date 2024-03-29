import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;


public class HBaseWriterBolt extends BaseBasicBolt {

    private static final long serialVersionUID = 1L;

    public void execute(Tuple tuple, BasicOutputCollector collector) {

        Configuration conf = HBaseConfiguration.create();

        conf.set("hbase.zookeeper.quorum", "zookeeper");
        conf.set("hbase.zookeeper.property.clientPort","2181");

        try {
            HTable hTable = new HTable(conf, "netflow");
            Put p = new Put(toBytes(UUID.randomUUID().toString()));
            p.add(Bytes.toBytes("metadata"), Bytes.toBytes("received"), Bytes.toBytes(tuple.getString(0)));


            Gson GSON = new Gson();
            Map<String, String> parsed = GSON.fromJson(tuple.getString(0), Map.class);

            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("src_addr"), Bytes.toBytes(String.valueOf(parsed.get("src_addr"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("src_port"), Bytes.toBytes(String.valueOf(parsed.get("src_port"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("protocol"), Bytes.toBytes(String.valueOf(parsed.get("protocol"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("octets"), Bytes.toBytes(String.valueOf(parsed.get("octets"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("dst_addr"), Bytes.toBytes(String.valueOf(parsed.get("dst_addr"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("dst_port"), Bytes.toBytes(String.valueOf(parsed.get("dst_port"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("src_host"), Bytes.toBytes(String.valueOf(parsed.get("src_host"))));
            p.add(Bytes.toBytes("flowdata"), Bytes.toBytes("dst_host"), Bytes.toBytes(String.valueOf(parsed.get("dst_host"))));

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
