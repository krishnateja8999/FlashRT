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
    private TextView imeiText, pendingHawbLists, listaColetasPendentes, syncHawb, listaColetasAguardandoSincronizar, listaAguardandoSincronizarImagens, listaColetasAguardandoSincronizarImagem, listaEntregues, listaDevolvidos, listaColetasFeitas, listaPesquisaAguardandoSincronizar, listaPesquisaPendente, listaPesquisaFotoAguardandoSincronizar;
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
        syncHawb = findViewById(R.id.syncHawb);
        listaColetasAguardandoSincronizar = findViewById(R.id.listaColetasAguardandoSincronizar);
        listaAguardandoSincronizarImagens = findViewById(R.id.listaAguardandoSincronizarImagens);
        listaColetasAguardandoSincronizarImagem = findViewById(R.id.listaColetasAguardandoSincronizarImagem);
        listaEntregues = findViewById(R.id.listaEntregues);
        listaDevolvidos = findViewById(R.id.listaDevolvidos);
        listaColetasFeitas = findViewById(R.id.listaColetasFeitas);
        listaPesquisaPendente = findViewById(R.id.listaPesquisaPendente);
        listaPesquisaAguardandoSincronizar = findViewById(R.id.listaPesquisaAguardandoSincronizar);
        listaPesquisaFotoAguardandoSincronizar = findViewById(R.id.listaPesquisaFotoAguardandoSincronizar);
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
        Cursor ResearchDetails = mDatabaseHelper.GetResearchDetails();
        Cursor ResearchImages = mDatabaseHelper.GetResearchImages();

        String totalHawbsPending = String.valueOf(HawbPending.getCount());
        String totalCollectPending = String.valueOf(CollectPending.getCount());
        int HawbSync = mDatabaseHelper.DashSyncCount();
        int CollectSync = mDatabaseHelper.DashCollectSyncCount();
        int HawbImageSync = mDatabaseHelper.DashImageSync();
        int NotCollectSync = mDatabaseHelper.DashNotCollectImageSync();
        int HawbDelivery = mDatabaseHelper.TotalDeliveryCount();
        int HawbReturn = mDatabaseHelper.TotalReturnCount();
        int CollectCount = mDatabaseHelper.TotalCollectCount();
        int ResearchPending = mDatabaseHelper.TotalResearchPendingCount();
        int ResearchSyncCount = ResearchDetails.getCount();
        int ResearchImageCount = ResearchImages.getCount();

        listaEntregues.setText(String.valueOf(HawbDelivery));
        listaDevolvidos.setText(String.valueOf(HawbReturn));
        pendingHawbLists.setText(totalHawbsPending);
        syncHawb.setText(String.valueOf(HawbSync));
        listaAguardandoSincronizarImagens.setText(String.valueOf(HawbImageSync));
        listaColetasPendentes.setText(totalCollectPending);
        listaColetasFeitas.setText(String.valueOf(CollectCount));
        listaColetasAguardandoSincronizar.setText(String.valueOf(CollectSync));
        listaColetasAguardandoSincronizarImagem.setText(String.valueOf(NotCollectSync));
        listaPesquisaPendente.setText(String.valueOf(ResearchPending));
        listaPesquisaAguardandoSincronizar.setText(String.valueOf(ResearchSyncCount));
        listaPesquisaFotoAguardandoSincronizar.setText(String.valueOf(ResearchImageCount));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}