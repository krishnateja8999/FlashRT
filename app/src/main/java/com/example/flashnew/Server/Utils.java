package com.example.flashnew.Server;

import android.os.BatteryManager;

import java.util.ArrayList;

import static android.content.Context.BATTERY_SERVICE;

public class Utils {
    public static final String TAG = "TAG";
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static String[] GetStringArray(ArrayList<String> arr) {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }


}
