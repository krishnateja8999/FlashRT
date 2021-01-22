package com.example.flashnew.Adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.flashnew.Fragments.ResearchOne;
import com.example.flashnew.Fragments.ResearchThree;
import com.example.flashnew.Fragments.ResearchTwo;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    public MyStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                final ResearchOne step1 = new ResearchOne();
                Bundle b = new Bundle();
                b.putInt("0", position);
                step1.setArguments(b);
                return step1;

            case 1:
                final ResearchTwo step2 = new ResearchTwo();
                Bundle bundle = new Bundle();
                bundle.putInt("1", position);
                step2.setArguments(bundle);
                return step2;

            case 2:
                final ResearchThree step3 = new ResearchThree();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("2", position);
                step3.setArguments(bundle1);
                return step3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        switch (position) {
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("IDENTIFICAÇÃO") //can be a CharSequence instead
                        .setEndButtonLabel("Próximo")
                        .setBackButtonLabel("De volta")
                        .create();

            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("CHECK LIST") //can be a CharSequence instead
                        .setEndButtonLabel("Próximo")
                        .setBackButtonLabel("De volta")
                        .create();

            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("RESPONDENTE") //can be a CharSequence instead
                        .setEndButtonLabel("Enviar Respostas")
                        .setBackButtonLabel("De volta")
                        .create();
        }
        return null;
    }
}
