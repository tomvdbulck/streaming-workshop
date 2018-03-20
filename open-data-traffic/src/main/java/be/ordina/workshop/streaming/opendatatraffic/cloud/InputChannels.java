package be.ordina.workshop.streaming.opendatatraffic.cloud;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InputChannels {

    @Input
    SubscribableChannel trafficEvents();
}

