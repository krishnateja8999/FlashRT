package com.example.flashnew.Modals;

public class CollectListModalClass {
    String hawbCode;
    String address;
    String tick_mark;
    String dna;
    String clientID;
    String contractID;
    double latitude;
    double longitude;

    public CollectListModalClass(String hawbCode, String address, String tick_mark, String dna, String clientID, String contractID, double latitude, double longitude) {
        this.hawbCode = hawbCode;
        this.address = address;
        this.tick_mark = tick_mark;
        this.dna = dna;
        this.clientID = clientID;
        this.contractID = contractID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getHawbCode() {
        return hawbCode;
    }

    public void setHawbCode(String hawbCode) {
        this.hawbCode = hawbCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTick_mark() {
        return tick_mark;
    }

    public void setTick_mark(String tick_mark) {
        this.tick_mark = tick_mark;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
