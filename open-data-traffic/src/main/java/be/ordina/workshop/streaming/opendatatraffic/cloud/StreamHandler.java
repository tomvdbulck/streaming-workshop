package be.ordina.workshop.streaming.opendatatraffic.cloud;

import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class StreamHandler {

    private List<TrafficEvent> messages;

    private final HashMap<String, TrafficEvent> lowestWithTraffic;
    private final HashMap<String, TrafficEvent> highestWithTraffic;

    private final HashMap<String, Integer> vehicleCount;



    private final Channels inputChannels;

    private MessageHandler messageHandler;

    @Autowired
    public StreamHandler(Channels channels) {
        this.messages = new ArrayList<>();
        this.inputChannels = channels;

        subScribeOnChannel();
        subScribeOnKStreamsChannel();

        this.lowestWithTraffic = new HashMap<>();
        this.highestWithTraffic = new HashMap<>();
        this.vehicleCount = new HashMap<>();
    }

    private void subScribeOnChannel() {
        this.messages = new ArrayList<>();

        messageHandler = (message -> {
            log.info("retrieved message with header " + message.getHeaders().toString());
            log.info("retrieved message " + message.getPayload().toString());

            TrafficEvent event = (TrafficEvent) message.getPayload();

            log.info(" the sensor id is " + event.getSensorId());

            if (event.getTrafficIntensity() > 0) {
                //log.info("We now have {} vehicles on the road {} for lane:{}", event.getTrafficIntensity(), event.getSensorData().getName(), event.getSensorData().getTrafficLane());

                log.info("We now have {} vehicles on the road {}", event.getTrafficIntensity(), event.getSensorId());


                int vehicleCountForEvent = event.getTrafficIntensity();

                if (vehicleCount.get(event.getSensorId()) != null) {
                    vehicleCountForEvent += vehicleCount.get(event.getSensorId());
                }

                //log.info("We now had total: {} vehicles on road: {} for lane: {}", vehicleCountForEvent, event.getSensorData().getName(), event.getSensorData().getTrafficLane());
                log.info("We now had total: {} vehicles", vehicleCountForEvent);


                vehicleCount.put(event.getSensorId(), vehicleCountForEvent);
            }


            if (event.getVehicleSpeedCalculated() > 0) {
                if (lowestWithTraffic.get(event.getSensorId()) == null || lowestWithTraffic.get(event.getSensorId()).getVehicleSpeedCalculated() > event.getVehicleSpeedCalculated()) {
                    lowestWithTraffic.put(event.getSensorId(), event);

                    //log.info("Updated lowestWithTraffic for sensor {} with an event with speed {} for vehicle {} ", event.getSensorData().getName() + " " + event.getSensorData().getTrafficLane()
                    //        , event.getVehicleSpeedCalculated(), event.getVehicleClass().name());
                }

                if (highestWithTraffic.get(event.getSensorId()) == null || highestWithTraffic.get(event.getSensorId()).getVehicleSpeedCalculated() < event.getVehicleSpeedCalculated()) {
                    highestWithTraffic.put(event.getSensorId(), event);

                    //log.info("Updated highestTraffic for sensor {} with an event with speed {} for vehicle {} ", event.getSensorData().getName() + " " + event.getSensorData().getTrafficLane()
                    //        , event.getVehicleSpeedCalculated(), event.getVehicleClass().name());
                }

                messages.add(event);
            }

        });

        inputChannels.trafficEvents().subscribe(messageHandler);
    }


    private void subScribeOnKStreamsChannel() {
        this.messages = new ArrayList<>();

        messageHandler = (message -> {
            log.info("retrieved message " + message.getPayload().toString());
        });


        inputChannels.ouputKStreams().subscribe(messageHandler);

    }

    public List<TrafficEvent> getMessages() {

        List<TrafficEvent> messagesToReturn = new ArrayList<>();

        if (messageHandler != null) {
            inputChannels.trafficEvents().unsubscribe(messageHandler);

            messages.forEach(m -> messagesToReturn.add(m));
        }

        this.subScribeOnChannel();


        return messagesToReturn;
    }


    public HashMap<String, TrafficEvent> getLowestWithTraffic() {
        return lowestWithTraffic;
    }

    public HashMap<String, TrafficEvent> getHighestWithTraffic() {
        return highestWithTraffic;
    }

}
