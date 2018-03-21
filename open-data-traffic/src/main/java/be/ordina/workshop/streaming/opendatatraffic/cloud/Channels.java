package be.ordina.workshop.streaming.opendatatraffic.cloud;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {

    @Input
    SubscribableChannel trafficEvents();

    @Input
    SubscribableChannel trafficEventsKStream();

    @Output
    SubscribableChannel ouputKStreams();



}
