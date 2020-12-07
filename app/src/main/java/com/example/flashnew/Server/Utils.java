package com.example.flashnew.Server;

import android.content.Context;

import java.util.ArrayList;


public class Utils {
    public static final String TAG = "TAG";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private Context context;


    public static String[] GetStringArray(ArrayList<String> arr) {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }

    public static String ConvertArrayListToString(ArrayList<String> arrList) {
        StringBuffer sb = new StringBuffer();
        for (String s : arrList) {
            sb.append(s);
        }
        String str = sb.toString();
        return str;
    }

//    public static boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

}
