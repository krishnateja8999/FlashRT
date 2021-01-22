package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.BatteryManager;
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
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.HelperClasses.UploadImages;
import com.example.flashnew.Modals.SaveResearchDetailsModal;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;
import com.example.flashnew.Server.Utils;
import com.google.cloud.storage.Storage;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.BATTERY_SERVICE;
import static com.example.flashnew.Server.Utils.VERSION;

public class ResearchThree extends Fragment implements BlockingStep {

    private TextView photo;
    private ImageTick tick;
    private ImageView image;
    private String timeStamp;
    private EditText responseDesta;
    private Landing_Screen context;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;
    private static final String TAG = "TAG";
    private InternetConnectionChecker checker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_three, container, false);

        responseDesta = v.findViewById(R.id.responseDesta);
        photo = v.findViewById(R.id.photo);
        image = v.findViewById(R.id.image);
        mDatabaseHelper = new DatabaseHelper(context);
        prefernces = new AppPrefernces(context);
        checker = new InternetConnectionChecker(context);
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        prefernces.setSignaturePath("null");
        Log.e(TAG, "onCreateViewResearchThree: " + prefernces.getSignaturePath());
        tick = new ImageTick();
        LocalBroadcastManager.getInstance(context).registerReceiver(tick, new IntentFilter("image_tick"));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureDialog dialog = new SignatureDialog(context);
                dialog.show();
            }
        });

        return v;

    }

    private void FinalDialog(String successDialog, String successDesc) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(successDialog);
        builder1.setMessage(successDesc);
        builder1.setCancelable(false);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FinalDialog2("Sucesso", "Pesquisa finalizada com successo");
                mDatabaseHelper.AddResearchImages("/null", "sdf");
                StoreResearchDetails();
                SaveImagesOfResearchTwo();
                //String imageName = customerID + "_" + contractCode + "_img_ft_especial_" + researchHawb + "_img_rt_" + clientName + "_" + timeStamp + ".png";
                String imageName = prefernces.getCustomerCode() + "_" + prefernces.getContractCode() + "_img_ft_especial_" + prefernces.getHawbCodeRes() + "_img_rt_" + prefernces.getClientName() + "_" + timeStamp + ".png";
                boolean success = mDatabaseHelper.AddResearchImages(prefernces.getSignaturePath(), imageName);
                System.out.println("ResearchImagesSavedSuccessfully" + success);
                mDatabaseHelper.CheckTickMarkInResearchLists();
                if (checker.checkInternetConnection()) {
                    PutResearchData();
                }
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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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
        context.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
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
    }

    private void StoreResearchDetails() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String body = setXML();

        SaveResearchDetailsModal detailsModal = new SaveResearchDetailsModal(prefernces.getHawbCodeRes(), formattedDate, batLevel, prefernces.getLatitude(),
                prefernces.getLongitude(), body, prefernces.getResearchListCode());
        boolean success = mDatabaseHelper.AddResearchDetails(detailsModal);
        System.out.println(success);
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
        Cursor data = mDatabaseHelper.GetResearchDetails(); //table11
        RequestQueue queue;
        queue = Volley.newRequestQueue(context);
        String url2 = prefernces.getHostUrl() + ApiUtils.GET_LIST1;

        ArrayList<String> codHawb = new ArrayList<String>();
        ArrayList<String> dataHoraBaixa = new ArrayList<String>();
        ArrayList<String> nivelBateria = new ArrayList<String>();
        ArrayList<String> latitude = new ArrayList<String>();
        ArrayList<String> longitude = new ArrayList<String>();
        ArrayList<String> body1 = new ArrayList<String>();
        ArrayList<String> listCode = new ArrayList<String>();

        if (data.getCount() == 0) {
            Log.e(TAG, "PutJsonRequest: No Data");
        } else {
            while (data.moveToNext()) {
                codHawb.add(data.getString(1));
                dataHoraBaixa.add(data.getString(2));
                nivelBateria.add(data.getString(3));
                latitude.add(data.getString(4));
                longitude.add(data.getString(5));
                body1.add(data.getString(6));
                listCode.add(data.getString(7));

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObj = new JSONObject();
                JSONObject jsonObj1 = new JSONObject();
                try {
                    jsonObj.put("codHawb", Utils.ConvertArrayListToString(codHawb));
                    jsonObj.put("dataHoraBaixa", Utils.ConvertArrayListToString(dataHoraBaixa));
                    jsonObj.put("nivelBateria", Utils.ConvertArrayListToString(nivelBateria));
                    jsonObj.put("latitude", Utils.ConvertArrayListToString(latitude));
                    jsonObj.put("longitude", Utils.ConvertArrayListToString(longitude));
                    jsonObj.put("xmlPesquisa", Utils.ConvertArrayListToString(body1));

                    jsonObj1.put("imei", prefernces.getIMEI());
                    jsonObj1.put("franquia", prefernces.getFranchise());
                    jsonObj1.put("sistema", prefernces.getSystem());
                    jsonObj1.put("lista", Utils.ConvertArrayListToString(listCode));
                    jsonObj1.put("entregas", jsonArray);
                    jsonArray.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "PutJsonRequest: " + jsonObj1);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url2 + Utils.ConvertArrayListToString(listCode), jsonObj1, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DeleteResearchDataUponSendingOrSync();
                        Intent intent22 = new Intent("research_list_update");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent22);
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
                        params.put("x-versao-rt", VERSION);
                        params.put("x-rastreador", "ricardo");
                        params.put("Content-Type", "application/json; charset=utf-8");
                        return params;
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };
                queue.add(request);

                codHawb.clear();
                dataHoraBaixa.clear();
                nivelBateria.clear();
                latitude.clear();
                longitude.clear();
                body1.clear();
                listCode.clear();
            }
            SendResearchImages();
        }
    }

    private void SendResearchImages() {
        Cursor data = mDatabaseHelper.GetResearchImages(); //table12
        ArrayList<String> ImagePath = new ArrayList<String>();
        ArrayList<String> ImageName = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(TAG, "SendResearchImages: No Data");
        } else {
            while (data.moveToNext()) {
                ImagePath.add(data.getString(1));
                ImageName.add(data.getString(2));
                try {
                    Storage storage = UploadImages.setCredentials(context.getAssets().open("key.json"));

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UploadImages.transmitImageFile(storage, Utils.ConvertArrayListToString(ImagePath), Utils.ConvertArrayListToString(ImageName));
                        }
                    });
                    thread.start();
                } catch (Exception e) {
                    Log.e(TAG, "SendResearchImagesException: " + e.getMessage());
                }
                Log.e(TAG, "SendResearchImages: " + ImagePath + " Name " + ImageName);
                ImagePath.clear();
                ImageName.clear();
            }
            mDatabaseHelper.DeleteResearchImages();
        }
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
        String result = "<![CDATA[[{" + prefernces.getResearchOneDetails() + ", \n" + tab1 + "},\n {\n" + prefernces.getResearchTwoDetails() + ", \n" + tab2 + "},\n {\n" + prefernces.getResearchThreeDetails() + ", \n" + tab3 + "}]]]>";
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
                        String result = value.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\\", "").replaceAll("\\\"", "");

                        String imageName = prefernces.getCustomerCode() + "_" + prefernces.getContractCode() + "_img_ft_especial_" + prefernces.getHawbCodeRes() + "_img_rt_" + prefernces.getClientName() + "_" + timeStamp + ".png";
                        boolean success = mDatabaseHelper.AddResearchImages(result, imageName);
                        System.out.println("ResearchImagesSavedSuccessfully" + success);
                        Log.e(TAG, "SaveImages: " + result);
                        //        <customer code>_<contract code>_<image type>*_<hawb>_img_rt_<customer number>**_<AAAAMMDDHHMMSS>.png
                        //        3586_4801_img_ar_02406021825_img_rt_OMtest001_20200111171205.png
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void DeleteResearchDataUponSendingOrSync() {
        Cursor data = mDatabaseHelper.GetResearchDetails(); //table11
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(TAG, "DeleteDataUponSyncOrUpload: No Data");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                mDatabaseHelper.DeleteDataFromResearchList(Utils.ConvertArrayListToString(list));
                list.clear();
            }
            mDatabaseHelper.DeleteFromResearchDetails();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }

}