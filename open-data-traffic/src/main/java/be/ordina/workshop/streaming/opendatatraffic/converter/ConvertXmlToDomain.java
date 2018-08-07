package be.ordina.workshop.streaming.opendatatraffic.converter;

import be.ordina.workshop.streaming.opendatatraffic.domain.SensorData;
import be.ordina.workshop.streaming.opendatatraffic.domain.TrafficEvent;
import be.ordina.workshop.streaming.opendatatraffic.domain.VehicleClass;
import generated.config.TMeetpunt;
import generated.config.TMivconfig;
import generated.traffic.Miv;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ConvertXmlToDomain {


    public List<TrafficEvent> trafficMeasurements (final List<Miv.Meetpunt> meetpunten) {

        List<TrafficEvent> events = new ArrayList<>();

        for (Miv.Meetpunt meetpunt : meetpunten) {
            for (Miv.Meetpunt.Meetdata meetdata : meetpunt.getMeetdata()) {
                TrafficEvent trafficEvent = new TrafficEvent();

                trafficEvent.setLastUpdated(meetpunt.getTijdLaatstGewijzigd().toGregorianCalendar().getTime());

                if (meetpunt.getBeschikbaar() == 1) {
                    trafficEvent.setRecentData(true);
                } else {
                    trafficEvent.setRecentData(false);
                }

                trafficEvent.setLveNumber(meetpunt.getLveNr().intValue());
                if (meetpunt.getActueelPublicatie() == 1) {
                    trafficEvent.setRecentData(true);
                } else {
                    trafficEvent.setRecentData(false);
                }

                trafficEvent.setTimeRegistration(meetpunt.getTijdWaarneming().toGregorianCalendar().getTime());
                trafficEvent.setLastUpdated(meetpunt.getTijdLaatstGewijzigd().toGregorianCalendar().getTime());

                //trafficEvent.setSensorDefect();
                trafficEvent.setSensorId(meetpunt.getUniekeId());
                trafficEvent.setSensorDescriptiveId(meetpunt.getBeschrijvendeId());
                trafficEvent.setTimeRegistration(meetpunt.getTijdWaarneming().toGregorianCalendar().getTime());

                /* Handle the meetData data*/
                trafficEvent.setVehicleClass(getVehicleClassFromMeetData(meetdata));
                trafficEvent.setTrafficIntensity(meetdata.getVerkeersintensiteit());

                trafficEvent.setVehicleSpeedCalculated(meetdata.getVoertuigsnelheidRekenkundig());
                trafficEvent.setVehicleSpeedHarmonical(meetdata.getVoertuigsnelheidHarmonisch());

                events.add(trafficEvent);
            }
        }
        return events;
    }

    private VehicleClass getVehicleClassFromMeetData(Miv.Meetpunt.Meetdata meetdata) {
        return  Arrays.stream(VehicleClass.values())
                .filter(e -> e.getValue() == meetdata.getKlasseId())
                .findFirst()
                .orElse(VehicleClass.UNKNOWN);
    }




    public List<SensorData> sensorConfig (final TMivconfig tMivconfig) {

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
