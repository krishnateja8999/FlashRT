package com.example.flashnew.Modals;

import android.graphics.Bitmap;

public class TableSevenNotCollectedModal {
    String collectID;
    String dateTime;
    String type;
    String latitude;
    String longitude;
    int batteryPercentage;
    String image;

    public TableSevenNotCollectedModal(String collectID, String dateTime, String type, String latitude, String longitude, int batteryPercentage, String image) {
        this.collectID = collectID;
        this.dateTime = dateTime;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.batteryPercentage = batteryPercentage;
        this.image = image;
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
