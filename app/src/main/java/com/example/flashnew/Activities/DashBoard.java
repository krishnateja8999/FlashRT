package com.example.flashnew.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.R;
import com.example.flashnew.Server.Utils;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

public class DashBoard extends AppCompatActivity {
    private static final String TAG = "DashBoard";
    private Toolbar toolbar;
    private TextView imeiText, pendingHawbLists, listaColetasPendentes;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        imeiText = findViewById(R.id.imeiText);
        prefernces = new AppPrefernces(this);
        imeiText.setText("IMEI: " + prefernces.getIMEI());
        pendingHawbLists = findViewById(R.id.pendingHawbLists);
        listaColetasPendentes = findViewById(R.id.listaColetasPendentes);
        mDatabaseHelper = new DatabaseHelper(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        CheckTotalData();

    }

    private void CheckTotalData() {
        Cursor HawbPending = mDatabaseHelper.getData();
        Cursor CollectPending = mDatabaseHelper.GetDataFromTableFive();

        String totalHawbsPending = String.valueOf(HawbPending.getCount());
        String totalCollectPending = String.valueOf(CollectPending.getCount());

        pendingHawbLists.setText(totalHawbsPending);
        listaColetasPendentes.setText(totalCollectPending);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}