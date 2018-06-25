package be.ordina.workshop.streaming.storm.storm.service;

import be.ordina.workshop.streaming.storm.storm.service.bolts.TrafficEventBolt;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;

@Component
public class StormApplicationRunnerWithKafka implements ApplicationRunner {

    private static final String KAFKA_LOCAL_BROKER = "localhost:9092";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Config tpConf = getConfig();

        //Consumer. Sets up a topology that reads the given Kafka spouts and logs the received messages
        //StormSubmitter.submitTopology("storm-kafka-client-spout-test", tpConf, getTopologyKafkaSpout(getKafkaSpoutConfig(KAFKA_LOCAL_BROKER)));



        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("storm-kafka-client-spout-test", tpConf, getTopologyKafkaSpout(getKafkaSpoutConfig(KAFKA_LOCAL_BROKER)));
    }

    protected StormTopology getTopologyKafkaSpout(KafkaSpoutConfig<String, String> spoutConfig) {
        final TopologyBuilder tp = new TopologyBuilder();
        tp.setSpout("kafka_spout", new KafkaSpout<>(spoutConfig), 1);
        tp.setBolt("trafficEvent_Bolt", new TrafficEventBolt());
        //tp.setBolt("kafka_bolt", new KafkaSpoutTestBolt())
        //        .shuffleGrouping("kafka_spout", TOPIC_0_1_STREAM)
        //        .shuffleGrouping("kafka_spout", TOPIC_2_STREAM);
        //tp.setBolt("kafka_bolt_1", new KafkaSpoutTestBolt()).shuffleGrouping("kafka_spout", TOPIC_2_STREAM);
        return tp.createTopology();
    }

    private Config getConfig() {
        Config config = new Config();
        config.setDebug(true);
        return config;
    }

    protected KafkaSpoutConfig<String, String> getKafkaSpoutConfig(String bootstrapServers) {
        ByTopicRecordTranslator<String, String> trans = new ByTopicRecordTranslator<>(
                (r) -> new Values(r.topic(), r.partition(), r.offset(), r.key(), r.value()),
                new Fields("topic", "partition", "offset", "key", "value"));
        trans.forTopic("trafficEventsOutput",
                (r) -> new Values(r.topic(), r.partition(), r.offset(), r.key(), r.value()),
                new Fields("topic", "partition", "offset", "key", "value"));
        return KafkaSpoutConfig.builder(bootstrapServers, new String[]{"trafficEventsOutput", "sensorDataOutput"})
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "kafkaSpoutTestGroup")
                .setRetry(getRetryService())
                .setRecordTranslator(trans)
                .setOffsetCommitPeriodMs(10_000)
                .setFirstPollOffsetStrategy(EARLIEST)
                .setMaxUncommittedOffsets(250)
                .build();
    }

    protected KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(KafkaSpoutRetryExponentialBackoff.TimeInterval.microSeconds(500),
                KafkaSpoutRetryExponentialBackoff.TimeInterval.milliSeconds(2), Integer.MAX_VALUE, KafkaSpoutRetryExponentialBackoff.TimeInterval.seconds(10));
    }

}
