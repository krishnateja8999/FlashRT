package com.example.flashnew.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.flashnew.Modals.TableOneDelivererModal;
import com.example.flashnew.Modals.TableThreeDeliveryModal;
import com.example.flashnew.Modals.TableTwoListModal;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;
import com.example.flashnew.Server.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.BATTERY_SERVICE;
import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;

public class List extends Fragment implements LocationListener {
    private TextView title, imei;
    private Spinner spinner;
    private Button conf, can, camera;
    private LinearLayout linearLayout, retur;
    private RelativeLayout rl1, rl2;
    private Landing_Screen context;
    private LinearLayout linearLayout1, linearLayout2;
    private String[] values2 =
            {"Selecione o motivo da devolução", "Endereçando", "Ausente", "Outras"};
    private CardView listScreenListDownload;
    private AppPrefernces preferences;
    private RequestQueue queue;
    private ProgressBar ListScreenProgressBar;
    private DatabaseHelper mDatabaseHelper;
    private AutoCompleteTextView hawb;
    private Spinner attemptsDropDown;
    private Bitmap photo, OutImage;
    private LocationManager locationManager;
    private InternetConnectionChecker internetChecker;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list, container, false);
        linearLayout = view.findViewById(R.id.buttonEntrega);
        spinner = view.findViewById(R.id.targetOptions);
        rl1 = view.findViewById(R.id.rl);
        rl2 = view.findViewById(R.id.rl1);
        title = view.findViewById(R.id.actionbarTitle);
        imei = view.findViewById(R.id.actionbarImei);
        conf = view.findViewById(R.id.buttonConfirm);
        can = view.findViewById(R.id.buttonCancel);
        retur = view.findViewById(R.id.buttonDevolucao);
        camera = view.findViewById(R.id.buttonPhotoAR);
        camera.setEnabled(false);
        camera.setBackground(getResources().getDrawable(R.drawable.rounded_grey_filled_bg));
        linearLayout1 = view.findViewById(R.id.buttonColeta);
        linearLayout2 = view.findViewById(R.id.buttonPesquisa);
        listScreenListDownload = view.findViewById(R.id.listScreenListDownload);
        preferences = new AppPrefernces(context);
        queue = Volley.newRequestQueue(context);
        ListScreenProgressBar = view.findViewById(R.id.ListScreenProgressBar);
        mDatabaseHelper = new DatabaseHelper(context);
        hawb = view.findViewById(R.id.hawb);
        attemptsDropDown = view.findViewById(R.id.attemptsDropDown);
        internetChecker = new InternetConnectionChecker(context);
        String[] items = new String[]{"1", "2", "3"};
        ArrayAdapter<String> attemptAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        attemptsDropDown.setAdapter(attemptAdapter);
        String[] values1 =
                {"Selecione Relacionamento", "Mãe", "Tio"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);
        preferences.setIMEI("514515854152463");

        hawb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (hawb.getText().toString().length() <= 0) {
                    camera.setBackground(getResources().getDrawable(R.drawable.rounded_grey_filled_bg));
                    camera.setEnabled(false);
                } else {
                    camera.setBackground(getResources().getDrawable(R.drawable.rounded_blue_button));
                    camera.setEnabled(true);
                }
            }
        });

        if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
            title.setVisibility(View.GONE);
        } else {
            title.setText("Lista : " + preferences.getListID());
        }
        imei.setText("IMEI : 9876543210123");


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor data = mDatabaseHelper.getDataFromTableFour();
                if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
                    listDownloadDialog();
                } else if (data.getCount() == 0) {
                    EmptyDataInTableFourDialog();
                } else {
                    title.setText("Entrega");
                    imei.setText("IMEI : 9876543210123");
                    rl2.setVisibility(View.GONE);
                    rl1.setVisibility(View.VISIBLE);
                    hawb.setText("");
                    spinner.setSelection(0);
                    attemptsDropDown.setSelection(0);
                    //preferences.clearListID();
                    preferences.setLowType("ENTREGA");
                    preferences.setPhotoBoolean("false");
                    HawbStringArray();
                    getLocation();
//                checkDB();
                    Log.e(TAG, "checkInternetConnection: " + internetChecker.checkInternetConnection());

                    try {
                        //PutJsonRequest();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor data = mDatabaseHelper.getDataFromTableFour();

                if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
                    listDownloadDialog();
                } else if (data.getCount() == 0) {
                    EmptyDataInTableFourDialog();
                } else {
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values2);
                    adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner.setAdapter(adapter1);
                    title.setText("Devolução");
                    imei.setText("IMEI : 9876543210123");
                    rl2.setVisibility(View.GONE);
                    rl1.setVisibility(View.VISIBLE);
                    preferences.setLowType("DEVOLUCAO");
                    HawbStringArray();
                }
            }
        });

        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDeliveryData();
                mDatabaseHelper.deleteHawbFromTableFour(hawb.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Sucesso");
                //Setting message manually and performing action on button click
                builder.setMessage("Completado com sucesso..")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                title.setText("Lista : " + preferences.getListID());
                                imei.setText("IMEI : 9876543210123");
                                rl2.setVisibility(View.VISIBLE);
                                rl1.setVisibility(View.GONE);
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Atenção");
                alert.show();
            }
        });

        listScreenListDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
                    listDownloadDialog();
                } else {
                    FragmentTransaction fragmentTransaction = context
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new HawbLists());
                    fragmentTransaction.commit();
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.changeFragment1(3);
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.changeFragment1(4);
            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.changeFragment1(2);
            }
        });

        return view;
    }

    private void listDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        edittext.setBackgroundResource(R.drawable.edit_text_border);
        edittext.setPadding(30, 30, 30, 30);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setTitle("Atenção");
        builder.setMessage("Insira uma lista");
        builder.setCancelable(false);
        builder.setView(edittext);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        preferences.setListID(edittext.getText().toString());
                        JsonParseListScreen();

                    }
                });

        builder.setNegativeButton(
                "CANCELAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void HawbStringArray() {
        Cursor data = mDatabaseHelper.getDataFromTableFour();
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            //Toast.makeText(context, "Sem dados", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "HawbStringArray: ");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
            }
            String[] str = Utils.GetStringArray(list);
            Log.e(TAG, "HawbStringArray: " + Arrays.toString(str));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getContext(), android.R.layout.select_dialog_item, str);
            hawb.setThreshold(1);//will start working from first character
            hawb.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        }
    }

    private void storeDeliveryData() {
        try {
            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            mDatabaseHelper.addDataToTableThree(new TableThreeDeliveryModal(hawb.getText().toString(), spinner.getSelectedItem().toString(),
                    attemptsDropDown.getSelectedItem().toString(), formattedDate, batLevel, preferences.getLowType(), preferences.getPhotoBoolean(), preferences.getLatitude(), preferences.getLongitude(), OutImage));

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void JsonParseListScreen() {
        try {
            ListScreenProgressBar.setVisibility(View.VISIBLE);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUtils.GET_LIST + preferences.getListID(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "ListScreen1: " + response);

                    try {
                        String franchiseName = response.getString("franquia");
                        String system = response.getString("sistema");
                        int lists = response.getInt("lista");
                        int deliveryID = response.getInt("idEntregador");
                        String delivererName = response.getString("nomeEntregador");
                        int totalDocuments = response.getInt("quantidadeDocumentos");

                        preferences.setFranchise(franchiseName);
                        preferences.setSystem(system);

                        TableOneDelivererModal tableOneDelivererModal = new TableOneDelivererModal(franchiseName, lists,
                                deliveryID, delivererName, totalDocuments);
                        boolean success1 = mDatabaseHelper.addDataToTableOne(tableOneDelivererModal);
                        Log.e(TAG, "ListScreen3: " + success1);

                        JSONArray array = response.getJSONArray("documentos");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Log.e(TAG, "ListScreen4: " + object);

                            int customerID = object.getInt("idCliente");
                            int contractID = object.getInt("idContrato");
                            String hawbCode = object.getString("codHawb");
                            String numberOrder = object.getString("numeroEncomandaCliente");
                            String recipientName = object.getString("nomeDestinatario");
                            int dna = object.getInt("dna");
                            int attempts = object.getInt("tentativas");
                            String specialPhoto = object.getString("fotoEspecial");
                            int score = object.getInt("score");
                            float latitude = (float) object.getDouble("latitude");
                            float longitude = (float) object.getDouble("longitude");

                            TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                    hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, latitude, longitude);
                            boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                            System.out.println(success);

                            boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode);
                            System.out.println(tableFourHawbCode);

                            FragmentTransaction fragmentTransaction = context
                                    .getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, new HawbLists());
                            fragmentTransaction.commit();
//                            setSuccessDialog();
                        }

                        ListScreenProgressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ListScreenProgressBar.setVisibility(View.GONE);
                    try {
                        Log.e(TAG, "ListScreen2: " + error);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        preferences.clearListID();
                        Log.e(TAG, "ListScreen5: " + e);
                        Toast.makeText(context, getResources().getString(R.string.list_screen1), Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String auth1 = "Basic "
                            + Base64.encodeToString((preferences.getUserName() + ":" + preferences.getPaso()).getBytes(),
                            Base64.NO_WRAP);
                    params.put("Authorization", auth1);
                    params.put("x-versao-rt", "3.8.10");
                    params.put("x-rastreador", "ricardo");
                    return params;
                }
            };
            queue.add(request);

        } catch (Exception e) {

        }
    }

    private void checkDB() {
        if (internetChecker.checkInternetConnection()) {
            Log.e(TAG, "checkDB: " + internetChecker.checkInternetConnection());
//            internetChecker.failureAlert();
//            internetChecker.serverErrorAlert();
        }
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        ArrayList<String> list3 = new ArrayList<String>();
        ArrayList<String> list4 = new ArrayList<String>();

        if (data.getCount() == 0) {
            Toast.makeText(context, "Sem dados", Toast.LENGTH_SHORT).show();
        }
        while (data.moveToNext()) {
            list1.add(data.getString(1));
            list2.add(data.getString(2));
            list3.add(data.getString(3));
            list4.add(data.getString(4));

            Log.e(TAG, "checkDB1: " + list1);
//            Log.e(TAG, "checkDB2: " + list2);
//            Log.e(TAG, "checkDB3: "+list3 );
//            Log.e(TAG, "checkDB4: "+list4 );
            list1.clear();
            list2.clear();
            list3.clear();
            list4.clear();
        }
    }

    private void PutJsonRequest() {

        Cursor data = mDatabaseHelper.getDeliveryData();
        ArrayList<String> codHawb = new ArrayList<String>();
        ArrayList<String> dataHoraBaixa = new ArrayList<String>();
        ArrayList<String> latitude = new ArrayList<String>();
        ArrayList<String> longitude = new ArrayList<String>();
        ArrayList<String> nivelBateria = new ArrayList<String>();
        ArrayList<String> tipoBaixa = new ArrayList<String>();
        ArrayList<String> foto = new ArrayList<String>();

        if (data.getCount() == 0) {
            Log.e(TAG, "PutJsonRequest: No Data");
        }
        while (data.moveToNext()) {
            codHawb.add(data.getString(1));
            dataHoraBaixa.add(data.getString(4));
            latitude.add(data.getString(8));
            longitude.add(data.getString(9));
            nivelBateria.add(data.getString(5));
            tipoBaixa.add(data.getString(6));
            foto.add(data.getString(7));

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonObj1 = new JSONObject();
            try {

                jsonObj.put("codHawb", Utils.ConvertArrayListToString(codHawb));
                jsonObj.put("dataHoraBaixa", Utils.ConvertArrayListToString(dataHoraBaixa));
                jsonObj.put("nivelBateria", Utils.ConvertArrayListToString(nivelBateria));
                jsonObj.put("tipoBaixa", Utils.ConvertArrayListToString(tipoBaixa));
                jsonObj.put("foto", Utils.ConvertArrayListToString(foto));
                jsonObj.put("latitude", Utils.ConvertArrayListToString(latitude));
                jsonObj.put("longitude", Utils.ConvertArrayListToString(longitude));
                jsonArray.put(jsonObj);

                jsonObj1.put("imei", preferences.getIMEI());
                jsonObj1.put("franquia", preferences.getFranchise());
                jsonObj1.put("sistema", preferences.getSystem());
                jsonObj1.put("lista", preferences.getListID());
                jsonObj1.put("entregas", jsonArray);

                Log.e(TAG, "PutJsonRequest: " + jsonObj1);

                final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ApiUtils.GET_LIST + preferences.getListID(), jsonObj1, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "PUTonResponse: " + response);
                        try {
                            String statusMessage = response.getString("statusMessage");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "PUTonResponseError: " + error);

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String auth1 = "Basic "
                                + Base64.encodeToString((preferences.getUserName() + ":" + preferences.getPaso()).getBytes(),
                                Base64.NO_WRAP);
                        params.put("Authorization", auth1);
                        params.put("x-versao-rt", "3.8.10");
                        params.put("x-rastreador", "ricardo");
//                    params.put("Content-Type", "application/json");
                        params.put("Content-Type", "application/json; charset=utf-8");
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };
                request.setTag(TAG);
                queue.add(request);


                codHawb.clear();
                dataHoraBaixa.clear();
                latitude.clear();
                longitude.clear();
                nivelBateria.clear();
                tipoBaixa.clear();
                foto.clear();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void EmptyDataInTableFourDialog() {
//        Cursor data= mDatabaseHelper.getDataFromTableFour();
//        Log.e(TAG, "DataInTableFour: "+data.getCount());
//        if (data.getCount()==0){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(getResources().getString(R.string.Login_screen1));
        builder1.setMessage(getResources().getString(R.string.list_screen5));
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
        //}
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            OutImage = Bitmap.createScaledBitmap(photo, 600, 800, true);
            preferences.setPhotoBoolean("true");
            Toast.makeText(context, getResources().getString(R.string.list_screen4), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }

    // Location

    private void getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Log.e(TAG, "onLocationChanged: " + location.getLatitude() + ", " + location.getLongitude());
        preferences.setLatitude(String.valueOf(location.getLatitude()));
        preferences.setLongitude(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(context, "Habilite o GPS e a Internet", Toast.LENGTH_SHORT).show();
    }

}


//    private void setSuccessDialog(){
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setTitle("Sucesso");
//        builder1.setMessage("Lista baixada com sucesso");
//        builder1.setCancelable(false);
//        builder1.setPositiveButton(
//                "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        Toast.makeText(context, "What is happening", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        //Creating dialog box
//        AlertDialog alert1 = builder1.create();
//        alert1.show();
//    }