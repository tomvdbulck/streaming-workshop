package be.ordina.workshop.streaming.storm.storm.service.bolts;

import be.ordina.workshop.streaming.storm.storm.domain.TrafficEvent;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;

@Slf4j
public class TrafficEventBolt extends BaseRichBolt {
    private OutputCollector collector;


    private final List<String> sensorIds;

    public TrafficEventBolt(List<String> sensorIdsToProcess) {
        this.sensorIds = sensorIdsToProcess;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple input) {
        log.info("input = [" + input + "]");

        input.getValues();

        TrafficEvent trafficEvent = new Gson().fromJson((String)input.getValueByField("value"), TrafficEvent.class);
        //TrafficEvent trafficEvent = (TrafficEvent) input.getValueByField("value");

        if (sensorIds.contains(trafficEvent.getSensorId())) {
            collector.emit(input, new Values(trafficEvent.getSensorId(), trafficEvent.getVehicleSpeedCalculated(), trafficEvent.getTrafficIntensity()));
        } else {
            collector.ack(input);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sensorId", "speed", "trafficIntensity"));
    }
}
