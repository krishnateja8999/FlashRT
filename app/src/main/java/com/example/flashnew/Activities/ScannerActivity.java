package com.example.flashnew.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.flashnew.Fragments.Collect;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.Modals.TableFiveModel;
import com.example.flashnew.R;
import com.example.flashnew.Server.Utils;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        prefernces = new AppPrefernces(this);
        mDatabaseHelper = new DatabaseHelper(this);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String Qcode = result.getText();
                        Log.e("TAG", "run: " + Qcode);

                        String[] item = Qcode.split("\\|");
                        boolean check = false;
                        try {
                            check = mDatabaseHelper.CheckColetaData(item[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (item.length < 10) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ScannerActivity.this);
                            builder1.setTitle("Erro");
                            builder1.setMessage("Inválido");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                            //Creating dialog box
                            AlertDialog alert1 = builder1.create();
                            alert1.show();
                        } else if (check) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ScannerActivity.this);
                            builder1.setTitle(getResources().getString(R.string.Login_screen1));
                            builder1.setMessage("Coleta " + item[2] + " já foi adicionado");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                            //Creating dialog box
                            AlertDialog alert1 = builder1.create();
                            alert1.show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ScannerActivity.this);
                            builder1.setTitle("Sucesso");
                            builder1.setMessage("Coleta " + item[2] + " lido com sucesso");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent("qr_code_validate");
                                    LocalBroadcastManager.getInstance(ScannerActivity.this).sendBroadcast(intent);
                                    finish();
                                }
                            });
                            //Creating dialog box
                            AlertDialog alert1 = builder1.create();
                            alert1.show();
                            TableFiveModel tableFiveModel = new TableFiveModel(item[2], item[8], item[9], item[12], item[13], item[14]);
                            boolean success = mDatabaseHelper.AddDateToTableFive(tableFiveModel);
                            System.out.println(success);
                        }

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