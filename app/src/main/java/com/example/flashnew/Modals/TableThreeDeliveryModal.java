package com.example.flashnew.Modals;

import android.graphics.Bitmap;

public class TableThreeDeliveryModal {

    private String hawbCode;
    private String relationship;
    private String attempts;
    private String dateTime;
    private int batteryLevel;
    private String lowType;
    private Bitmap image;

    public TableThreeDeliveryModal(String hawbCode, String relationship, String attempts, String dateTime, int batteryLevel, String lowType, Bitmap image) {
        this.hawbCode = hawbCode;
        this.relationship = relationship;
        this.attempts = attempts;
        this.dateTime = dateTime;
        this.batteryLevel = batteryLevel;
        this.lowType = lowType;
        this.image = image;
    }

    public String getLowType() {
        return lowType;
    }

    public void setLowType(String lowType) {
        this.lowType = lowType;
    }

    public String getHawbCode() {
        return hawbCode;
    }

    public void setHawbCode(String hawbCode) {
        this.hawbCode = hawbCode;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAttempts() {
        return attempts;
    }

    public void setAttempts(String attempts) {
        this.attempts = attempts;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
