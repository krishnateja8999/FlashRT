package com.example.flashnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeScreen extends AppCompatActivity {

    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(),"sdsf",Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(MainActivity.this, Landing_Screen.class);
                Intent i = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}