package be.ordina.workshop.streaming.opendatatraffic.service;


import be.ordina.workshop.streaming.opendatatraffic.converter.ConvertXmlToDomain;
import be.ordina.workshop.streaming.opendatatraffic.domain.SensorData;
import generated.config.TMivconfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ConfigurationService {


    private HashMap<String, SensorData> sensorDataHashMap;
    private List<String> sensorIdsToProcess;


    private final ConvertXmlToDomain converter;

    @Autowired
    public ConfigurationService(final ConvertXmlToDomain convertXmlToDomain) throws  Exception {

        sensorDataHashMap = new HashMap<>();

        this.converter = convertXmlToDomain;
    }

    @PostConstruct
    public void loadInSensorData() throws  Exception{

        log.info("Will load in the sensor data ");

        JAXBContext jc = JAXBContext.newInstance("generated.config");
        Unmarshaller um = jc.createUnmarshaller();

        JAXBElement<TMivconfig> config = (JAXBElement<TMivconfig>) um.unmarshal(getClass().getResourceAsStream("/configuratie.xml" ));


        List<SensorData> sensorDataList = converter.sensorConfig(config.getValue());

        for (SensorData sensorData : sensorDataList) {
            sensorDataHashMap.put(sensorData.getUniekeId(), sensorData);
        }

        log.info("Read in {} records", sensorDataHashMap.size());
    }


    @PostConstruct
    public void setupSensors() throws Exception {
        sensorIdsToProcess =  Files.lines(Paths.get(getClass().getResource("/sensors_mechelen_n_z.txt").toURI() ))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

    }


    public HashMap<String, SensorData> getSensorDataHashMap() {
        return sensorDataHashMap;
    }

    public List<String> getSensorIdsToProcess() {return this.sensorIdsToProcess;}

}
