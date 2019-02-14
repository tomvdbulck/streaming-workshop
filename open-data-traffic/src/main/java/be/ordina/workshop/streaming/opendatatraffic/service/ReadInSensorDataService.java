package be.ordina.workshop.streaming.opendatatraffic.service;

import be.ordina.workshop.streaming.opendatatraffic.converter.ConvertXmlToDomain;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import generated.traffic.Miv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ReadInSensorDataService {

    private final ConvertXmlToDomain convertXmlToDomain;

    private Date lastReadInDate = null;

    private final String url = "http://miv.opendata.belfla.be/miv/verkeersdata";

    @Autowired
    public ReadInSensorDataService(final ConvertXmlToDomain convertXmlToDomain) {
        this.convertXmlToDomain = convertXmlToDomain;
    }

    public List<TrafficEvent> readInData() throws Exception {
        log.info("Will read in data from " + url);

        JAXBContext jc = JAXBContext.newInstance("generated.traffic");
        Unmarshaller um = jc.createUnmarshaller();

        Miv miv = (Miv) um.unmarshal(new URL(url).openStream());

        log.info(" This data is from " + miv.getTijdPublicatie().toGregorianCalendar().getTime());
        List<TrafficEvent> trafficEventList = convertXmlToDomain.trafficMeasurements(miv.getMeetpunt());

        lastReadInDate = miv.getTijdPublicatie().toGregorianCalendar().getTime();

        log.info("retrieved {} events ", trafficEventList.size()) ;

        return trafficEventList;
    }

    public Date getLastReadInDate() {
        return this.lastReadInDate;
    }
}
