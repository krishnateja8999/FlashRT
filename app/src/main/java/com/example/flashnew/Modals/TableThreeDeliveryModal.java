package com.example.flashnew.Modals;

import android.graphics.Bitmap;

public class TableThreeDeliveryModal {

    private String hawbCode;
    private int relationship;
    private String attempts;
    private String dateTime;
    private int batteryLevel;
    private String lowType;
    private String photoBoolean;
    private String latitude;
    private String longitude;
    private String image;
    private String imageName;

    public TableThreeDeliveryModal(String hawbCode, int relationship, String attempts, String dateTime, int batteryLevel, String lowType, String photoBoolean, String latitude, String longitude, String image, String imageName) {
        this.hawbCode = hawbCode;
        this.relationship = relationship;
        this.attempts = attempts;
        this.dateTime = dateTime;
        this.batteryLevel = batteryLevel;
        this.lowType = lowType;
        this.photoBoolean = photoBoolean;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.imageName = imageName;
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

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
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

    public String getPhotoBoolean() {
        return photoBoolean;
    }

    public void setPhotoBoolean(String photoBoolean) {
        this.photoBoolean = photoBoolean;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
