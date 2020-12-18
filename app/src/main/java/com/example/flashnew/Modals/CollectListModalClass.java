package com.example.flashnew.Modals;

public class CollectListModalClass {
    String hawbCode;
    String address;

    public CollectListModalClass(String hawbCode, String address) {
        this.hawbCode = hawbCode;
        this.address = address;
    }

    public String getHawbCode() {
        return hawbCode;
    }

    public void setHawbCode(String hawbCode) {
        this.hawbCode = hawbCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
