package be.ordina.workshop.streaming.opendatatraffic.converter;

import be.ordina.workshop.streaming.opendatatraffic.domain.SensorData;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import generated.config.TMeetpunt;
import generated.config.TMivconfig;
import generated.traffic.Miv;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertXmlToDomain {


    public List<TrafficEvent> trafficMeasurements (List<Miv.Meetpunt> meetpunten) {

        List<TrafficEvent> events = new ArrayList<>();

        for (Miv.Meetpunt meetpunt : meetpunten) {
            TrafficEvent trafficEvent = new TrafficEvent();

            events.add(trafficEvent);
        }


        return events;

    }





    public List<SensorData> sensorConfig (TMivconfig tMivconfig) {

        List<SensorData> sensorDataList = new ArrayList<>();

        for (TMeetpunt tMeetpunt : tMivconfig.getMeetpunt()) {

            SensorData sensorData = new SensorData();
            sensorData.setUniekeId(tMeetpunt.getUniekeId());
            sensorData.setIdent8(tMeetpunt.getIdent8());
            sensorData.setName(tMeetpunt.getVolledigeNaam());
            sensorData.setSensorDescriptiveId(tMeetpunt.getBeschrijvendeId());
            sensorData.setTrafficLane(tMeetpunt.getRijstrook());

            sensorDataList.add(sensorData);
        }

        return sensorDataList;

    }
}
