import java.util.Map;
import java.util.HashMap;

import org.apache.storm.tuple.Tuple;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.task.TopologyContext;

public class CountBolt implements IRichBolt{
    Map<String, Integer> counters;
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.counters = new HashMap<String, Integer>();
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String str = input.getString(0);
        System.out.println(str);

        collector.ack(input);
    }

    @Override
    public void cleanup() {
        for(Map.Entry<String, Integer> entry:counters.entrySet()){
            System.out.println(entry.getKey()+" : " + entry.getValue());
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