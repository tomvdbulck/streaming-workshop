package be.ordina.workshop.streaming.opendatatraffic.cloud;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutputChannels {

    @Output
    MessageChannel trafficEvents();
}
