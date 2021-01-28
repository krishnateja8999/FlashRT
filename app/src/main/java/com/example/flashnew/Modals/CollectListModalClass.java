package com.example.flashnew.Modals;

public class CollectListModalClass {
    String hawbCode;
    String address;
    String tick_mark;
    String dna;

    public CollectListModalClass(String hawbCode, String address, String tick_mark, String dna) {
        this.hawbCode = hawbCode;
        this.address = address;
        this.tick_mark = tick_mark;
        this.dna = dna;
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

    public String getTick_mark() {
        return tick_mark;
    }

    public void setTick_mark(String tick_mark) {
        this.tick_mark = tick_mark;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }
}
