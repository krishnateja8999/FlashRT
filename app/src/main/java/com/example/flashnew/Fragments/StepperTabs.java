package com.example.flashnew.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashnew.Adapters.MyStepperAdapter;
import com.example.flashnew.R;
import com.stepstone.stepper.StepperLayout;

public class StepperTabs extends Fragment {

    private StepperLayout mStepperLayout;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stepper_tabs, container, false);
        mStepperLayout = (StepperLayout) v.findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new MyStepperAdapter(getChildFragmentManager(), getActivity()));
        //mStepperLayout.setAdapter(new MyStepperAdapter(((AppCompatActivity) context).getSupportFragmentManager(), getActivity()));


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}