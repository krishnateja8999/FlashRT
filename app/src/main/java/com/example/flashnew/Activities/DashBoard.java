package com.example.flashnew.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.R;

public class DashBoard extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title, imei;
    private AppPrefernces prefernces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.actionbarTitle);
        imei = findViewById(R.id.actionbarImei);
        prefernces = new AppPrefernces(this);
        title.setVisibility(View.GONE);
        imei.setText("IMEI: " + prefernces.getIMEI());


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();

        }
        return true;

    }
}