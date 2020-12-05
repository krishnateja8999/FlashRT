package com.example.flashnew.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AppPrefernces {
    private static final String PREFERENCE_NAME = "Flash";
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public AppPrefernces(Context context) {
        preference = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        editor = preference.edit();
    }

    public void setLanding(String pwd) {
        editor.putString("gmail", pwd);
        editor.apply();
    }

    public String getLanding() {
        return preference.getString("gmail", "N/A");
    }

    //login response store

    public void setID(String id) {
        editor.putString("LoginID", id);
        editor.apply();
    }

    public String getID() {
        return preference.getString("LoginID", " ");
    }

    public void setUserName(String userName) {
        editor.putString("LoginUserName", userName);
        editor.apply();
    }

    public String getUserName() {
        return preference.getString("LoginUserName", " ");
    }

    public void setName(String name) {
        editor.putString("LoginName", name);
        editor.apply();
    }

    public String getName() {
        return preference.getString("LoginName", " ");
    }

    public void setPaso(String paso) {
        editor.putString("Paso", paso);
        editor.apply();
    }

    public String getPaso() {
        return preference.getString("Paso", " ");
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        return preference.getString("LoginID", null) != null;
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.apply();
    }

    // List screen preferences

    public void setListID(String ListID) {
        editor.putString("ListID", ListID);
        editor.apply();
    }

    public String getListID() {
        return preference.getString("ListID", " ");
    }

    public void clearListID() {
        editor = preference.edit();
        editor.remove("ListID");
        editor.apply();
    }

    public void setLowType(String LowType) {
        editor.putString("LowType", LowType);
        editor.apply();
    }

    public String getLowType() {
        return preference.getString("LowType", " ");
    }

    public void setIMEI(String IMEI) {
        editor.putString("IMEI", IMEI);
        editor.apply();
    }

    public String getIMEI() {
        return preference.getString("IMEI", " ");
    }

    public void setFranchise(String Franchise) {
        editor.putString("Franchise", Franchise);
        editor.apply();
    }

    public String getFranchise() {
        return preference.getString("Franchise", " ");
    }

    public void setSystem(String system) {
        editor.putString("System", system);
        editor.apply();
    }

    public String getSystem() {
        return preference.getString("System", " ");
    }


}
