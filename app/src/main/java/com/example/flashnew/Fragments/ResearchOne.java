package com.example.flashnew.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.R;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResearchOne extends Fragment implements BlockingStep {

    private Landing_Screen context;
    private AppPrefernces prefernces;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private static final String TAG = "ResearchOne";
    private TextView baseFile, dateOfVerify, empresa, cnjp, ResponseBaseFile, endereco, bairro, cidade, uf, cep, telePhone, email;

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
        telePhone = v.findViewById(R.id.telePhone);
        email = v.findViewById(R.id.email);
        prefernces = new AppPrefernces(context);
        dateOfVerify.setInputType(InputType.TYPE_NULL);

        dateOfVerify.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show();
                }
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateOfVerify.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        return v;
    }

    private void Validate(StepperLayout.OnNextClickedCallback callback) throws JSONException {
        if (baseFile.getText().length() == 0) {
            baseFile.setError("Por favor, insira sua filial");
        } else if (dateOfVerify.getText().length() == 0) {
            dateOfVerify.setError("Selecione a data");
        } else if (empresa.getText().length() == 0) {
            empresa.setError("Digite o nome da sua empresa");
        } else if (cnjp.getText().length() == 0) {
            cnjp.setError("Insira CNPJ");
        } else if (ResponseBaseFile.getText().length() == 0) {
            ResponseBaseFile.setError("Por favor, insira sua filial");
        } else if (endereco.getText().length() == 0) {
            endereco.setError("Insira o endereço");
        } else if (bairro.getText().length() == 0) {
            bairro.setError("Por favor, insira bairro");
        } else if (cidade.getText().length() == 0) {
            cidade.setError("Por favor, insira cidade");
        } else if (uf.getText().length() == 0) {
            uf.setError("Por favor, insira uf");
        } else if (cep.getText().length() == 0) {
            cep.setError("Por favor, insira cep");
        } else if (telePhone.getText().length() == 0) {
            telePhone.setError("Por favor, insira telephone");
        } else if (email.getText().length() == 0) {
            email.setError("Por favor, insira email");
        } else if (validateEmail()) {
            //AddToPreferences();
            JsonObjectIdentity(baseFile.getText().toString(), dateOfVerify.getText().toString(), empresa.getText().toString(),
                    cnjp.getText().toString(), ResponseBaseFile.getText().toString(), endereco.getText().toString(),
                    bairro.getText().toString(), cidade.getText().toString(), uf.getText().toString(), cep.getText().toString(),
                    telePhone.getText().toString(), email.getText().toString());
            callback.goToNextStep();
        }
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
        //prefernces.setResearch(object);
        Log.e("TAG", "AddToPreferences: " + prefernces.getResearchOne());
    }

    private void JsonObjectIdentity(String base, String date, String empresa2, String cnjp2, String resoponseBase, String endreco2, String bairo, String city2, String state2, String pincode2, String phone2, String email2) throws JSONException {
        String object = "[{\"label\": \"BASE/FILIAL (SIGLA)\", \"required\":1, \"type\": \"input-name\", \"id\": \"input-177\", \"value\": [\"" + base + "\"]},\n" +
                "{\"label\":\"DATE DE VERIFICAÇÃO\",\"required\":1,\"type\":\"input-date\",\"id\":\"input-359\",\"value\":[\"" + date + "\"]},\n" +
                "{\"label\":\"EMPRESA\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-502\",\"value\":[\"" + empresa2 + "\"]},\n" +
                "{\"label\":\"CNPJ\",\"required\":1,\"type\":\"input-number\",\"id\":\"input-537\",\"value\":[\"" + cnjp2 + "\"]},\n" +
                "{\"label\":\"RESPONSÁVEL PELA BASE/FILIAL\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-512\",\"value\":[\"" + resoponseBase + "\"]},\n" +
                "{\"label\":\"ENDEREÇO\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-901\",\"value\":[\"" + endreco2 + "\"]},\n" +
                "{\"label\":\"BAIRRO\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-442\",\"value\":[\"" + bairo + "\"]},\n" +
                "{\"label\":\"CIDADE\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-33\",\"value\":[\"" + city2 + "\"]},\n" +
                "{\"label\":\"UF\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-237\",\"value\":[\"" + state2 + "\"]},\n" +
                "{\"label\":\"CEP\",\"required\":1,\"type\":\"input-number\",\"id\":\"input-22\",\"value\":[\"" + pincode2 + "\"]},\n" +
                "{\"label\":\"TELEFONE\",\"required\":1,\"type\":\"input-number\",\"id\":\"input-401\",\"value\":[\"" + phone2 + "\"]},\n" +
                "{\"label\":\"E-MAIL\",\"required\":1,\"type\":\"input-email\",\"id\":\"input-335\",\"value\":[\"" + email2 + "\"]}]";

        String array11 = "\"Survey\"" + ":" + object;

        JSONArray array = new JSONArray(object);
        for (int i = 0; i <= array.length(); i++) {
            JSONObject array1 = array.getJSONObject(1);
            String type = array1.getString("type");
//            Log.e(TAG, "dsf: "+type);
//            Log.e(TAG, "dsf: "+array1);
        }
        //Log.e(TAG, "ConvertToJsonArray: "+ array);

        prefernces.setResearchOneDetails(array11);
//        Log.e(TAG, "JsonObjectIdentityString: "+object);
//        Log.e(TAG, "JsonObjectIdentityStringArray: "+array11);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String mail = email.getText().toString().trim();
        Boolean isTrue = false;
        if (!isValidEmail(mail)) {
            email.setError("Invalid Email");
            isTrue = false;
        } else {
            isTrue = true;
        }
        return isTrue;
    }

    //For Validation add BlockingStep in implementation instead of step.
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        //callback.goToNextStep();
        try {
            Validate(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }
}