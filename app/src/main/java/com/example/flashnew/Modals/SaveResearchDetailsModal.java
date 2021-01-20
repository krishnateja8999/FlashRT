package com.example.flashnew.Modals;

public class SaveResearchDetailsModal {
    String hawb_code;
    String dateTime;
    int batteryLevel;
    String latitude;
    String longitude;
    String xmlBody;
    String listCodes;

    public SaveResearchDetailsModal(String hawb_code, String dateTime, int batteryLevel, String latitude, String longitude, String xmlBody, String listCodes) {
        this.hawb_code = hawb_code;
        this.dateTime = dateTime;
        this.batteryLevel = batteryLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.xmlBody = xmlBody;
        this.listCodes = listCodes;
    }

    public String getHawb_code() {
        return hawb_code;
    }

    public void setHawb_code(String hawb_code) {
        this.hawb_code = hawb_code;
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

    public String getXmlBody() {
        return xmlBody;
    }

    public void setXmlBody(String xmlBody) {
        this.xmlBody = xmlBody;
    }

    public String getListCodes() {
        return listCodes;
    }

    public void setListCodes(String listCodes) {
        this.listCodes = listCodes;
    }
}
