package be.ordina.workshop.streaming.opendatatraffic.cloud;

import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {

    @Input
    SubscribableChannel trafficEvents();

    @Output
    MessageChannel trafficEventsOutput();

    @Output
    MessageChannel sensorDataOutput();

    @Output
    SubscribableChannel ouputKStreams();

    @Output
    SubscribableChannel dummyTopic();

}
