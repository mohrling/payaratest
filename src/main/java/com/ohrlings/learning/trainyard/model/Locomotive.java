package com.ohrlings.learning.trainyard.model;

public class Locomotive extends Vehicle{
    private int dynamicWeight;

    public Locomotive(int vehicleNr, String littera, int clogWeight, int vehicleWeight, double length, boolean isClearedForTraffic, int dynamicWeight) {
        super(vehicleNr, littera, clogWeight, vehicleWeight, length, isClearedForTraffic);
        this.dynamicWeight = dynamicWeight;
    }
}
