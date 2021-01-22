package com.example.flashnew.Fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.Modals.TableSevenNotCollectedModal;
import com.example.flashnew.Modals.TableSixCollectModal;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;
import com.example.flashnew.Server.Utils;

import net.skoumal.fragmentback.BackFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BATTERY_SERVICE;
import static com.android.volley.VolleyLog.TAG;
import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;

public class CollectDetails extends Fragment {

    private String strtext;
    private String[] outros;
    private String[] ausente;
    private String[] enderec;
    private RequestQueue queue;
    private EditText nameColeta;
    private Button buttonPhotoAR;
    private EditText coletaIdenti;
    private String[] nao_visitado;
    private File photoFile = null;
    private String currentPhotoPath;
    private Landing_Screen mContext;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;
    private Spinner spinner, spinner2, spinner3;
    private InternetConnectionChecker internetChecker;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (Landing_Screen) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collect_details, container, false);

        queue = Volley.newRequestQueue(mContext);
        prefernces = new AppPrefernces(mContext);
        mDatabaseHelper = new DatabaseHelper(mContext);
        spinner = view.findViewById(R.id.targetOptions);
        nameColeta = view.findViewById(R.id.nameColeta);
        spinner2 = view.findViewById(R.id.targetOptions2);
        spinner3 = view.findViewById(R.id.targetOptions3);
        coletaIdenti = view.findViewById(R.id.coletaIdenti);
        buttonPhotoAR = view.findViewById(R.id.buttonPhotoAR);
        TextView imei = view.findViewById(R.id.actionbarImei);
        TextView title = view.findViewById(R.id.actionbarTitle);
        internetChecker = new InternetConnectionChecker(mContext);
        enderec = getResources().getStringArray(R.array.motivo_dev);
        outros = getResources().getStringArray(R.array.motivo_outros);
        ausente = getResources().getStringArray(R.array.motivo_ausente);
        String[] values2 = getResources().getStringArray(R.array.motivo_grupo);
        Button buttonCancelCollect = view.findViewById(R.id.buttonCancelCollect);
        nao_visitado = getResources().getStringArray(R.array.motivo_nao_visitado);
        Button buttonConfirmCollect = view.findViewById(R.id.buttonConfirmCollect);

        prefernces.setNotCollectedImage(" ");
        strtext = getArguments().getString("CID");
        imei.setText("Coleta: " + strtext);
        title.setVisibility(View.GONE);

        String[] values1 = {"-- Selecione Coletar --", "COLETADO", "NAO_COLETADO"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinner2.setVisibility(View.GONE);
                    nameColeta.setVisibility(View.VISIBLE);
                    coletaIdenti.setVisibility(View.VISIBLE);
                    buttonPhotoAR.setVisibility(View.GONE);
                    spinner3.setVisibility(View.GONE);
                    spinner2.setSelection(0);
                } else if (position == 1) {
                    spinner2.setVisibility(View.GONE);
                    nameColeta.setVisibility(View.VISIBLE);
                    coletaIdenti.setVisibility(View.VISIBLE);
                    buttonPhotoAR.setVisibility(View.GONE);
                    spinner3.setVisibility(View.GONE);
                    spinner2.setSelection(0);
                } else if (position == 2) {
                    spinner2.setVisibility(View.VISIBLE);
                    nameColeta.setVisibility(View.GONE);
                    coletaIdenti.setVisibility(View.GONE);
                    buttonPhotoAR.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinner3.setVisibility(View.GONE);
                } else if (position == 1) {
                    spinner3.setVisibility(View.VISIBLE);
                    spinner3.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, enderec);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner3.setAdapter(adapter2);
                } else if (position == 2) {
                    spinner3.setVisibility(View.VISIBLE);
                    spinner3.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ausente);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner3.setAdapter(adapter2);
                } else if (position == 3) {
                    spinner3.setVisibility(View.VISIBLE);
                    spinner3.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nao_visitado);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner3.setAdapter(adapter2);
                } else if (position == 4) {
                    spinner3.setVisibility(View.VISIBLE);
                    spinner3.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, outros);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner3.setAdapter(adapter2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonConfirmCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmCollected();
            }
        });

        buttonCancelCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new Collect();
                FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content, fr);
                ft.commit();
            }
        });

        buttonPhotoAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return view;
    }

    private void SuccessDialog() {
        StoreColetaDetails();
        SuccessDialog1("Sucesso", "Colleta com sucesso");
    }

    private void NotCollectedSuccessDialog() {
        StoreNotColetaDetails();
        SuccessDialog1("Sucesso", "Colleta com sucesso");
    }

    private void StoreColetaDetails() {
        BatteryManager bm = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        Log.e(TAG, "StoreColetaDetails: " + batLevel);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String spinnerValue = spinner.getSelectedItem().toString();

        TableSixCollectModal sixCollectModal = new TableSixCollectModal(strtext, nameColeta.getText().toString(), coletaIdenti.getText().toString(),
                formattedDate, spinnerValue, prefernces.getLatitude(), prefernces.getLongitude(), batLevel);
        boolean success = mDatabaseHelper.AddDataToTableSix(sixCollectModal);
        System.out.println(success);
        Fragment fr = new Collect();
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fr);
        ft.commit();
    }

    private void StoreNotColetaDetails() {
        BatteryManager bm = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String spinnerValue = spinner.getSelectedItem().toString();

        TableSevenNotCollectedModal sevenCollectModal = new TableSevenNotCollectedModal(strtext,
                formattedDate, spinnerValue, prefernces.getLatitude(), prefernces.getLongitude(), batLevel, prefernces.getNotCollectedImage());
        boolean success = mDatabaseHelper.AddDataToTableSeven(sevenCollectModal);
        Log.e(TAG, "StoreNotColetaDetails: " + prefernces.getNotCollectedImage());
        System.out.println(success);
        Fragment fr = new Collect();
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fr);
        ft.commit();
    }

    private void ConfirmCollected() {
        if (spinner.getSelectedItem().toString().equals("-- Selecione Coletar --")) {
            Toast.makeText(getContext(), "Selecione a Coletar", Toast.LENGTH_SHORT).show();
        } else if (spinner.getSelectedItem().toString().equals("COLETADO")) {
            if (nameColeta.getText().toString().length() == 0) {
                Toast.makeText(getContext(), "Por favor insira um nome", Toast.LENGTH_SHORT).show();
            } else if (coletaIdenti.getText().toString().length() == 0) {
                Toast.makeText(getContext(), "Por favor insira um rg", Toast.LENGTH_SHORT).show();
            } else {
                mDatabaseHelper.CheckTickMarkInTableFive(strtext);
                SuccessDialog();
                boolean AddCollectForCount = mDatabaseHelper.AddCollectsForCount("COLECTA");
                System.out.println(AddCollectForCount);
                if (internetChecker.checkInternetConnection()) {
                    PostCollectData();
                }
            }
        } else if (spinner.getSelectedItem().toString().equals("NAO_COLETADO")) {
            if (spinner2.getSelectedItem().toString().equals("-- Selecionar grupo --")) {
                Toast.makeText(mContext, "Selecione a grupo", Toast.LENGTH_SHORT).show();
            } else if (prefernces.getNotCollectedImage().equals(" ") || prefernces.getNotCollectedImage().equals("")) {
                Toast.makeText(mContext, "Por favor carregue uma foto", Toast.LENGTH_SHORT).show();
            } else {
                mDatabaseHelper.CheckTickMarkInTableFive(strtext);
                NotCollectedSuccessDialog();
                boolean AddCollectForCount = mDatabaseHelper.AddCollectsForCount("COLECTA");
                System.out.println(AddCollectForCount);
                if (internetChecker.checkInternetConnection()) {
                    PostNotCollectData();
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String fileName = "temp";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        String a = image.getName();

        Log.d("TAG", "createImageFileCollect: " + currentPhotoPath);
        Log.d("TAG", "createImageFileCollect: " + a);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            photo = (Bitmap) data.getExtras().get("data");
//            OutImage = Bitmap.createScaledBitmap(photo, 600, 800, true);
            prefernces.setNotCollectedImage(currentPhotoPath);
            buttonPhotoAR.setText("Tirar foto do local" + "   ✔");
            Toast.makeText(mContext, getResources().getString(R.string.list_screen4), Toast.LENGTH_SHORT).show();
        }
    }

    private void SuccessDialog1(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle(title);
        builder1.setMessage(message);
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

    private void DeleteDataUponSyncOrUpload() {
        Cursor data = mDatabaseHelper.GetDataFromTableSix(); //table6
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(ContentValues.TAG, "DeleteDataUponSyncOrUpload: No Data");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                mDatabaseHelper.DeleteFromTableFiveUponUpload(Utils.ConvertArrayListToString(list));//Table5
                list.clear();
            }
        }
    }

    private void DeleteDataUponSyncOrUpload1() {
        Cursor data = mDatabaseHelper.GetDataFromTableSeven(); //table7
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(ContentValues.TAG, "DeleteDataUponSyncOrUpload: No Data");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                mDatabaseHelper.DeleteFromTableFiveUponUpload(Utils.ConvertArrayListToString(list));//Table5
                list.clear();
            }
        }
    }

    private void PostCollectData() {
        Cursor data = mDatabaseHelper.GetDataFromTableSix(); //table6
        ArrayList<String> coletaID = new ArrayList<String>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> identification = new ArrayList<String>();
        ArrayList<String> dateTime = new ArrayList<String>();
        ArrayList<String> typeProcess = new ArrayList<String>();
        ArrayList<String> latitude = new ArrayList<String>();
        ArrayList<String> longitude = new ArrayList<String>();
        ArrayList<String> batteryLevel = new ArrayList<String>();

        if (data.getCount() == 0) {
            Log.e(ContentValues.TAG, "PostCollect: No Data");
        } else {
            while (data.moveToNext()) {
                coletaID.add(data.getString(1));
                name.add(data.getString(2));
                identification.add(data.getString(3));
                dateTime.add(data.getString(4));
                typeProcess.add(data.getString(5));
                latitude.add(data.getString(6));
                longitude.add(data.getString(7));
                batteryLevel.add(data.getString(8));

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObj = new JSONObject();
                JSONObject jsonObj1 = new JSONObject();

                try {
                    jsonObj.put("codColeta", Utils.ConvertArrayListToString(coletaID));
                    jsonObj.put("recebedor", Utils.ConvertArrayListToString(name));
                    jsonObj.put("rg", Utils.ConvertArrayListToString(identification));
                    jsonObj.put("dataProcesso", Utils.ConvertArrayListToString(dateTime));
                    jsonObj.put("tipoProcesso", Utils.ConvertArrayListToString(typeProcess));
                    jsonObj.put("longitude", Utils.ConvertArrayListToString(longitude));
                    jsonObj.put("latitude", Utils.ConvertArrayListToString(latitude));
                    jsonObj.put("nivelBateria", Utils.ConvertArrayListToString(batteryLevel));
                    jsonArray.put(jsonObj);

                    jsonObj1.put("usuario", prefernces.getUserName());
                    jsonObj1.put("password", prefernces.getPaso());
                    jsonObj1.put("imei", prefernces.getIMEI());
                    jsonObj1.put("coleta", jsonArray);

                    Log.e(ContentValues.TAG, "PostCollectData: " + jsonObj1);

                    String url1 = ApiUtils.POST_COLETA;
                    String url2 = prefernces.getHostUrl() + ApiUtils.POST_COLETA1;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url2, jsonObj1, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(ContentValues.TAG, "JsonPOSTResponse: " + response);
                            //PostResponseDeleteData();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(ContentValues.TAG, "JsonPOSTErrorResponse: " + error);
                            Toast.makeText(mContext, "Erro, tente mais tarde..." + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(request);

                    coletaID.clear();
                    name.clear();
                    identification.clear();
                    dateTime.clear();
                    typeProcess.clear();
                    latitude.clear();
                    longitude.clear();
                    batteryLevel.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            DeleteDataUponSyncOrUpload();
            mDatabaseHelper.DeleteFromTableSixUponSync();
        }
    }

    private void PostResponseDeleteData() {
        Log.e(TAG, "PostResponseDeleteData: ");
        DeleteDataUponSyncOrUpload();
        mDatabaseHelper.DeleteFromTableSixUponSync();
    }

    private void PostNotCollectData() {
        Cursor data = mDatabaseHelper.GetDataFromTableSeven(); //table7
        ArrayList<String> coletaID = new ArrayList<String>();
        ArrayList<String> dateTime = new ArrayList<String>();
        ArrayList<String> typeProcess = new ArrayList<String>();
        ArrayList<String> latitude = new ArrayList<String>();
        ArrayList<String> longitude = new ArrayList<String>();
        ArrayList<String> batteryLevel = new ArrayList<String>();
        ArrayList<String> notCollectImage = new ArrayList<String>();

        if (data.getCount() == 0) {
            Log.e(ContentValues.TAG, "PostNotCollect: No Data");
        } else {
            while (data.moveToNext()) {
                coletaID.add(data.getString(1));
                dateTime.add(data.getString(2));
                typeProcess.add(data.getString(3));
                latitude.add(data.getString(4));
                longitude.add(data.getString(5));
                batteryLevel.add(data.getString(6));
                notCollectImage.add(data.getString(7));

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObj = new JSONObject();
                JSONObject jsonObj1 = new JSONObject();

                try {
                    jsonObj.put("codColeta", Utils.ConvertArrayListToString(coletaID));
                    jsonObj.put("dataProcesso", Utils.ConvertArrayListToString(dateTime));
                    jsonObj.put("tipoProcesso", Utils.ConvertArrayListToString(typeProcess));
                    jsonObj.put("longitude", Utils.ConvertArrayListToString(longitude));
                    jsonObj.put("latitude", Utils.ConvertArrayListToString(latitude));
                    jsonObj.put("nivelBateria", Utils.ConvertArrayListToString(batteryLevel));
                    jsonArray.put(jsonObj);

                    jsonObj1.put("usuario", prefernces.getUserName());
                    jsonObj1.put("password", prefernces.getPaso());
                    jsonObj1.put("imei", prefernces.getIMEI());
                    jsonObj1.put("coleta", jsonArray);

                    Log.e(ContentValues.TAG, "PostNotCollectData: " + jsonObj1);

                    String url1 = ApiUtils.POST_COLETA;
                    String url2 = prefernces.getHostUrl() + ApiUtils.POST_COLETA1;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url2, jsonObj1, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(ContentValues.TAG, "JsonPOSTResponse: " + response);
                            //PostNotCollectResponseDeleteData();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(ContentValues.TAG, "JsonPOSTErrorResponse: " + error);
                            Toast.makeText(mContext, "Erro, tente mais tarde.." + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(request);

                    DeletePhotoPath(Utils.ConvertArrayListToString(notCollectImage));
                    coletaID.clear();
                    dateTime.clear();
                    typeProcess.clear();
                    latitude.clear();
                    longitude.clear();
                    batteryLevel.clear();
                    notCollectImage.clear();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            DeleteDataUponSyncOrUpload1();
            mDatabaseHelper.DeleteFromTableSevenUponSync();//Table7
        }
    }

    private void PostNotCollectResponseDeleteData() {
        DeleteDataUponSyncOrUpload1();
        mDatabaseHelper.DeleteFromTableSevenUponSync();//Table7
    }

    private void DeletePhotoPath(String path) {
        File delete = new File(path);
        if (delete.exists()) {
            if (delete.delete()) {
                System.out.println("file Deleted :" + path);
            } else {
                System.out.println("file not Deleted :" + path);
            }
        }
    }
}