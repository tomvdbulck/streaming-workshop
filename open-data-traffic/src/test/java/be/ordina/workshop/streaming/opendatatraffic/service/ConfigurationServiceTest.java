package be.ordina.workshop.streaming.opendatatraffic.service;

import be.ordina.workshop.streaming.opendatatraffic.converter.ConvertXmlToDomain;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationServiceTest {


    private ConfigurationService configurationService;


    @Before
    public void setup() throws  Exception{
        configurationService = new ConfigurationService(new ConvertXmlToDomain());
    }

    @Test
    public void testLoadInConfigData() throws Exception{

        configurationService.loadInSensorData();

        Assert.assertThat(configurationService.getSensorDataHashMap().size(), Is.is(4254));
        Assert.assertThat(configurationService.getSensorDataHashMap().get("3640").getName(), Is.is("Parking Kruibeke"));

        Assert.assertThat(configurationService.getSensorIdsToProcess().size(), Is.is(23));

    }

    @Test
    public void testLoadInSensorIdsToProcess() throws Exception{

        configurationService.setupSensors();

        Assert.assertThat(configurationService.getSensorIdsToProcess().size(), Is.is(23));

    }
}
