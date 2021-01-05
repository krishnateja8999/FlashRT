package com.example.flashnew.Fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.Adapters.AutoSuggestAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.LoginActivity;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.BATTERY_SERVICE;
import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;

public class List extends Fragment implements LocationListener {
    private TextView title, imei;
    private Spinner spinner, spinner2;
    private Button conf, can, camera;
    private LinearLayout linearLayout, retur;
    private RelativeLayout rl1, rl2;
    private Landing_Screen context;
    private LinearLayout linearLayout1, linearLayout2;
    private String[] values2, enderec, ausente, nao_visitado, outros;
    private CardView listScreenListDownload;
    private AppPrefernces preferences;
    private RequestQueue queue;
    private ProgressBar ListScreenProgressBar;
    private DatabaseHelper mDatabaseHelper;
    private AutoCompleteTextView hawb;
    private Spinner attemptsDropDown;
    private Bitmap OutImage;
    private LocationManager locationManager;
    private InternetConnectionChecker internetChecker;
    private ListCodeUpdater listCodeUpdater;
    private ListScreenUpdater listScreenUpdater;
    private File photoFile = null;
    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list, container, false);
        linearLayout = view.findViewById(R.id.buttonEntrega);
        spinner = view.findViewById(R.id.targetOptions);
        spinner2 = view.findViewById(R.id.spinner2);
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
        String[] items = new String[]{"1"};
        //String[] items = new String[]{"1", "2", "3"};
        values2 = getResources().getStringArray(R.array.motivo_grupo);
        ArrayAdapter<String> attemptAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        attemptsDropDown.setAdapter(attemptAdapter);
        String[] tab_names = getResources().getStringArray(R.array.grau_relacionamento);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tab_names);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);
        enderec = getResources().getStringArray(R.array.motivo_dev);
        ausente = getResources().getStringArray(R.array.motivo_ausente);
        nao_visitado = getResources().getStringArray(R.array.motivo_nao_visitado);
        outros = getResources().getStringArray(R.array.motivo_outros);
        getLocation();
        listCodeUpdater = new ListCodeUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listCodeUpdater, new IntentFilter("list_code_status"));
        listScreenUpdater = new ListScreenUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listScreenUpdater, new IntentFilter("list_screen"));
        Cursor data = mDatabaseHelper.getDeliveryData(); //table3
        Cursor data1 = mDatabaseHelper.getDataFromTableFour();
        Cursor data2 = mDatabaseHelper.getData();//Table 2
        if (data.getCount() == 0 && data1.getCount() == 0) {
            mDatabaseHelper.DeleteDataFromTableTwo();
            preferences.clearListID();
        }
        if (data2.getCount() == 0) {
            mDatabaseHelper.DeleteTableEight();
        }
        getLocation();
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
            title.setText("Sem Listas");

        } else {
            title.setText("Lista : " + preferences.getListID());
        }
        imei.setText("IMEI : " + preferences.getIMEI());

        listScreenListDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
                    listDownloadDialog();
                } else {
                    FragmentTransaction fragmentTransaction = context
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new HawbLists());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delivery();
            }
        });

        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Returns();
            }
        });

        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = mDatabaseHelper.CheckHawbCode(hawb.getText().toString());
                if (preferences.getLowType().equals("ENTREGA")) {
                    if (hawb.getText().toString().length() == 0) {
                        Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
                        hawb.requestFocus();
                        //hawb.setError("Selecione um Hawb");
                    } else if (!check) {
                        Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
                    } else if (spinner.getSelectedItem().toString().equals("-- Selecionar parentesco --")) {
                        Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
                    } else if (OutImage == null) {
                        Toast.makeText(context, "Por favor carregue uma foto", Toast.LENGTH_SHORT).show();
                    } else {
                        storeDeliveryData();
                        if (internetChecker.checkInternetConnection()) {
                            PutJsonRequest();
                        }
                        mDatabaseHelper.deleteHawbFromTableFour(hawb.getText().toString());
                        mDatabaseHelper.ValidateDataWithSecondTable(hawb.getText().toString());
                        boolean AddDeliveryType = mDatabaseHelper.AddDeliveryType("ENTREGA");
                        System.out.println(AddDeliveryType);
                        ConfirmSuccessDialog("entregue");
                    }
                } else {
                    if (hawb.getText().toString().length() == 0) {
                        Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
                        hawb.requestFocus();
                        //hawb.setError("Selecione um Hawb");
                    } else if (!check) {
                        Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
                    } else if (spinner.getSelectedItem().toString().equals("-- Selecionar parentesco --")) {
                        Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
                    } else if (OutImage == null) {
                        Toast.makeText(context, "Por favor carregue uma foto", Toast.LENGTH_SHORT).show();
                    } else {
                        storeDeliveryData();
                        if (internetChecker.checkInternetConnection()) {
                            PutJsonRequest();
                        }
                        mDatabaseHelper.deleteHawbFromTableFour(hawb.getText().toString());
                        mDatabaseHelper.ValidateDataWithSecondTable(hawb.getText().toString());
                        boolean AddDeliveryType = mDatabaseHelper.AddDeliveryType("DEVOLUCAO");
                        System.out.println(AddDeliveryType);
                        ConfirmSuccessDialog("devolvida");
                    }
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                //dispatchTakePictureIntent();
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

    private void Delivery() {
        Cursor data = mDatabaseHelper.getDataFromTableFour();
        Cursor data1 = mDatabaseHelper.getDeliveryData();
        if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
            listDownloadDialogForDelivery();
        } else if (data.getCount() == 0 && data1.getCount() != 0) {
            EmptyDataInTableFourDialog("entregar");
        } else {
            title.setText("Entrega");
            imei.setText("IMEI : " + preferences.getIMEI());
            rl2.setVisibility(View.GONE);
            rl1.setVisibility(View.VISIBLE);
            hawb.setText("");
            spinner.setSelection(0);
            spinner2.setVisibility(View.GONE);
            attemptsDropDown.setSelection(0);
            conf.setText("Entrega");
            camera.setText("Selecione a foto");
            OutImage = null;
            String[] tab_names = getResources().getStringArray(R.array.grau_relacionamento);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tab_names);
            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter1);
            //preferences.clearListID();
            preferences.setLowType("ENTREGA");
            preferences.setPhotoBoolean("false");
            HawbStringArray();
            getLocation();
        }
    }

    private void Returns() {
        Cursor data = mDatabaseHelper.getDataFromTableFour();
        if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
            listDownloadDialogForReturn();
        } else if (data.getCount() == 0) {
            EmptyDataInTableFourDialog("devolvida");
        } else {
            spinner.setVisibility(View.GONE);
            spinner2.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values2);
            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner2.setAdapter(adapter1);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        spinner.setVisibility(View.GONE);
                    } else if (position == 1) {
                        spinner.setVisibility(View.VISIBLE);
                        spinner.performClick();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, enderec);
                        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter2);
                    } else if (position == 2) {
                        spinner.setVisibility(View.VISIBLE);
                        spinner.performClick();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ausente);
                        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter2);
                    } else if (position == 3) {
                        spinner.setVisibility(View.VISIBLE);
                        spinner.performClick();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nao_visitado);
                        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter2);
                    } else if (position == 4) {
                        spinner.setVisibility(View.VISIBLE);
                        spinner.performClick();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, outros);
                        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter2);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            title.setText("Devolução");
            imei.setText("IMEI : " + preferences.getIMEI());
            hawb.setText("");
            spinner.setSelection(0);
            attemptsDropDown.setSelection(0);
            preferences.setPhotoBoolean("false");
            camera.setText("Selecione a foto");
            conf.setText("Devolver");
            OutImage = null;
            rl2.setVisibility(View.GONE);
            rl1.setVisibility(View.VISIBLE);
            preferences.setLowType("DEVOLUCAO");
            HawbStringArray();
        }
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

    private void listDownloadDialogForDelivery() {
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
                        JsonParseListScreen2();
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

    private void listDownloadDialogForReturn() {
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
                        JsonParseListScreen3();
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
            Log.e(TAG, "HawbStringArray: No Data");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                list.add(data.getString(2));
            }
            String[] str = Utils.GetStringArray(list);
            Log.e(TAG, "HawbStringArray: " + Arrays.toString(str));
            AutoSuggestAdapter adapter1 = new AutoSuggestAdapter(context, android.R.layout.simple_list_item_1, list);
            hawb.setAdapter(adapter1);
            hawb.setThreshold(1);
        }
    }

    private void storeDeliveryData() {
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String spinnerValue = spinner.getSelectedItem().toString();
        int spinnerID = Integer.parseInt(spinnerValue.replaceAll("[^0-9]", ""));

        String clientNumber = mDatabaseHelper.CheckClientNumber(hawb.getText().toString());
        boolean HawbChecker = mDatabaseHelper.CheckHawbCode1(clientNumber);
        if (HawbChecker) {
            mDatabaseHelper.addDataToTableThree(new TableThreeDeliveryModal(clientNumber, spinnerID,
                    attemptsDropDown.getSelectedItem().toString(), formattedDate, batLevel, preferences.getLowType(), preferences.getPhotoBoolean(), preferences.getLatitude(), preferences.getLongitude(), OutImage));
        } else {
            mDatabaseHelper.addDataToTableThree(new TableThreeDeliveryModal(hawb.getText().toString(), spinnerID,
                    attemptsDropDown.getSelectedItem().toString(), formattedDate, batLevel, preferences.getLowType(), preferences.getPhotoBoolean(), preferences.getLatitude(), preferences.getLongitude(), OutImage));
        }
    }

    private void EmptyDataInTableFourDialog(String type) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(getResources().getString(R.string.Login_screen1));
        builder1.setMessage("Nenhuma lista para " + type + ", sincronize para atualizar as listas");
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
        Cursor data = mDatabaseHelper.getDeliveryData();//Table3
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(TAG, "DeleteDataUponSyncOrUpload: No Data");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                mDatabaseHelper.DeleteDataUponUpload(Utils.ConvertArrayListToString(list));//Table2
                list.clear();
            }
        }

    }

    private void JsonParseListScreen() {
        ListScreenProgressBar.setVisibility(View.VISIBLE);
        String url1 = ApiUtils.GET_LIST;
        String url2 = preferences.getHostUrl() + ApiUtils.GET_LIST1;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2 + preferences.getListID(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "JsonParseListScreen: " + response);
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
                    System.out.println(success1);

                    JSONArray array = response.getJSONArray("documentos");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Log.e(TAG, "JsonParseListScreenArray: " + object);

                        int customerID = object.getInt("idCliente");
                        int contractID = object.getInt("idContrato");
                        String hawbCode = object.getString("codHawb");
                        String numberOrder = object.getString("numeroEncomandaCliente");
                        String recipientName = object.getString("nomeDestinatario");
                        int dna = object.getInt("dna");
                        int attempts = object.getInt("tentativas");
                        String specialPhoto = object.getString("idCCusto");
                        int score = object.getInt("score");
                        String clientNumber = object.getString("numeroEncomandaCliente");
//                        float latitude = (float) object.getDouble("latitude");
//                        float longitude = (float) object.getDouble("longitude");

                        TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber);
                        boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                        System.out.println(success);

                        boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
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
                Toast.makeText(context, getResources().getString(R.string.list_screen1), Toast.LENGTH_LONG).show();
                preferences.clearListID();
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
    }

    private void JsonParseListScreen2() {
        ListScreenProgressBar.setVisibility(View.VISIBLE);
        String url1 = ApiUtils.GET_LIST;
        String url2 = preferences.getHostUrl() + ApiUtils.GET_LIST1;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2 + preferences.getListID(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "JsonParseListScreen: " + response);

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
                    System.out.println(success1);

                    JSONArray array = response.getJSONArray("documentos");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Log.e(TAG, "JsonParseListScreenArray: " + object);

                        int customerID = object.getInt("idCliente");
                        int contractID = object.getInt("idContrato");
                        String hawbCode = object.getString("codHawb");
                        String numberOrder = object.getString("numeroEncomandaCliente");
                        String recipientName = object.getString("nomeDestinatario");
                        int dna = object.getInt("dna");
                        int attempts = object.getInt("tentativas");
                        String specialPhoto = object.getString("idCCusto");
                        int score = object.getInt("score");
                        String clientNumber = object.getString("numeroEncomandaCliente");

                        TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber);
                        boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                        System.out.println(success);

                        boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                        System.out.println(tableFourHawbCode);
                        Delivery();
//                            setSuccessDialog();
                    }
                    Toast.makeText(context, "Listas baixadas com sucesso", Toast.LENGTH_SHORT).show();
                    ListScreenProgressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListScreenProgressBar.setVisibility(View.GONE);
                Toast.makeText(context, getResources().getString(R.string.list_screen1), Toast.LENGTH_LONG).show();
                preferences.clearListID();
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
    }

    private void JsonParseListScreen3() {
        ListScreenProgressBar.setVisibility(View.VISIBLE);
        String url1 = ApiUtils.GET_LIST;
        String url2 = preferences.getHostUrl() + ApiUtils.GET_LIST1;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2 + preferences.getListID(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "JsonParseListScreen: " + response);

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
                    System.out.println(success1);

                    JSONArray array = response.getJSONArray("documentos");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Log.e(TAG, "JsonParseListScreenArray: " + object);

                        int customerID = object.getInt("idCliente");
                        int contractID = object.getInt("idContrato");
                        String hawbCode = object.getString("codHawb");
                        String numberOrder = object.getString("numeroEncomandaCliente");
                        String recipientName = object.getString("nomeDestinatario");
                        int dna = object.getInt("dna");
                        int attempts = object.getInt("tentativas");
                        String specialPhoto = object.getString("idCCusto");
                        int score = object.getInt("score");
                        String clientNumber = object.getString("numeroEncomandaCliente");

                        TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber);
                        boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                        System.out.println(success);

                        boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                        System.out.println(tableFourHawbCode);
                        Returns();
//                            setSuccessDialog();
                    }
                    Toast.makeText(context, "Listas baixadas com sucesso", Toast.LENGTH_SHORT).show();


                    ListScreenProgressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListScreenProgressBar.setVisibility(View.GONE);
                Toast.makeText(context, getResources().getString(R.string.list_screen1), Toast.LENGTH_LONG).show();
                preferences.clearListID();
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
    }

    public void PutJsonRequest() {
        Cursor data = mDatabaseHelper.getDeliveryData(); //table3
        Cursor data1 = mDatabaseHelper.getDataFromTableFour();//Table4
        ArrayList<String> codHawb = new ArrayList<String>();
        ArrayList<String> dataHoraBaixa = new ArrayList<String>();
        ArrayList<String> latitude = new ArrayList<String>();
        ArrayList<String> longitude = new ArrayList<String>();
        ArrayList<String> nivelBateria = new ArrayList<String>();
        ArrayList<String> tipoBaixa = new ArrayList<String>();
        ArrayList<String> foto = new ArrayList<String>();
        ArrayList<String> relationID = new ArrayList<String>();

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
            relationID.add(data.getString(2));


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
                jsonObj.put("idGrauParentesco", Utils.ConvertArrayListToString(relationID));
                jsonArray.put(jsonObj);

                jsonObj1.put("imei", preferences.getIMEI());
                jsonObj1.put("franquia", preferences.getFranchise());
                jsonObj1.put("sistema", preferences.getSystem());
                jsonObj1.put("lista", preferences.getListID());
                jsonObj1.put("entregas", jsonArray);

                Log.e(TAG, "PutJsonRequest: " + jsonObj1);

                String url1 = ApiUtils.GET_LIST;
                String url2 = preferences.getHostUrl() + ApiUtils.GET_LIST1;

                final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url2 + preferences.getListID(), jsonObj1, new Response.Listener<JSONObject>() {
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
                relationID.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DeleteDataUponSyncOrUpload();
        mDatabaseHelper.DeleteFromTableThreeUponSync();//Table 3
        Log.e(TAG, "getDeliveryData: " + data.getCount());
        Log.e(TAG, "getDataFromTableFour: " + data1.getCount());
        if (data.getCount() == 0 && data1.getCount() == 0) {
            mDatabaseHelper.DeleteDataFromTableTwo();
            preferences.clearListID();
        }
    }

    private void ConfirmSuccessDialog(String type) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        String formattedDate = df.format(c.getTime());
        String formatTime = time.format(c.getTime());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sucesso");
        //Setting message manually and performing action on button click
        builder.setMessage("Hawb " + type + " com sucesso em " + formattedDate + " as " + formatTime)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        title.setText("Lista : " + preferences.getListID());
                        imei.setText("IMEI : " + preferences.getIMEI());
                        rl2.setVisibility(View.VISIBLE);
                        rl1.setVisibility(View.GONE);
                        spinner.setSelection(0);
                        Intent intent = new Intent("list_screen");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Atenção");
        alert.show();

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String fileName = "temp";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        String a = image.getName();

        Log.d("TAG", "createImageFileList: " + currentPhotoPath);
        Log.d("TAG", "createImageFileList: " + a);

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                OutImage = Bitmap.createScaledBitmap(photo, 600, 800, true);
                preferences.setPhotoBoolean("true");
                Log.e(TAG, "onActivityResultList: " + OutImage);
                camera.setText("Selecione a foto" + "   ✔");
                Toast.makeText(context, getResources().getString(R.string.list_screen4), Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onActivityResult: Data Null");
            }
        }
    }

    private class ListCodeUpdater extends BroadcastReceiver {
        @Override
        public void onReceive(Context i, Intent intent) {
            title.setText("Sem Listas");
        }
    }

    private class ListScreenUpdater extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Cursor data = mDatabaseHelper.getDeliveryData(); //table3
            Cursor data1 = mDatabaseHelper.getDataFromTableFour();
            if (data.getCount() == 0 && data1.getCount() == 0) {
                mDatabaseHelper.DeleteDataFromTableTwo();
                preferences.clearListID();
            }
        }
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
        Log.e(TAG, "onLocationChangedList: " + location.getLatitude() + ", " + location.getLongitude());
        preferences.setLatitude(String.valueOf(location.getLatitude()));
        preferences.setLongitude(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        //Toast.makeText(context, "Habilite o GPS e a Internet", Toast.LENGTH_SHORT).show();
        //Log.e(TAG, "onProviderDisabled: ");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
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
