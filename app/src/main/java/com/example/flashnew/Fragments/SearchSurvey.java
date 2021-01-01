package com.example.flashnew.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.Adapters.MyStepperAdapter;
import com.example.flashnew.R;
import com.stepstone.stepper.StepperLayout;


public class SearchSurvey extends Fragment {

    private CardView client_local, client_alberto, lastCardView;
    private Button button_abrir, confirm_enviar;
    private RadioButton radio1, radio2, radio3, radio4;
    private RadioGroup radioGroup, radioGroup1;
    private Spinner spi, spi2;
    private String[] values1, enderec, ausente, nao_visitado, outros;
    private String researchName;
    private Context context;
    private TextView researchHawb;
    private StepperLayout mStepperLayout;
    private LinearLayout bl1, actions_lay1;
    private MyStepperAdapter mStepperAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_survey, container, false);

        client_local = view.findViewById(R.id.client_local);
        client_alberto = view.findViewById(R.id.client_alberto);
        lastCardView = view.findViewById(R.id.lastCardView);
        button_abrir = view.findViewById(R.id.button_abrir);
        confirm_enviar = view.findViewById(R.id.confirm_enviar);
        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
        radio3 = view.findViewById(R.id.radio3);
        radio4 = view.findViewById(R.id.radio4);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        spi = view.findViewById(R.id.spi);
        spi2 = view.findViewById(R.id.spi2);
        bl1 = view.findViewById(R.id.bl1);
        actions_lay1 = view.findViewById(R.id.actions_lay1);
        assert getArguments() != null;
        researchName = getArguments().getString("Research");
        researchHawb = view.findViewById(R.id.researchHawb);
        researchHawb.setText("Pesquisa: " + researchName);
        mStepperLayout = (StepperLayout) view.findViewById(R.id.stepperLayout);
        mStepperAdapter = new MyStepperAdapter(getChildFragmentManager(), context);
        mStepperLayout.setAdapter(mStepperAdapter);
        values1 = getResources().getStringArray(R.array.motivo_grupo);
        enderec = getResources().getStringArray(R.array.motivo_dev);
        ausente = getResources().getStringArray(R.array.motivo_ausente);
        nao_visitado = getResources().getStringArray(R.array.motivo_nao_visitado);
        outros = getResources().getStringArray(R.array.motivo_outros);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spi.setAdapter(adapter1);
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spi2.setVisibility(View.GONE);
                } else if (position == 1) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, enderec);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                } else if (position == 2) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ausente);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                } else if (position == 3) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nao_visitado);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                } else if (position == 4) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, outros);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //This overrides the radiogroup onCheckListener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    if (checkedRadioButton.getText().equals("Sim")) {
                        client_alberto.setVisibility(View.VISIBLE);
                        lastCardView.setVisibility(View.GONE);
                        confirm_enviar.setVisibility(View.GONE);
                        radio3.setChecked(false);
                        radio4.setChecked(false);
                    } else {
                        lastCardView.setVisibility(View.VISIBLE);
                        confirm_enviar.setVisibility(View.VISIBLE);
                        client_alberto.setVisibility(View.GONE);
                        button_abrir.setVisibility(View.GONE);
                    }
                }
            }
        });

        //This overrides the radiogroup onCheckListener
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    if (checkedRadioButton.getText().equals("Sim")) {
                        button_abrir.setVisibility(View.VISIBLE);
                        confirm_enviar.setVisibility(View.GONE);

                    } else {
                        confirm_enviar.setVisibility(View.VISIBLE);
                        button_abrir.setVisibility(View.GONE);
                    }
                }
            }
        });

        button_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bl1.setVisibility(View.GONE);
                actions_lay1.setVisibility(View.GONE);
                mStepperLayout.setVisibility(View.VISIBLE);
//                Fragment fr = new StepperTabs();
//                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context)
//                        .getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content, fr);
//                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}