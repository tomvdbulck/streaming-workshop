package be.ordina.workshop.streaming.opendatatraffic.cloud;


import be.ordina.workshop.streaming.opendatatraffic.domain.SensorData;
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


        log.info("Send message to the trafficEventOutput channel");
        outputChannels.trafficEventsOutput().send(MessageBuilder.withPayload(trafficEvent).build());
    }

    public void sendSensorData(SensorData sensorData) {
        outputChannels.sensorDataOutput().send(MessageBuilder.withPayload(sensorData).build());
    }

}
