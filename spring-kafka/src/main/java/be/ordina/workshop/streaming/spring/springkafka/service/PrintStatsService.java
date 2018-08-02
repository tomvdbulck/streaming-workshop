package be.ordina.workshop.streaming.spring.springkafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrintStatsService implements ApplicationRunner {

    private final KafkaStreamsConfiguration kafkaStreamsConfiguration;

    @Autowired
    public PrintStatsService (KafkaStreamsConfiguration kafkaStreamsConfiguration) {
        this.kafkaStreamsConfiguration = kafkaStreamsConfiguration;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0;  i<500 ; i ++) {
            kafkaStreamsConfiguration.printOutStats();

            Thread.sleep(60000l);
        }
    }
}
