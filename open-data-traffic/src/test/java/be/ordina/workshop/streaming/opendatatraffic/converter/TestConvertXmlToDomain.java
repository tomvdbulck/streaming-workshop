package be.ordina.workshop.streaming.opendatatraffic.converter;

import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import generated.Miv;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
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
        JAXBContext jc = JAXBContext.newInstance("generated");
        Unmarshaller um = jc.createUnmarshaller();

        Miv miv = (Miv) um.unmarshal(getClass().getResourceAsStream("/xml/verkeersdata.xml" ));

        List<TrafficEvent> trafficEvents = converter.trafficMeasurements(miv.getMeetpunt());

        assertEquals(4254, trafficEvents.size());

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
