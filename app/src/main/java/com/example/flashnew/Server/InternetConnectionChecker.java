package com.example.flashnew.Server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class InternetConnectionChecker {
    private Context mContext;

    public InternetConnectionChecker(Context mcontext) {
        this.mContext = mcontext;
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else
            return false;

    }

    public void failureAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("No Internet Connection");
        // Setting Dialog Message
        alertDialog.setMessage("You don't have internet connection.");
        // Setting alert dialog icon
        // Setting OK Button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                mContext.startActivity(i);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                //((Activity)_context).finish();
            }
        });
        // Showing Alert Message
        AlertDialog alert = alertDialog.create();
        Activity activity = (Activity) mContext;
        if (!activity.isFinishing())
            alert.show();

    }

    public void serverErrorAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("Connectivity Issue.");
        // Setting Dialog Message
        alertDialog.setMessage("Please check Internet connection and retry.");
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialog.create();
        Activity activity = (Activity) mContext;
        if (!activity.isFinishing())
            alert.show();
    }
}
