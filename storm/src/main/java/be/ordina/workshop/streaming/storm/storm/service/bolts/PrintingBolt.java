package be.ordina.workshop.streaming.storm.storm.service.bolts;

import be.ordina.workshop.streaming.storm.storm.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Slf4j
public class PrintingBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple input) {
        System.out.println("==============> PRINTING input = [" + input + "]");
        log.info("PRINTING input = [" + input + "]");

        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("topic", "partition", "offset", "key", "value"));
    }
}
