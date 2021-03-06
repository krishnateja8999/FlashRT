package com.example.flashnew.Server;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Utils {
    public static final String TAG = "TAG";
    public static final String VERSION = "3.9.0";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String MASTER_PASSWORD = "tbmvc";
    public static final String IMAGE_SERVICE_PROJECT_ID = "digitalizacao-174822";
    public static final String IMAGE_SERVICE_BUCKET = "gunsandroses-909459-dontcry-571723"; // test bucket.
    //"saopaulo-blackbirds-imagens-174822"    //production bucket.

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

    public static AlertDialog DialogClass(Context context, String title, String message, String positiveBtn) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
        return alert1;
    }

    public static boolean FindPerimeter(double cPoint, double cPoint2, double lat, double longi) {
        float[] results = new float[1];
        Location.distanceBetween(cPoint, cPoint2, lat, longi, results);
        float distanceInMeters = results[0];
        return distanceInMeters < 1000;
    }

    public static double GetLatitude(List<Double> a) {
        String b = TextUtils.join(",", a);
        String[] item = b.split(",");
        return Double.parseDouble(item[0]);
    }

    public static double GetLongitude(List<Double> a) {
        String b = TextUtils.join(",", a);
        String[] item = b.split(",");
        return Double.parseDouble(item[1]);
    }

    public static boolean CheckZeroInLocation(double latiti) {
        if (latiti == 0.0 || latiti == 0) {
            return true;
        } else {
            return false;
        }
    }
}
