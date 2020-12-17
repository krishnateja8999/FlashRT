package com.example.flashnew.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashnew.Adapters.SearchListAdapter;
import com.example.flashnew.Modals.SearchListModalClass;
import com.example.flashnew.R;

import java.util.ArrayList;

public class Search extends Fragment {
    TextView title, imei;
    private ArrayList<SearchListModalClass> searchListModalClasses;
    private RecyclerView recyclerViewSearchList;
    private LinearLayoutManager layoutManager;
    private SearchListAdapter searchAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search, container, false);
        title = view.findViewById(R.id.actionbarTitle);
        imei = view.findViewById(R.id.actionbarImei);
        searchListModalClasses = new ArrayList<>();
        recyclerViewSearchList = view.findViewById(R.id.recyclerViewSearchList);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchList.setLayoutManager(layoutManager);

        searchListModalClasses.add(new SearchListModalClass("Augusto Vasquez", "PRAJA DO CERIO 21 VILA PAULISTA, SAO PAULO, SP"));
        searchAdapter = new SearchListAdapter(getActivity(), searchListModalClasses);
        recyclerViewSearchList.setAdapter(searchAdapter);

        return view;
    }

    //        String[] values1 =
//                {"Selecione o motivo", "Endereçando", "Ausente", "Não visitou", "Outras"};
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
//        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spinner.setAdapter(adapter1);
//        ss = view.findViewById(R.id.ss);
//        sss1=view.findViewById(R.id.sss1);
//        title.setText("Pesquisa");
//        imei.setText("IMEI : 9876543210123");
//        bl = view.findViewById(R.id.bl);
//        bl1 = view.findViewById(R.id.bl1);
//        radioButton1 = view.findViewById(R.id.radio1);
//        radioButton2 = view.findViewById(R.id.radio2);
//        radioButton3 = view.findViewById(R.id.radio3);
//        radioButton4 = view.findViewById(R.id.radio4);
//        RadioGroup rGroup = view.findViewById(R.id.radioGroup1);
//// This will get the radiobutton in the radiogroup that is checked
//        RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
//
//        // This overrides the radiogroup onCheckListener
//        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // This will get the radiobutton that has changed in its check state
//                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//                // This puts the value (true/false) into the variable
//                boolean isChecked = checkedRadioButton.isChecked();
//                // If the radiobutton that has changed in check state is now checked...
//                if (isChecked) {
//                    // Changes the textview's text to "Checked: example radiobutton text"
//                    Toast.makeText(getContext(), checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
//                    if (checkedRadioButton.getText().equals("Sim")) {
//                        cardView.setVisibility(View.VISIBLE);
//                        lay.setVisibility(View.GONE);
//                    } else {
//                        lay.setVisibility(View.VISIBLE);
//                        cardView.setVisibility(View.GONE);
//                        ss.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
//        RadioGroup rGroup1 = view.findViewById(R.id.radioGroup1);
//// This will get the radiobutton in the radiogroup that is checked
//        RadioButton checkedRadioButton1 = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
//
//        // This overrides the radiogroup onCheckListener
//        rGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // This will get the radiobutton that has changed in its check state
//                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//                // This puts the value (true/false) into the variable
//                boolean isChecked = checkedRadioButton.isChecked();
//                // If the radiobutton that has changed in check state is now checked...
//                if (isChecked) {
//                    // Changes the textview's text to "Checked: example radiobutton text"
//                    Toast.makeText(getContext(), checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
//                    if (checkedRadioButton.getText().equals("Yes")) {
//                        ss.setVisibility(View.VISIBLE);
//                    }
//
//                }
//            }
//        });
//
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bl.setVisibility(View.GONE);
//                bl1.setVisibility(View.VISIBLE);
//
//
//            }
//        });
}
