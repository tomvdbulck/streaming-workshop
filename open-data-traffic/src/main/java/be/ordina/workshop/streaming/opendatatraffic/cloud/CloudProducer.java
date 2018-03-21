package be.ordina.workshop.streaming.opendatatraffic.cloud;


import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CloudProducer {

    private final OutputChannels outputChannels;

    @Autowired
    public CloudProducer(final OutputChannels outputChannels) {
        this.outputChannels = outputChannels;
    }

    public void sendMessages(List<TrafficEvent> trafficEventList) {

        trafficEventList.forEach(m -> {
            //log.info("send message: " + m + " to trafficEvents");
            outputChannels.trafficEvents().send(MessageBuilder.withPayload(m).build());
        }
        );

    }

    public void sendMessage(TrafficEvent trafficEvent) {
        outputChannels.trafficEvents().send(MessageBuilder.withPayload(trafficEvent).build());

    }

}
