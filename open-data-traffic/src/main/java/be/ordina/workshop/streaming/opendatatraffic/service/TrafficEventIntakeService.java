package be.ordina.workshop.streaming.opendatatraffic.service;


import be.ordina.workshop.streaming.opendatatraffic.cloud.CloudProducer;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class TrafficEventIntakeService implements ApplicationRunner {

    private final ReadInSensorDataService readInSensorDataService;

    private final CloudProducer cloudProducer;

    @Autowired
    public TrafficEventIntakeService(final ReadInSensorDataService readInSensorDataService, final CloudProducer cloudProducer) {

        this.readInSensorDataService = readInSensorDataService;
        this.cloudProducer = cloudProducer;

    }


    public void putEventsInKafka() throws Exception {
        log.info("put Events in Kafka");
        readInSensorDataService.readInData().forEach(m -> {cloudProducer.sendMessage(m);});

        log.info("completed putting Events in Kafka");
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        for (int i = 0;  i<3 ; i ++) {
            Thread.sleep(1000l);

            putEventsInKafka();
        }

    }
}
