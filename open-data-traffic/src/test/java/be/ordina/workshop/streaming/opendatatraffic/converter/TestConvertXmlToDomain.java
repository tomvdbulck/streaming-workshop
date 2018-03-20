package be.ordina.workshop.streaming.opendatatraffic.converter;

import be.ordina.workshop.streaming.opendatatraffic.domain.SensorData;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import generated.config.TMivconfig;
import generated.traffic.Miv;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestConvertXmlToDomain {

    private ConvertXmlToDomain converter;

    @Before
    public void setup() {
        this.converter = new ConvertXmlToDomain();
    }

    @Test
    public void testConvert() throws Exception {
        JAXBContext jc = JAXBContext.newInstance("generated.traffic");
        Unmarshaller um = jc.createUnmarshaller();

        Miv miv = (Miv) um.unmarshal(getClass().getResourceAsStream("/xml/verkeersdata.xml" ));

        List<TrafficEvent> trafficEvents = converter.trafficMeasurements(miv.getMeetpunt());

        assertEquals(4254, trafficEvents.size());

    }


    @Test
    public void testConvertConfiguration() throws Exception{

        JAXBContext jc = JAXBContext.newInstance("generated.config");
        Unmarshaller um = jc.createUnmarshaller();

        JAXBElement<TMivconfig> config = (JAXBElement<TMivconfig>) um.unmarshal(getClass().getResourceAsStream("/xml/configuratie.xml" ));


        List<SensorData> sensorDataList = converter.sensorConfig(config.getValue());

        assertEquals(4254, sensorDataList.size());

    }

    @Test
    @Ignore
    public void testConvertWithEmptyFile() {
        //TODO
    }

    @Test
    @Ignore
    public void testConvertWithNoTrafficEvents() {
        //TODO
    }
}
