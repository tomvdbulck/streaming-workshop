package be.ordina.workshop.streaming.opendatatraffic.service;

import be.ordina.workshop.streaming.opendatatraffic.converter.ConvertXmlToDomain;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.hamcrest.number.IsCloseTo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.NotNull;

import java.util.Date;

public class ReadInSensorDataTest {


    private ReadInSensorDataService readInSensorDataService;

    @Before
    public void setup() {
        readInSensorDataService = new ReadInSensorDataService(new ConvertXmlToDomain());
    }

    @Test
    public void testReadInSensorDataFromURL() throws Exception {

        Assert.assertThat(readInSensorDataService.readInData().size(), Is.is(22640));

        Assert.assertNotNull(readInSensorDataService.getLastReadInDate().getTime());
    }
}
