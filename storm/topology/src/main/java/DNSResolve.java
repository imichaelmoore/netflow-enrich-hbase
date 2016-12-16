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
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.UUID;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;


public class DNSResolve extends BaseBasicBolt {

    private static final long serialVersionUID = 1L;

    public void execute(Tuple tuple, BasicOutputCollector collector) {

        Gson GSON = new Gson();
        Map<String, String> parsed = GSON.fromJson(tuple.getString(0), Map.class);

        // src_addr
        InetAddress src_addr = null;
        try {
            src_addr = InetAddress.getByName(parsed.get("src_addr"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String src_host = src_addr.getHostName();
        parsed.put("src_host",src_host.toString());

        // dest_addr
        InetAddress dest_addr = null;
        try {
            dest_addr = InetAddress.getByName(parsed.get("dst_addr"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String dest_host = dest_addr.getHostName();
        parsed.put("dst_host",dest_host.toString());

        String newJson = GSON.toJson(parsed);

        collector.emit(new Values(newJson));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("json"));
    }

}
