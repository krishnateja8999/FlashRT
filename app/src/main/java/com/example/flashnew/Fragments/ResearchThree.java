package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.Utils;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ResearchThree extends Fragment implements BlockingStep {

    private static final String TAG = "TAG";
    private EditText responseDesta;
    private TextView photo;
    private ImageView image;
    private DatabaseHelper mDatabaseHelper;
    private ImageTick tick;
    private AppPrefernces prefernces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_three, container, false);

        responseDesta = v.findViewById(R.id.responseDesta);
        photo = v.findViewById(R.id.photo);
        image = v.findViewById(R.id.image);
        mDatabaseHelper = new DatabaseHelper(getContext());
        prefernces = new AppPrefernces(getContext());
        prefernces.setSignaturePath("null");
        Log.e(TAG, "onCreateViewResearchThree: " + prefernces.getSignaturePath());

        tick = new ImageTick();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(tick, new IntentFilter("image_tick"));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureDialog dialog = new SignatureDialog(getContext());
                dialog.show();
            }
        });

        return v;
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
                mDatabaseHelper.CheckTickMarkInResearchLists();
                PutResearchData();
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

    private void Validate(StepperLayout.OnCompleteClickedCallback callback) {

        if (responseDesta.getText().length() == 0) {
            responseDesta.setError("Por favor, insira seu nome.");
        } else if (prefernces.getSignaturePath().equals("null")) {
            Toast.makeText(getContext(), "Forneça sua assinatura.", Toast.LENGTH_LONG).show();
        } else {
            JsonDetailsThree(responseDesta.getText().toString(), prefernces.getSignaturePath());
            FinalDialog("Confirme as respostas", "Deseja finalizar?");
            setXML();
            callback.complete();
        }
//        getXML(55,"sfdsf", "sddfg");
//        FinalDialog("Confirme as respostas", "Deseja finalizar?");
//            callback.complete();

    }

    private void JsonDetailsThree(String name, String photopath) {

        String object3 = "[{\"label\":\"RESPONSÁVEL PELAS INFORMAÇÕES DESTA PESQUISA\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-292\",\"value\":[\"" + name + "\"]},\n" +
                "{\"label\":\"AS INFORMAÇÕES NESTA PESQUISA FORAM PRESTADAS COM VERACIDADE E ASSUMO INTEGRAL RESPONSABILIDADE PELAS MESMAS. DECLARO ESTAR CIENTE DE QUE A FALSIDADE NAS INFORMAÇÕES IMPLICARÁ NAS PENALIDADES CABÍVEIS. COMPROMETO-ME A COMUNICAR Á TICKET QUAISQUER ALTERAÇÕES NESTAS INFORMAÇÕES NUM PRAZO MÁXIMO DE 07 DIAS.\",\"required\":1,\"type\":\"text\",\"id\":\"input-461\",\"value\":[]},\n" +
                "{\"label\":\"ASSINATURA DO GERENTE\\/DIRETOR RESPONSÁVEL\",\"required\":1,\"type\":\"signature\",\"id\":\"input-735\",\"value\":[\"" + photopath + "\"]}]";

        String array11 = "\"Survey\"" + ":" + object3;
        prefernces.setResearchThreeDetails(array11);
        //Log.e(TAG, "JsonDetailsThree: "+ array11);

    }

    private void PutResearchData() {
        RequestQueue queue;
        queue = Volley.newRequestQueue(getContext());
        String url2 = prefernces.getHostUrl() + ApiUtils.GET_LIST1;

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj1 = new JSONObject();
        String body = setXML();

        try {
            jsonObj.put("codHawb", "02406023207");
            jsonObj.put("dataHoraBaixa", "2021-01-18T18:47:22");
            jsonObj.put("nivelBateria", "77");
            jsonObj.put("tipoBaixa", "ENTREGA");
            jsonObj.put("foto", "true");
            jsonObj.put("latitude", "-23.214458905023452");
            jsonObj.put("longitude", "-46.86617505263801");
            jsonObj.put("xmlPesquisa", body);

            jsonObj1.put("imei", prefernces.getIMEI());
            jsonObj1.put("franquia", prefernces.getFranchise());
            jsonObj1.put("sistema", prefernces.getSystem());
            jsonObj1.put("lista", prefernces.getListID());
            jsonObj1.put("entregas", jsonArray);
            jsonArray.put(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "PutJsonRequest: " + jsonObj1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url2 + prefernces.getListID(), jsonObj1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponseResearchThree: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponseResearchThree: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String auth1 = "Basic "
                        + Base64.encodeToString((prefernces.getUserName() + ":" + prefernces.getPaso()).getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth1);
                params.put("x-versao-rt", "3.9.0");
                params.put("x-rastreador", "ricardo");
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

//            @Override
//            public byte[] getBody() {
//                String body = setXML();
//                return body.getBytes();
//            }
        };
        queue.add(request);
    }

    private String setXML() {
        String tab1 = " \"tab\": \"tabStrip\",\n" +
                "                \"label\": \"IDENTIFICAÇÃO\",\n" +
                "                \"type\": \"fieldset\",\n" +
                "                \"id\": \"fieldset-627\",\n" +
                "                \"value\": []";

        String tab2 = "\"tab\": \"tabStrip\",\n" +
                "                \"label\": \"CHECK LIST\",\n" +
                "                \"type\": \"fieldset\",\n" +
                "                \"id\": \"fieldset-797\",\n" +
                "                \"value\": []";

        String tab3 = "\"tab\": \"tabStrip\",\n" +
                "                \"label\": \"RESPONDENTE\",\n" +
                "                \"type\": \"fieldset\",\n" +
                "                \"id\": \"fieldset-59\",\n" +
                "                \"value\": []";
        String result = "<rt_pesquisa>\n  <![CDATA[[{" + prefernces.getResearchOneDetails() + ", \n" + tab1 + "},\n {\n" + prefernces.getResearchTwoDetails() + ", \n" + tab2 + "},\n {\n" + prefernces.getResearchThreeDetails() + ", \n" + tab3 + "}]]]> \n </rt_pesquisa>";
//        Log.e(TAG, "setXML: "+result);
        return result;
    }

    private void SaveImagesOfResearchTwo() {
        String checkListImages = "{" + prefernces.getResearchTwoDetails() + "}";
        try {
            JSONObject object = new JSONObject(checkListImages);
            JSONArray array = object.getJSONArray("Survey");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                String type = object1.getString("type");
                if (type.equals("photo")) {
                    String value = object1.getString("value");
                    if (!value.equals("[\"null\"]")) {
                        String result = value.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\\", "");
                        Log.e(TAG, "SaveImages: " + result);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Validate(callback);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
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

    private class ImageTick extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            image.setImageResource(R.drawable.ic_right);
        }
    }
}