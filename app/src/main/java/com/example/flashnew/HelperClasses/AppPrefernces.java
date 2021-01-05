package com.example.flashnew.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

import static android.content.Context.MODE_PRIVATE;

public class AppPrefernces {
    private static final String PREFERENCE_NAME = "Flash";
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public AppPrefernces(Context context) {
        preference = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        editor = preference.edit();
    }
    //login response store

    public void setID(String id) {
        editor.putString("LoginID", id);
        editor.apply();
    }

    public String getID() {
        return preference.getString("LoginID", "ggg");
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

    public void setHostUrl(String url) {
        editor.putString("HostURL", url);
        editor.apply();
    }

    public String getHostUrl() {
        return preference.getString("HostURL", " ");
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        return preference.getString("LoginID", null) != null;
    }

    public boolean isLoggedIn1() {
        return preference.getString("HostURL", null) != null;
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

    public void setPhotoBoolean(String photoBoolean) {
        editor.putString("PhotoBoolean", photoBoolean);
        editor.apply();
    }

    public String getPhotoBoolean() {
        return preference.getString("PhotoBoolean", " ");
    }

    public void setLatitude(String Latitude) {
        editor.putString("Latitude", Latitude);
        editor.apply();
    }

    public String getLatitude() {
        return preference.getString("Latitude", " ");
    }

    public void setLongitude(String longitude) {
        editor.putString("Longitude", longitude);
        editor.apply();
    }

    public String getLongitude() {
        return preference.getString("Longitude", " ");
    }

    //Collect Screen preferences

    //Profile Screen Preference

    public void setProfileImage(String img) {
        editor.putString("ProfileImage", img);
        editor.apply();
    }

    public String getProfileImage() {
        return preference.getString("ProfileImage", " ");
    }

    //Research Screen preferences
    public void setResearchOne(JSONObject object) {
        Gson gson = new Gson();
        String list = gson.toJson(object);
        editor.putString("ResearchOne", list);
        editor.apply();
    }

    public JSONObject getResearchOne() {
        Gson gson = new Gson();
        String list = preference.getString("ResearchOne", "N/A");
        Type type = new TypeToken<JSONObject>() {
        }.getType();
        return gson.fromJson(list, type);
    }


}
