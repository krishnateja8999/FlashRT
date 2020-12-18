package com.example.flashnew.Modals;

public class TableFiveModel {
    String coletaID;
    String streetName;
    String aptNo;
    String city;
    String state;
    String pincode;

    public TableFiveModel(String coletaID, String streetName, String aptNo, String city, String state, String pincode) {
        this.coletaID = coletaID;
        this.streetName = streetName;
        this.aptNo = aptNo;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
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
}
