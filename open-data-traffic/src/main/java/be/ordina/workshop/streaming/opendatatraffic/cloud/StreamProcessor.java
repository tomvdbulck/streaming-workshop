package be.ordina.workshop.streaming.opendatatraffic.cloud;

import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@EnableAutoConfiguration
public class StreamProcessor {


    //@StreamListener("input")
    //@SendTo("output")
    public KStream<?, String> process(KStream<?, TrafficEvent> input) {


        log.info(">>>>>>>>>>>>> processing");

        return input
                .flatMapValues(value -> Arrays.asList(value.getSensorId()))
                .map((key, sensorId) -> new KeyValue<>(sensorId, sensorId))
                .groupByKey(Serdes.String(), Serdes.String())
                .count(TimeWindows.of(50000), "store-name")
                .toStream()
                .map((w, c) -> new KeyValue<>(null, "Count for " + w.key() + ": " + c));
    }


}
