package com.example.flashnew.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.R;
import com.example.flashnew.Server.Utils;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class ResearchThree extends Fragment implements Step {

    private EditText responseDesta;
    private TextView photo;
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_three, container, false);

        responseDesta = v.findViewById(R.id.responseDesta);
        photo = v.findViewById(R.id.photo);
        image = v.findViewById(R.id.image);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        FinalDialog("Confirme as respostas", "Deseja finalizar?");
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void FinalDialog(String successDialog, String successDesc) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle(successDialog);
        builder1.setMessage(successDesc);
        builder1.setCancelable(false);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FinalDialog2("Sucesso", "Pesquisa finalizada com successo");
            }
        });
        builder1.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    private void FinalDialog2(String successDialog2, String successDesc2) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle(successDialog2);
        builder1.setMessage(successDesc2);
        builder1.setCancelable(false);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                changeFragment(new Search());

            }
        });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public void changeFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


}