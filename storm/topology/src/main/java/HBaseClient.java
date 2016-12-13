import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Iterator;
import java.util.UUID;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * Created by moorema1 on 12/10/16.
 */


public class HBaseClient {

    public static void main(String[] args) throws Exception {
        System.err.println("Starting");

        Configuration conf = HBaseConfiguration.create();

        conf.set("hbase.zookeeper.quorum", "zookeeper");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "hbase:60000");


        HTable hTable = new HTable(conf, "netflow");

        Scan s = new Scan();
        ResultScanner scanner = hTable.getScanner(s);
        for (Result r = scanner.next(); r != null; r = scanner.next()) {
            byte[] rowId = r.getRow();
            System.out.println("rowid=" + Bytes.toString(rowId));
            Iterator<KeyValue> iter = r.list().iterator();
//            while (iter.hasNext()) {
                KeyValue kv = iter.next();
                byte[] family = kv.getFamily();
                byte[] qualifier = kv.getQualifier();
                byte[] value = kv.getValue();
                System.out.println(Bytes.toString(family) + ":" + Bytes.toString(qualifier) + "=" + Bytes.toString(value));
//            }
            System.out.println(""); //Blank line to separate records
        }


    }

}
