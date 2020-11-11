package com.ohrlings.learning.trainyard.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class Carriage extends Vehicle {
    private boolean cleaned;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date comfortChecked;
    private int firstClassSeats;
    private int secondClassSeats;
    private boolean discBrakes;

    public Carriage(int vehicleNr, String littera, double length, boolean cleaned, Date comfortChecked, boolean isClearedForTraffic, int vehicleWeight, int clogWeight, int firstClassSeats, int secondClassSeats, boolean discBrakes) {
        super(vehicleNr, littera, clogWeight, vehicleWeight, length, isClearedForTraffic);
        this.cleaned = cleaned;
        this.comfortChecked = comfortChecked;
        this.firstClassSeats = firstClassSeats;
        this.secondClassSeats = secondClassSeats;
        this.discBrakes = discBrakes;
    }

    public Carriage() {
    }

    public boolean isCleaned() {
        return cleaned;
    }

    public Date getComfortChecked() {
        return comfortChecked;
    }

    public void setCleaned(boolean cleaned) {
        this.cleaned = cleaned;
    }

    public void setComfortChecked(Date comfortChecked) {
        this.comfortChecked = comfortChecked;
    }

    public int getVehicleWeight() {
        return vehicleWeight;
    }

    public int getClogWeight() {
        return clogWeight;
    }

    public int getFirstClassSeats() {
        return firstClassSeats;
    }

    public int getSecondClassSeats() {
        return secondClassSeats;
    }

    public boolean isDiscBrakes() {
        return discBrakes;
    }
}
