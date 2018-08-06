package be.ordina.workshop.streaming.storm.storm.service;

import be.ordina.workshop.streaming.storm.storm.domain.TrafficEvent;
import be.ordina.workshop.streaming.storm.storm.service.bolts.PrintingBolt;
import be.ordina.workshop.streaming.storm.storm.service.bolts.TrafficCountBolt;
import be.ordina.workshop.streaming.storm.storm.service.bolts.TrafficEventBolt;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;

@Component
public class StormRunnerWithKafka{

    private static final String KAFKA_LOCAL_BROKER = "localhost:9092";

    private List<String> sensorIdsToProcess;

    private final HashMap<String, Integer> totalVehicleCountPerSensor;
    private final HashMap<String, Integer> totalVehicleCountPerSensorPerType;
    private final HashMap<String, Integer> highestSpeedPerSensor;
    private final HashMap<String, Integer> lowestSpeedPerSensor;

    public StormRunnerWithKafka(){
        this.totalVehicleCountPerSensor = new HashMap<>();
        this.totalVehicleCountPerSensorPerType = new HashMap<>();
        this.highestSpeedPerSensor = new HashMap<>();
        this.lowestSpeedPerSensor = new HashMap<>();
    }

    public void printOutStats() {
        System.out.println("==========    TOTAL PER SENSOR ===========");
        for (String key : totalVehicleCountPerSensor.keySet()) {
            System.out.println(key + ": " + totalVehicleCountPerSensor.get(key));
        }

        System.out.println("==========    TOTAL PER SENSOR PER VEHICLE TYPE ===========");
        for (String key : totalVehicleCountPerSensorPerType.keySet()) {
            System.out.println(key + ": " + totalVehicleCountPerSensorPerType.get(key));
        }

        System.out.println("==========    FASTEST PER SENSOR ===========");
        for (String key : highestSpeedPerSensor.keySet()) {
            System.out.println(key + ": " + highestSpeedPerSensor.get(key));
        }

        System.out.println("==========    SLOWEST PER SENSOR ===========");
        for (String key : lowestSpeedPerSensor.keySet()) {
            System.out.println(key + ": " + lowestSpeedPerSensor.get(key));
        }
    }

    public void updateStats(TrafficEvent trafficEvent) {

        //System.out.println("Processing data for trafficEvent" + trafficEvent);

        Integer totalCountSensor = (totalVehicleCountPerSensor.get(trafficEvent.getSensorId()) != null ? totalVehicleCountPerSensor.get(trafficEvent.getSensorId()) : 0);
        totalVehicleCountPerSensor.put(trafficEvent.getSensorId(), totalCountSensor + trafficEvent.getTrafficIntensity());

        Integer totalCountPerSensorPerType = (totalVehicleCountPerSensorPerType.get(trafficEvent.getSensorId() + " " +trafficEvent.getVehicleClass().name()) != null
                ? totalVehicleCountPerSensorPerType.get(trafficEvent.getSensorId() + " " +trafficEvent.getVehicleClass().name()) : 0);
        totalVehicleCountPerSensorPerType.put(trafficEvent.getSensorId() + " " +trafficEvent.getVehicleClass().name(), totalCountPerSensorPerType + trafficEvent.getTrafficIntensity());

        Integer fastestPerSensor = (highestSpeedPerSensor.get(trafficEvent.getSensorId()) != null ? highestSpeedPerSensor.get(trafficEvent.getSensorId()) : 0);
        if (fastestPerSensor < trafficEvent.getVehicleSpeedCalculated()) {
            highestSpeedPerSensor.put(trafficEvent.getSensorId(), trafficEvent.getVehicleSpeedCalculated());
        }

        Integer lowestSpeedPserSensor = (lowestSpeedPerSensor.get(trafficEvent.getSensorId()) != null ? lowestSpeedPerSensor.get(trafficEvent.getSensorId()) : 0);
        if (lowestSpeedPserSensor > trafficEvent.getVehicleSpeedCalculated()) {
            lowestSpeedPerSensor.put(trafficEvent.getSensorId(), trafficEvent.getVehicleSpeedCalculated());
        }

    }

    @PostConstruct
    public void run() throws Exception {

        this.sensorIdsToProcess =  Files.lines(Paths.get(getClass().getResource("/sensors_mechelen_n_z.txt").toURI() ))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        Config tpConf = getConfig();

        //Consumer. Sets up a topology that reads the given Kafka spouts and logs the received messages
        //StormSubmitter.submitTopology("storm-kafka-client-spout-test", tpConf, getTopologyKafkaSpout(getKafkaSpoutConfig(KAFKA_LOCAL_BROKER)));

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("storm-kafka-client-spout-test", tpConf, getTopologyKafkaSpout(getKafkaSpoutConfig(KAFKA_LOCAL_BROKER)));
    }

    private StormTopology getTopologyKafkaSpout(KafkaSpoutConfig<String, String> spoutConfig) {
        final TopologyBuilder tp = new TopologyBuilder();
        tp.setSpout("kafka_spout", new KafkaSpout<>(spoutConfig), 1).setDebug(false);
        tp.setBolt("trafficEvent_Bolt", new TrafficEventBolt(sensorIdsToProcess)).setDebug(false)
                .globalGrouping("kafka_spout");
        tp.setBolt("updateTrafficEventStats_bolt", new TrafficCountBolt()).setDebug(true)
                .fieldsGrouping("trafficEvent_Bolt", new Fields("sensorId"));
        return tp.createTopology();
    }

    private Config getConfig() {
        Config config = new Config();
        //config.setDebug(true);
        return config;
    }

    protected KafkaSpoutConfig<String, String> getKafkaSpoutConfig(String bootstrapServers) {
        ByTopicRecordTranslator<String, String> trans = new ByTopicRecordTranslator<>(
                (r) -> new Values(r.topic(), r.partition(), r.offset(), r.key(), r.value()),
                new Fields("topic", "partition", "offset", "key", "value"));
        trans.forTopic("trafficEventsOutput",
                (r) -> new Values(r.topic(), r.partition(), r.offset(), r.key(), r.value()),
                new Fields("topic", "partition", "offset", "key", "value"));
        return KafkaSpoutConfig.builder(bootstrapServers, new String[]{"trafficEventsOutput"})
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "kafkaSpoutTestGroup")
                .setRetry(getRetryService())
                .setRecordTranslator(trans)
                .setOffsetCommitPeriodMs(10_000)
                .setFirstPollOffsetStrategy(EARLIEST)
                .setMaxUncommittedOffsets(1050)
                .build();
    }

    protected KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(KafkaSpoutRetryExponentialBackoff.TimeInterval.microSeconds(500),
                KafkaSpoutRetryExponentialBackoff.TimeInterval.milliSeconds(2), Integer.MAX_VALUE, KafkaSpoutRetryExponentialBackoff.TimeInterval.seconds(10));
    }

}
