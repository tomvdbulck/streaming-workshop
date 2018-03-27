package be.ordina.workshop.streaming.opendatatraffic.cloud;


import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CloudProducer {

    private final Channels outputChannels;

    @Autowired
    public CloudProducer(final Channels channels) {
        this.outputChannels = channels;
    }

    public void sendMessages(List<TrafficEvent> trafficEventList) {

        trafficEventList.forEach(m -> {


            //log.info("send message: " + m + " to trafficEvents");
            outputChannels.trafficEvents().send(MessageBuilder.withPayload(m).build());

            outputChannels.trafficEventsKStream().send(MessageBuilder.withPayload(m).build());
        }
        );

    }

    public void sendMessage(TrafficEvent trafficEvent) {
        outputChannels.trafficEvents().send(MessageBuilder.withPayload(trafficEvent).build());
    }

}
