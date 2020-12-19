package com.example.flashnew.Fragments;

import android.content.DialogInterface;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.Modals.TableSixCollectModal;
import com.example.flashnew.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.BATTERY_SERVICE;
import static com.android.volley.VolleyLog.TAG;

public class CollectDetails extends Fragment {
    private TextView title, imei;
    private Spinner spinner;
    private EditText nameColeta;
    private EditText coletaIdenti;
    private DatabaseHelper mDatabaseHelper;
    private String strtext;
    private AppPrefernces prefernces;
    private Button buttonConfirmCollect, buttonCancelCollect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collect_details, container, false);
        title = view.findViewById(R.id.actionbarTitle);
        imei = view.findViewById(R.id.actionbarImei);
        spinner = view.findViewById(R.id.targetOptions);
        nameColeta = view.findViewById(R.id.nameColeta);
        coletaIdenti = view.findViewById(R.id.coletaIdenti);
        buttonConfirmCollect = view.findViewById(R.id.buttonConfirmCollect);
        buttonCancelCollect = view.findViewById(R.id.buttonCancelCollect);
        mDatabaseHelper = new DatabaseHelper(getContext());
        prefernces = new AppPrefernces(getContext());
        title.setVisibility(View.GONE);
        strtext = getArguments().getString("CID");
        imei.setText("Coleta: " + strtext);
        String[] values1 = {"Selecione Coletar", "Coletado", "Não coletado"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);

        buttonConfirmCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem().toString().equals("Selecione Coletar")) {
                    Toast.makeText(getContext(), "Selecione a Coletar", Toast.LENGTH_SHORT).show();
                } else if (nameColeta.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Por favor insira um nome", Toast.LENGTH_SHORT).show();
                } else if (coletaIdenti.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Por favor insira um rg", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseHelper.CheckTickMarkInTableFive(strtext);
                    SuccessDialog();
                }
            }
        });

        buttonCancelCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new Collect();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content, fr);
                ft.commit();
            }
        });

        return view;
    }

    private void SuccessDialog() {
        StoreColetaDetails();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("Sucesso");
        builder1.setMessage("Colleta com sucesso");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    private void StoreColetaDetails() {
        BatteryManager bm = (BatteryManager) getActivity().getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String spinnerValue = spinner.getSelectedItem().toString();

        TableSixCollectModal sixCollectModal = new TableSixCollectModal(strtext, nameColeta.getText().toString(), coletaIdenti.getText().toString(),
                formattedDate, spinnerValue, prefernces.getLatitude(), prefernces.getLongitude(), batLevel);
        Log.e(TAG, "StoreColetaDetails: " + sixCollectModal);
        boolean success = mDatabaseHelper.AddDataToTableSix(sixCollectModal);
        System.out.println(success);
        Fragment fr = new Collect();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fr);
        ft.commit();
    }
}