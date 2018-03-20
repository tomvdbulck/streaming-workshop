package be.ordina.workshop.streaming.opendatatraffic.domain;

public enum VehicleClass {


    /*
    Please note that the vehicle class moto, 1, does not provide reliable data.
     */
    MOTO(1),
    CAR(2),
    CAMIONET(3),
    RIGGID_LORRIES(4),
    TRUCK_OR_BUS(5),
    UNKNOWN(0);


    private final int value;

    public int getValue() {
        return value;
    }

    VehicleClass (final int value) {
        this.value = value;
    }
}
