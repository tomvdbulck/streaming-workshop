package be.ordina.workshop.streaming.spring.springkafka.domain;

import org.springframework.kafka.support.serializer.JsonSerde;

public class TrafficEventSerde extends JsonSerde<TrafficEvent> {
}