package com.example.flashnew.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.R;


/**
 * FRAGMENT CLASS NOT IN USE
 */

public class Dashboard extends Fragment {
    LinearLayout linearLayout, linearLayout1, linearLayout2;
    Landing_Screen context;
    CardView sync_data;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dashboard, container, false);
        linearLayout = view.findViewById(R.id.buttonLista);
        linearLayout1 = view.findViewById(R.id.buttonColeta);
        linearLayout2 = view.findViewById(R.id.buttonPesquisa);
        sync_data = view.findViewById(R.id.sync_data);

        sync_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Carregando dados");
                progressDialog.setMessage("Por favor, espere..");
                progressDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                    }
                }, 3000);


            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.changeFragment1(2);
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.changeFragment1(3);
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.changeFragment1(4);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }
}
