package be.ordina.workshop.streaming.opendatatraffic.cloud;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {

    @Input
    SubscribableChannel binding1();


    @Input("trafficEventsKStream")
    KStream<?, ?> trafficEventsKStream();

    @Output("ouputKStreams")
    KStream<?, ?> ouputKStreams();
}
