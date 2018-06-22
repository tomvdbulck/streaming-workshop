package be.ordina.workshop.streaming.opendatatraffic.service;


import be.ordina.workshop.streaming.opendatatraffic.cloud.CloudProducer;
import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrafficEventIntakeService implements ApplicationRunner {

    private final ReadInSensorDataService readInSensorDataService;
    private final ConfigurationService configurationService;

    private final CloudProducer cloudProducer;


    @Autowired
    public TrafficEventIntakeService(final ReadInSensorDataService readInSensorDataService, final CloudProducer cloudProducer, final ConfigurationService configurationService){

        this.readInSensorDataService = readInSensorDataService;
        this.cloudProducer = cloudProducer;

        this.configurationService = configurationService;

    }




    public void putEventsInKafka() throws Exception {
        log.info("put Events in Kafka");
        readInSensorDataService.readInData().forEach(m -> {
            if (configurationService.getSensorIdsToProcess().contains(m.getSensorId())) {
                m.setSensorData(configurationService.getSensorDataHashMap().get(m.getSensorId()));
                cloudProducer.sendMessage(m);
            }
        });

        log.info("completed putting Events in Kafka");
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        for (int i = 0;  i<500 ; i ++) {
            putEventsInKafka();

            Thread.sleep(6000l);
        }



    }
}
