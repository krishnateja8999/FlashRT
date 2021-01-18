package com.example.flashnew.Modals;

public class SearchListModalClass {
    String hawbCode;
    String name;
    String address;
    String tick_mark;
    String customerID;
    String clientID;
    String clientName;

    public SearchListModalClass(String hawbCode, String name, String address, String tick_mark, String customerID, String clientID, String clientName) {
        this.hawbCode = hawbCode;
        this.name = name;
        this.address = address;
        this.tick_mark = tick_mark;
        this.customerID = customerID;
        this.clientID = clientID;
        this.clientName = clientName;
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

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
