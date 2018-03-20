package be.ordina.workshop.streaming.opendatatraffic.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StreamHandler {

    private List<String> messages;
    private final InputChannels inputChannels;

    private MessageHandler messageHandler;

    @Autowired
    public StreamHandler(InputChannels inputChannels) {
        this.messages = new ArrayList<>();
        this.inputChannels = inputChannels;

        subScribeOnChannel();
    }

    private void subScribeOnChannel() {


        this.messages = new ArrayList<>();

        messageHandler = (message -> {
            log.info("retrieved message " + message.getPayload());
            messages.add((String) message.getPayload());
        });


        inputChannels.trafficEvents().subscribe(messageHandler);
    }

    public List<String> getMessages() {

        List<String> messagesToReturn = new ArrayList<>();

        if (messageHandler != null) {
            inputChannels.trafficEvents().unsubscribe(messageHandler);

            messages.forEach(m -> messagesToReturn.add(m));
        }

        this.subScribeOnChannel();


        return messagesToReturn;
    }

}
