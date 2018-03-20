package be.ordina.workshop.streaming.opendatatraffic.converter;

import be.ordina.workshop.streaming.opendatatraffic.domain.SensorData;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import generated.config.TMeetpunt;
import generated.config.TMivconfig;
import generated.traffic.Miv;

import java.util.ArrayList;
import java.util.List;

public class ConvertXmlToDomain {


    List<TrafficEvent> trafficMeasurements (List<Miv.Meetpunt> meetpunten) {

        List<TrafficEvent> events = new ArrayList<>();

        for (Miv.Meetpunt meetpunt : meetpunten) {
            TrafficEvent trafficEvent = new TrafficEvent();

            events.add(trafficEvent);
        }


        return events;

    }





    List<SensorData> sensorConfig (TMivconfig tMivconfig) {

        List<SensorData> sensorDataList = new ArrayList<>();

        for (TMeetpunt tMeetpunt : tMivconfig.getMeetpunt()) {

            sensorDataList.add(new SensorData());
        }

        return sensorDataList;

    }
}
