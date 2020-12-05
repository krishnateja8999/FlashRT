package com.example.flashnew.Modals;

public class TableOneDelivererModal {
    String franchise;
    int lists;
    int deliveryID;
    String delivererName;
    int totalDocuments;

    public TableOneDelivererModal(String franchise, int lists, int deliveryID, String delivererName, int totalDocuments) {
        this.franchise = franchise;
        this.lists = lists;
        this.deliveryID = deliveryID;
        this.delivererName = delivererName;
        this.totalDocuments = totalDocuments;
    }

    public String getFranchise() {
        return franchise;
    }

    public void setFranchise(String franchise) {
        this.franchise = franchise;
    }

    public int getLists() {
        return lists;
    }

    public void setLists(int lists) {
        this.lists = lists;
    }

    public int getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    public int getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(int totalDocuments) {
        this.totalDocuments = totalDocuments;
    }
}
