package be.ordina.workshop.streaming.storm.storm.service.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;

import java.util.HashMap;

public class CountPerSensorIdBolt extends BaseWindowedBolt {

    private OutputCollector collector;

    private final HashMap<String, Integer> countPerSensors = new HashMap<>();

    @Override
    public void execute(TupleWindow tupleWindow) {

        for (Tuple input : tupleWindow.get()) {
            Integer count = countPerSensors.get((String)input.getValueByField("sensorId"));
            if (count == null) {
                count = 0;
            }
            count = count+ (Integer) input.getValueByField("trafficIntensity");
            countPerSensors.put(input.getString(0), count);

            collector.emit(new Values(input.getString(0), count));

            collector.ack(input);
        }
    }
}
