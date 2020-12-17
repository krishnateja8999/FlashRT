package com.example.flashnew.Modals;

public class HawbLists {
    String hawbCode;
    String name;
    String clientNumber;
    String tick;

    public HawbLists(String hawbCode, String name, String clientNumber, String tick) {
        this.hawbCode = hawbCode;
        this.name = name;
        this.clientNumber = clientNumber;
        this.tick = tick;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
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
}
