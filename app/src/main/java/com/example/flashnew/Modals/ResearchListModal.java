package com.example.flashnew.Modals;

public class ResearchListModal {
    String hawbCode;
    String clientNumber;
    String RecipientName;
    int dna;
    String aptNo;
    String publicPlace;
    String streetName;
    String city;
    String state;
    int pinCode;
    int clientID;
    int contractID;
    int listCode;

    public ResearchListModal() {

    }

    public ResearchListModal(String hawbCode, String clientNumber, String recipientName, int dna, String aptNo, String publicPlace, String streetName, String city, String state, int pinCode, int clientID, int contractID, int listCode) {
        this.hawbCode = hawbCode;
        this.clientNumber = clientNumber;
        this.RecipientName = recipientName;
        this.dna = dna;
        this.aptNo = aptNo;
        this.publicPlace = publicPlace;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.clientID = clientID;
        this.contractID = contractID;
        this.listCode = listCode;
    }

    public String getHawbCode() {
        return hawbCode;
    }

    public void setHawbCode(String hawbCode) {
        this.hawbCode = hawbCode;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getRecipientName() {
        return RecipientName;
    }

    public void setRecipientName(String recipientName) {
        RecipientName = recipientName;
    }

    public int getDna() {
        return dna;
    }

    public void setDna(int dna) {
        this.dna = dna;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(String publicPlace) {
        this.publicPlace = publicPlace;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getListCode() {
        return listCode;
    }

    public void setListCode(int listCode) {
        this.listCode = listCode;
    }
}
