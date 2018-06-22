package be.ordina.workshop.streaming.spring.springkafka.be.ordina.workshop.streaming.spring.springkafka.service;

import be.ordina.workshop.streaming.spring.springkafka.be.ordina.workshop.streaming.spring.springkafka.domain.TrafficEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
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
public class KafkaStreamsConfiguration {

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
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(TrafficEvent.class).getClass());
        //props.put(JsonDeserializer.DEFAULT_KEY_TYPE, String.class);
        //props.put(JsonDeserializer.DEFAULT_VALUE_TYPE, TrafficEvent.class);
        return new StreamsConfig(props);
    }

    @Bean
    public KStream<Windowed<String>, Integer> kStream(StreamsBuilder streamsBuilder) {

        KStream<String, TrafficEvent> stream = streamsBuilder.stream("trafficEvents", Consumed.with(Serdes.String(), new JsonSerde<>(TrafficEvent.class)));

        KStream<Windowed<String>, Integer> countedSensorStream = stream.map(new SensorKeyValueMapper())
                .groupByKey()
                .reduce((Integer value1, Integer value2) -> value1 + value2, TimeWindows.of(1000), "windowStore")
                .toStream();

        countedSensorStream.to("streams-output");

        countedSensorStream.print();

        return countedSensorStream;

    }

    public class SensorKeyValueMapper implements KeyValueMapper<String, TrafficEvent, KeyValue<String, Integer>> {

        @Override
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
