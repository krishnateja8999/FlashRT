package com.example.flashnew.HelperClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class GetCurrentLocation {

    private static AppPrefernces prefernces;
    private static LocationRequest locationRequest;
    private static final boolean isContinue = false;
    private static final int LOCATION_REQUEST = 1000;
    private static LocationCallback locationCallback;
    private static double wayLatitude = 0.0, wayLongitude = 0.0;
    private static FusedLocationProviderClient mFusedLocationClient;

    public static void Location(Activity context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            Log.e("TAG", "GPSHelperClass: " + String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            Log.e("TAG", "GPSHelperClass: " + String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        getLocation(context);
    }

    public static void getLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);

        } else {
            prefernces = new AppPrefernces(activity);
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        Log.e("TAG", "GPSHelperClass: " + String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        prefernces.setLatitude(String.valueOf(wayLatitude));
                        prefernces.setLongitude(String.valueOf(wayLongitude));
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            }
        }
    }
}
