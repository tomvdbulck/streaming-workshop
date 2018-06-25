package be.ordina.workshop.streaming.storm.storm.service.bolts;

import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TrafficCountBolt extends BaseRichBolt {
    private OutputCollector collector;


    private final HashMap<String, Integer> countPerSensors = new HashMap<>();


    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple input) {
        log.info("input = [" + input + "]");

        Integer count = countPerSensors.get((String)input.getValueByField("sensorId"));
        if (count == null) {
            count = 0;
        }
        count = count+ (Integer) input.getValueByField("trafficIntensity");
        countPerSensors.put(input.getString(0), count);

        collector.emit(new Values(input.getString(0), count));

        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sensorId", "count"));
    }
}
