package be.ordina.workshop.streaming.spring.springkafka.service;

import be.ordina.workshop.streaming.spring.springkafka.domain.SensorData;
import be.ordina.workshop.streaming.spring.springkafka.domain.SensorDataSerde;
import be.ordina.workshop.streaming.spring.springkafka.domain.TrafficEvent;
import be.ordina.workshop.streaming.spring.springkafka.domain.TrafficEventSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
@Slf4j
public class KafkaStreamsConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        /**
         * Required properties are: APPLICATION_ID_CONFIG and BOOTSTRAP_SERVERS_CONFIG
         *
         * SERDE: Serializer/Deserializer
         * https://kafka.apache.org/10/documentation/streams/developer-guide/datatypes.html
         * so that data can be materialized when needed
         *
         */
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "spring-kafka-traffic");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<TrafficEvent>(TrafficEvent.class).getClass());


        //props.put(JsonDeserializer.DEFAULT_KEY_TYPE, String.class);
        //props.put(JsonDeserializer.DEFAULT_VALUE_TYPE, TrafficEvent.class);
        return new StreamsConfig(props);
    }

    @Bean
    public KStream<String, TrafficEvent> kStreamStart (StreamsBuilder streamsBuilder) {

        System.out.println(">>>>>>>>>>>>>> enter kstreamStart ");


        KStream<String, SensorData> sensorDescriptionsStream = streamsBuilder.stream("sensorDataOutput", Consumed.with(Serdes.String(), new SensorDataSerde()));

        KStream<String, SensorData> sensorDescriptionsWithKey = sensorDescriptionsStream.selectKey((key, value) -> value.getUniekeId());
        sensorDescriptionsWithKey.to("dummy-topic");

        KTable<String, SensorData> sensorDataKTable = streamsBuilder.table("dummy-topic", Consumed.with(Serdes.String(), new SensorDataSerde()));
        //sensorDataKTable.print();

        KStream<String, TrafficEvent> stream = streamsBuilder.stream("trafficEventsOutput", Consumed.with(Serdes.String(), new TrafficEventSerde()));

        stream.selectKey((key,value) -> value.getSensorId())
                //.join(sensorDataKTable, ((trafficEvent, sensorData) -> {
                //    trafficEvent.setSensorData(sensorData);
                //    return trafficEvent;
                //}))
                .selectKey((key, value) -> key + value.getTimeRegistration().toString())
                .print();
                //.to("enriched-traffic-events");

        //stream.print();


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> return ");

        return stream;

    }


    @Bean
    public KStream<String, SensorData> kStreamSensorIdentification(StreamsBuilder streamsBuilder){
        //KStream<String, SensorData> sensorDescriptionsStream = streamsBuilder.stream("sensorDataOutput", Consumed.with(Serdes.String(), new SensorDataSerde()));

        //KStream<String, SensorData> sensorDescriptionsWithKey = sensorDescriptionsStream.selectKey((key, value) -> value.getUniekeId());
        //sensorDescriptionsWithKey.to("sensorDataOutputWithKey");
        //sensorDescriptionsWithKey.print();

        //return sensorDescriptionsStream;

        return null;
    }



/**
 //@Bean
    public KStream<Windowed<String>, Integer> kStream(StreamsBuilder streamsBuilder) {

        System.out.println(">>>>>>>>>>>>>> enter kstream");

        JsonSerde<TrafficEvent> trafficEventJsonSerde = new JsonSerde<>(TrafficEvent.class);

        KStream<String, String> stream = streamsBuilder.stream("trafficEventsOutput", Consumed.with(Serdes.String(), Serdes.String()));

        KStream<Windowed<String>, Integer> countedSensorStream = stream.map(new SensorKeyValueMapper())
                .groupByKey()
                .reduce((Integer value1, Integer value2) -> value1 + value2, TimeWindows.of(1000), "windowStore")
                .toStream();

        KStream<String, TrafficEvent> stream = streamsBuilder.stream("trafficEventsOutput", Consumed.with(Serdes.String(), new TrafficEventSerde()));

        KStream<Windowed<String>, Integer> countedSensorStream = stream.map(new SensorKeyValueMapper())
                .groupByKey()
                .reduce((Integer value1, Integer value2) -> value1 + value2, TimeWindows.of(1000), "windowStore")
                .toStream();

        countedSensorStream.to("streams-output", Produced.valueSerde(Serdes.Integer()));

        System.out.println("print ");
        countedSensorStream.print();


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> return ");

        return countedSensorStream;

    }
 **/

    public class SensorKeyValueMapper implements KeyValueMapper<String, String, KeyValue<String, Integer>> {


        @Override
        public KeyValue<String, Integer> apply(String key, String value) {
            return new KeyValue<>(value, 1);
        }

        //@Override
        public KeyValue<String, Integer> apply(String key, TrafficEvent value) {
            return new KeyValue<>(value.getSensorId(), 1);
        }

    }

    public class CountReducer implements Reducer<Integer> {

        @Override
        public Integer apply(Integer val1, Integer val2) {
            return val1 + val2;
        }
    }
}
