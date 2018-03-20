package be.ordina.workshop.streaming.opendatatraffic.service;

import be.ordina.workshop.streaming.opendatatraffic.converter.ConvertXmlToDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadInSensorData {

    private final ConvertXmlToDomain convertXmlToDomain;

    @Autowired
    public ReadInSensorData(final ConvertXmlToDomain convertXmlToDomain) {
        this.convertXmlToDomain = convertXmlToDomain;
    }
}
