package com.example.flashnew.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.flashnew.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class ResearchTwo extends Fragment implements Step {

    private RadioGroup researchRGroup1, researchRGroup2, researchRGroup3, researchRGroup4, researchRGroup5, researchRGroup6;
    private EditText vigilantes, observacao, comments;
    private TextView photo1, photo2, photo3, photo4, photo5;
    private ImageView image1, image2, image3, image4, image5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_two, container, false);

        researchRGroup1 = v.findViewById(R.id.researchRGroup1);
        researchRGroup2 = v.findViewById(R.id.researchRGroup2);
        researchRGroup3 = v.findViewById(R.id.researchRGroup3);
        researchRGroup4 = v.findViewById(R.id.researchRGroup4);
        researchRGroup5 = v.findViewById(R.id.researchRGroup5);
        researchRGroup6 = v.findViewById(R.id.researchRGroup6);
        vigilantes = v.findViewById(R.id.vigilantes);
        observacao = v.findViewById(R.id.observacao);
        comments = v.findViewById(R.id.comments);

        //camera
        photo1 = v.findViewById(R.id.photo1);
        photo2 = v.findViewById(R.id.photo2);
        photo3 = v.findViewById(R.id.photo3);
        photo4 = v.findViewById(R.id.photo4);
        photo5 = v.findViewById(R.id.photo5);

        //tick
        image1 = v.findViewById(R.id.image1);
        image2 = v.findViewById(R.id.image2);
        image3 = v.findViewById(R.id.image3);
        image4 = v.findViewById(R.id.image4);
        image5 = v.findViewById(R.id.image5);

        return v;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}