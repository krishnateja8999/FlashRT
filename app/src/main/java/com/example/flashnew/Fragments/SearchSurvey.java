package com.example.flashnew.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.flashnew.R;


public class SearchSurvey extends Fragment {

    private CardView client_local, client_alberto, lastCardView;
    private Button button_abrir, confirm_enviar;
    private RadioButton radio1, radio2, radio3, radio4;
    private RadioGroup radioGroup, radioGroup1;
    private Spinner spi;

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
        String[] values1 =
                {"Selecione o motivo", "Endereçando", "Ausente", "Não visitou", "Outras"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spi.setAdapter(adapter1);

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


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}