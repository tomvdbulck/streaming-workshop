package be.ordina.workshop.streaming.opendatatraffic.domain;

import lombok.Data;

import java.util.Date;

@Data
public class TrafficEvent {

    private VehicleClass vehicleClass;

    private Integer trafficIntensity;


    /*
    Sum (vi) / n = arithmetic average speed of the vehicles in this vehicle class
        (with vi = individual speed of a vehicle in this vehicle class)
        Value domaing 0 to 254 km/h.
        Value range 0..200 km/h
        Resolution 1.
        Special values:
        - 251: Initial value
        - 254: Calculation not possible
        - 252: no vehicles were counted in this vehicle class.
     */
    private Integer vehicleSpeedCalculated;


    /*
    n / Sum (1/vi) = harmonic average speed of the vehicles in this vehicle class
         (with vi = individual speed of a vehicle in this vehicle class)
        Special values:
        - 251: Initial value
        - 254: Calculation not possible
        - 252: no vehicles were counted in this vehicle class.
     */
    private Integer vehicleSpeedHarmonical;

    /*
    MeetpuntId
     */
    private String sensorId;
    /*
    Meetpunt beschrijvende Id
     */
    private String sensorDescriptiveId;

    private Integer lveNumber;
    private Date timeRegistration;
    private Date lastUpdated;

    /*
    actueel_publicatie: 1 = data is minder dan 3 minuten oud.
     */
    private Boolean recentData;

    private Boolean availableMeetpunt;

    private Integer sensorDefect;
    private Integer sensorValid;

}
