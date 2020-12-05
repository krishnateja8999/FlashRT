package com.example.flashnew.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.flashnew.R;

public class Profile_Class extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__class);
        toolbar = findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);


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