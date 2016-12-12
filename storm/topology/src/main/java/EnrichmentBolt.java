import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;


public class EnrichmentBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;

    public void execute(Tuple tuple) {
        System.out.println("message " + tuple.getValues());
    }

    public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
    }

    public void declareOutputFields(OutputFieldsDeclarer arg0) {
    }
}
