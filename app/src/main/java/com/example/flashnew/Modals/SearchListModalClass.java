package com.example.flashnew.Modals;

public class SearchListModalClass {
    String hawbCode;
    String name;
    String address;
    String tick_mark;

    public SearchListModalClass(String hawbCode, String name, String address, String tick_mark) {
        this.hawbCode = hawbCode;
        this.name = name;
        this.address = address;
        this.tick_mark = tick_mark;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTick_mark() {
        return tick_mark;
    }

    public void setTick_mark(String tick_mark) {
        this.tick_mark = tick_mark;
    }
}
