package be.ordina.workshop.streaming.opendatatraffic.service;

import be.ordina.workshop.streaming.opendatatraffic.cloud.Channels;
import be.ordina.workshop.streaming.opendatatraffic.cloud.CloudProducer;
import be.ordina.workshop.streaming.opendatatraffic.converter.ConvertXmlToDomain;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.MessageChannel;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {


    private ConfigurationService configurationService;

    @Mock
    Channels channels;

    @Mock
    MessageChannel messageChannelSensors;


    @Before
    public void setup() throws  Exception{
        configurationService = new ConfigurationService(new ConvertXmlToDomain(), new CloudProducer(channels));
    }

    @Test
    public void testLoadInConfigData() throws Exception{
        configurationService.loadInSensorData();

        Assert.assertThat(configurationService.getSensorDataHashMap().size(), Is.is(4254));
        Assert.assertThat(configurationService.getSensorDataHashMap().get("3640").getName(), Is.is("Parking Kruibeke"));
    }

    @Test
    public void testLoadInSensorIdsToProcess() throws Exception{

        configurationService.setupSensors();

        Assert.assertThat(configurationService.getSensorIdsToProcess().size(), Is.is(23));

    }
}
