package com.example.flashnew.Modals;

public class HawbLists {
    String hawbCode;
    String name;
    float latitude;
    float longitude;
    boolean tick;

    public HawbLists(String hawbCode, String name, float latitude, float longitude, boolean tick) {
        this.hawbCode = hawbCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tick = tick;
    }

    public boolean isTick() {
        return tick;
    }

    public void setTick(boolean tick) {
        this.tick = tick;
    }

    public String getHawbCode() {
        return hawbCode;
    }

    public void setHawbCode(String hawbCode) {
        this.hawbCode = hawbCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
