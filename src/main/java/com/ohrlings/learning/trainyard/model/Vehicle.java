package com.ohrlings.learning.trainyard.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public abstract class Vehicle implements Serializable {

    @Id
    protected int vehicleNr;
    protected String littera;
    protected int clogWeight;
    protected int vehicleWeight;
    protected double length;
    protected boolean isClearedForTraffic;

    public Vehicle() {
    }

    public Vehicle(int vehicleNr, String littera, int clogWeight, int vehicleWeight, double length, boolean isClearedForTraffic) {
        this.vehicleNr = vehicleNr;
        this.littera = littera;
        this.clogWeight = clogWeight;
        this.vehicleWeight = vehicleWeight;
        this.length = length;
        this.isClearedForTraffic = isClearedForTraffic;
    }

    public int getClogWeight() {
        return clogWeight;
    }

    public int getVehicleWeight() {
        return vehicleWeight;
    }

    public int getVehicleNr() {
        return vehicleNr;
    }

    public String getLittera() {
        return littera;
    }

    public double getLength() {
        return length;
    }

    public boolean isClearedForTraffic() {
        return isClearedForTraffic;
    }

    public void setClearedForTraffic(boolean clearedForTraffic) {
        isClearedForTraffic = clearedForTraffic;
    }
}
