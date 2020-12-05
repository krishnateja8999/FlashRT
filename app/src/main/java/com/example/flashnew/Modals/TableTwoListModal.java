package com.example.flashnew.Modals;

public class TableTwoListModal {
    int customerID;
    int contractID;
    String hawbCode;
    String numberOrder;
    String recipientName;
    int dna;
    int attempts;
    String specialPhoto;
    int score;
    float latitude;
    float longitude;

    public TableTwoListModal(int customerID, int contractID, String hawbCode, String numberOrder, String recipientName, int dna, int attempts, String specialPhoto, int score, float latitude, float longitude) {
        this.customerID = customerID;
        this.contractID = contractID;
        this.hawbCode = hawbCode;
        this.numberOrder = numberOrder;
        this.recipientName = recipientName;
        this.dna = dna;
        this.attempts = attempts;
        this.specialPhoto = specialPhoto;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public String getHawbCode() {
        return hawbCode;
    }

    public void setHawbCode(String hawbCode) {
        this.hawbCode = hawbCode;
    }

    public String getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(String numberOrder) {
        this.numberOrder = numberOrder;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public int getDna() {
        return dna;
    }

    public void setDna(int dna) {
        this.dna = dna;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getSpecialPhoto() {
        return specialPhoto;
    }

    public void setSpecialPhoto(String specialPhoto) {
        this.specialPhoto = specialPhoto;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
