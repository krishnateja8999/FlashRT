package com.example.flashnew.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.flashnew.Fragments.Collect;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.R;
import com.example.flashnew.Server.Utils;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private AppPrefernces prefernces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        prefernces = new AppPrefernces(this);
        prefernces.setQRCode("null");

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String Qcode = result.getText();
                        prefernces.setQRCode(Qcode);
                        Intent intent = new Intent("qr_code_validate");
                        LocalBroadcastManager.getInstance(ScannerActivity.this).sendBroadcast(intent);
                        finish();
//                        String[] item = Qcode.split("\\|");
//                        if (item.length<10){
//                            Utils.DialogClass(ScannerActivity.this,"Erro", "Inválido", "OK" );
//                        }else {
//                            Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_LONG).show();
//                            finish();
//                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}