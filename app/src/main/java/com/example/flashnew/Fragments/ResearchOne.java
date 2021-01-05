package com.example.flashnew.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

public class ResearchOne extends Fragment implements Step {

    private TextView baseFile, dateOfVerify, empresa, cnjp, ResponseBaseFile, endereco, bairro, cidade, uf, cep;
    private AppPrefernces prefernces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_one, container, false);

        baseFile = v.findViewById(R.id.baseFile);
        dateOfVerify = v.findViewById(R.id.dateOfVerify);
        empresa = v.findViewById(R.id.empresa);
        cnjp = v.findViewById(R.id.cnjp);
        ResponseBaseFile = v.findViewById(R.id.ResponseBaseFile);
        endereco = v.findViewById(R.id.endereco);
        bairro = v.findViewById(R.id.bairro);
        cidade = v.findViewById(R.id.cidade);
        uf = v.findViewById(R.id.uf);
        cep = v.findViewById(R.id.cep);
        prefernces = new AppPrefernces(getContext());

        return v;
    }

    //For Validation add BlockingStep in implementation instead of step.
    @Nullable
    @Override
    public VerificationError verifyStep() {
        AddToPreferences();
        Toast.makeText(getContext(), "First", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void AddToPreferences() {
        JSONObject object = new JSONObject();
        try {
            object.put("FfvName", baseFile.getText().toString());
            object.put("Email", dateOfVerify.getText().toString());
            object.put("ContactNumber", empresa.getText().toString());
            object.put("StateId", cnjp.getText().toString());
            object.put("State", ResponseBaseFile.getText().toString());
            object.put("DistrictId", endereco.getText().toString());
            object.put("District", bairro.getText().toString());
            object.put("LocationId", cidade.getText().toString());
            object.put("Location", uf.getText().toString());
            object.put("PostcodeId", cep.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        prefernces.setResearchOne(null);
        prefernces.setResearchOne(object);
        Log.e("TAG", "AddToPreferences: " + prefernces.getResearchOne());
    }
}