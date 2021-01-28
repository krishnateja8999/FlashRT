package com.example.flashnew.Modals;

import android.graphics.Bitmap;

public class TableSixCollectModal {
    String collectID;
    String name;
    String identification;
    String dateTime;
    String type;
    String latitude;
    String longitude;
    int batteryPercentage;
    String imagePath;

    public TableSixCollectModal(String collectID, String name, String identification, String dateTime, String type, String latitude, String longitude, int batteryPercentage, String imagePath) {
        this.collectID = collectID;
        this.name = name;
        this.identification = identification;
        this.dateTime = dateTime;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.batteryPercentage = batteryPercentage;
        this.imagePath = imagePath;
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
