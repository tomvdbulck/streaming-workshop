package be.ordina.workshop.streaming.opendatatraffic.cloud;


import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloudProducer {

    private final Channels outputChannels;

    @Autowired
    public CloudProducer(final Channels channels) {
        this.outputChannels = channels;
    }


    public void sendMessage(TrafficEvent trafficEvent) {
        outputChannels.trafficEvents().send(MessageBuilder.withPayload(trafficEvent).build());
    }

}
