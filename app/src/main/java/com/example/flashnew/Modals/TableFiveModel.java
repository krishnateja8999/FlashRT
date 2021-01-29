package com.example.flashnew.Modals;

public class TableFiveModel {
    String coletaID;
    String streetName;
    String aptNo;
    String city;
    String state;
    String pincode;
    String dna;
    String clientID;
    String contractID;
    double latitude;
    double longitude;

    public TableFiveModel(String coletaID, String streetName, String aptNo, String city, String state, String pincode, String dna, String clientID, String contractID, double latitude, double longitude) {
        this.coletaID = coletaID;
        this.streetName = streetName;
        this.aptNo = aptNo;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.dna = dna;
        this.clientID = clientID;
        this.contractID = contractID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getColetaID() {
        return coletaID;
    }

    public void setColetaID(String coletaID) {
        this.coletaID = coletaID;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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
