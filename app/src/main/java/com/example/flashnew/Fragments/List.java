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
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.core.content.ContextCompat;
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
import com.example.flashnew.HelperClasses.GetCurrentLocation;
import com.example.flashnew.HelperClasses.UploadImages;
import com.example.flashnew.Modals.ListImageModal;
import com.example.flashnew.Modals.ResearchListModal;
import com.example.flashnew.Modals.TableOneDelivererModal;
import com.example.flashnew.Modals.TableThreeDeliveryModal;
import com.example.flashnew.Modals.TableTwoListModal;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;
import com.example.flashnew.Server.Utils;
import com.google.cloud.storage.Storage;

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
import static com.example.flashnew.Server.Utils.VERSION;

public class List extends Fragment {

    private Bitmap OutImage;
    private RequestQueue queue;
    private TextView title, imei;
    private File photoFile = null;
    private Landing_Screen context;
    private String currentPhotoPath;
    private RelativeLayout rl1, rl2;
    private Spinner attemptsDropDown;
    private Button conf, can, camera;
    private AppPrefernces preferences;
    private Spinner spinner, spinner2;
    private AutoCompleteTextView hawb;
    private DatabaseHelper mDatabaseHelper;
    private ListCodeUpdater listCodeUpdater;
    private CardView listScreenListDownload;
    private LinearLayout linearLayout, retur;
    private ProgressBar ListScreenProgressBar;
    private ListScreenUpdater listScreenUpdater;
    private InternetConnectionChecker internetChecker;
    private String[] values2, enderec, ausente, nao_visitado, outros, deliveryDna, returnDna;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list, container, false);

        rl1 = view.findViewById(R.id.rl);
        rl2 = view.findViewById(R.id.rl1);
        hawb = view.findViewById(R.id.hawb);
        queue = Volley.newRequestQueue(context);
        preferences = new AppPrefernces(context);
        can = view.findViewById(R.id.buttonCancel);
        spinner2 = view.findViewById(R.id.spinner2);
        conf = view.findViewById(R.id.buttonConfirm);
        imei = view.findViewById(R.id.actionbarImei);
        mDatabaseHelper = new DatabaseHelper(context);
        camera = view.findViewById(R.id.buttonPhotoAR);
        title = view.findViewById(R.id.actionbarTitle);
        spinner = view.findViewById(R.id.targetOptions);
        retur = view.findViewById(R.id.buttonDevolucao);
        linearLayout = view.findViewById(R.id.buttonEntrega);
        internetChecker = new InternetConnectionChecker(context);
        attemptsDropDown = view.findViewById(R.id.attemptsDropDown);
        ListScreenProgressBar = view.findViewById(R.id.ListScreenProgressBar);
        listScreenListDownload = view.findViewById(R.id.listScreenListDownload);

        camera.setEnabled(false);
        camera.setBackground(getResources().getDrawable(R.drawable.rounded_grey_filled_bg));

        String[] items = new String[]{"1"};
        values2 = getResources().getStringArray(R.array.motivo_grupo);
        ArrayAdapter<String> attemptAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        attemptsDropDown.setAdapter(attemptAdapter);

        String[] tab_names = getResources().getStringArray(R.array.grau_relacionamento);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tab_names);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);

        enderec = getResources().getStringArray(R.array.motivo_dev);
        outros = getResources().getStringArray(R.array.motivo_outros);
        ausente = getResources().getStringArray(R.array.motivo_ausente);
        returnDna = getResources().getStringArray(R.array.list_return_dna);
        deliveryDna = getResources().getStringArray(R.array.list_delivery_dna);
        nao_visitado = getResources().getStringArray(R.array.motivo_nao_visitado);

        listCodeUpdater = new ListCodeUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listCodeUpdater, new IntentFilter("list_code_status"));
        listScreenUpdater = new ListScreenUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listScreenUpdater, new IntentFilter("list_screen"));

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                GetCurrentLocation.Location(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

        hawb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //DnaConfirmForHawbET();
            }

            @Override
            public void afterTextChanged(Editable s) {
                DnaConfirmForHawbET();
//                if (hawb.getText().toString().length() <= 0) {
//                    camera.setVisibility(View.GONE);
//                    camera.setBackground(getResources().getDrawable(R.drawable.rounded_grey_filled_bg));
//                    camera.setEnabled(false);
//                } else {
//                    camera.setBackground(getResources().getDrawable(R.drawable.rounded_blue_button));
//                    camera.setEnabled(true);
//                }
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
                    if (internetChecker.checkInternetConnection()) {
                        listDownloadDialog();
                    } else {
                        Toast.makeText(context, "Sem conexão de internet", Toast.LENGTH_SHORT).show();
                    }
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
                //ConfirmButton();
                ConfirmButton2();
//                boolean check = mDatabaseHelper.CheckHawbCode(hawb.getText().toString());
//                if (preferences.getLowType().equals("ENTREGA")) {
//                    if (hawb.getText().toString().length() == 0) {
//                        Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
//                        hawb.requestFocus();
//                    } else if (!check) {
//                        Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
//                    } else if (spinner.getSelectedItem().toString().equals("-- Selecionar parentesco --")) {
//                        Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
//                    } else if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
//                        Toast.makeText(context, "Por favor carregue uma foto", Toast.LENGTH_SHORT).show();
//                    } else {
//                        storeDeliveryData();
//                        if (internetChecker.checkInternetConnection()) {
//                            PutJsonRequest();
//                        }
//                        mDatabaseHelper.deleteHawbFromTableFour(hawb.getText().toString());
//                        mDatabaseHelper.ValidateDataWithSecondTable(hawb.getText().toString());
//                        boolean AddDeliveryType = mDatabaseHelper.AddDeliveryType("ENTREGA");
//                        System.out.println(AddDeliveryType);
//                        ConfirmSuccessDialog("entregue");
//                    }
//                } else {
//                    if (hawb.getText().toString().length() == 0) {
//                        Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
//                        hawb.requestFocus();
//                    } else if (!check) {
//                        Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
//                    } else if (spinner2.getSelectedItem().toString().equals("-- Selecionar grupo --")) {
//                        Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
//                    } else if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
//                        Toast.makeText(context, "Por favor carregue uma foto", Toast.LENGTH_SHORT).show();
//                    } else {
//                        storeDeliveryData();
//                        if (internetChecker.checkInternetConnection()) {
//                            PutJsonRequest();
//                        }
//                        mDatabaseHelper.deleteHawbFromTableFour(hawb.getText().toString());
//                        mDatabaseHelper.ValidateDataWithSecondTable(hawb.getText().toString());
//                        boolean AddDeliveryType = mDatabaseHelper.AddDeliveryType("DEVOLUCAO");
//                        System.out.println(AddDeliveryType);
//                        ConfirmSuccessDialog("devolvida");
//                    }
//                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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
            if (internetChecker.checkInternetConnection()) {
                listDownloadDialogForDelivery();
            } else {
                Toast.makeText(context, "Sem conexão de internet", Toast.LENGTH_SHORT).show();
            }
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
            preferences.setImagePath("");
            String[] tab_names = getResources().getStringArray(R.array.grau_relacionamento);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tab_names);
            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter1);
            preferences.setLowType("ENTREGA");
            preferences.setImageType("img_ar");
            preferences.setPhotoBoolean("false");
            HawbStringArray();
        }
    }

    private void Returns() {
        Cursor data = mDatabaseHelper.getDataFromTableFour();
        if (preferences.getListID().equals(" ") || preferences.getListID() == null) {
            if (internetChecker.checkInternetConnection()) {
                listDownloadDialogForReturn();
            } else {
                Toast.makeText(context, "Sem conexão de internet", Toast.LENGTH_SHORT).show();
            }
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
            preferences.setImagePath("");
            rl2.setVisibility(View.GONE);
            rl1.setVisibility(View.VISIBLE);
            preferences.setLowType("DEVOLUCAO");
            preferences.setImageType("img_local");
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
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        String spinnerValue = spinner.getSelectedItem().toString();
        int spinnerID = Integer.parseInt(spinnerValue.replaceAll("[^0-9]", ""));
        java.util.List<ListImageModal> imageModals = mDatabaseHelper.GetImgDetails(hawb.getText().toString());
//        <customer code>_<contract code>_<image type>*_<hawb>_img_rt_<customer number>**_<AAAAMMDDHHMMSS>.png
//        3586_4801_img_ar_02406021825_img_rt_OMtest001_20200111171205.png

        String clientNumber = mDatabaseHelper.CheckClientNumber(hawb.getText().toString());
        boolean HawbChecker = mDatabaseHelper.CheckHawbCode1(clientNumber);
        if (HawbChecker) {
            for (ListImageModal list231 : imageModals) {
                String imageName1 = list231.getCustomerCode() + "_" + list231.getContractCode() + "_" + preferences.getImageType() + "_" + clientNumber + "_img_rt_" + list231.getCustomerNumber() + "_" + timeStamp + ".png";
                mDatabaseHelper.addDataToTableThree(new TableThreeDeliveryModal(clientNumber, spinnerID,
                        attemptsDropDown.getSelectedItem().toString(), formattedDate, batLevel, preferences.getLowType(), preferences.getPhotoBoolean(), preferences.getLatitude(), preferences.getLongitude(), preferences.getImagePath(), imageName1));
            }
        } else {
            for (ListImageModal list231 : imageModals) {
                String imageName2 = list231.getCustomerCode() + "_" + list231.getContractCode() + "_" + preferences.getImageType() + "_" + hawb.getText().toString() + "_img_rt_" + list231.getCustomerNumber() + "_" + timeStamp + ".png";
                mDatabaseHelper.addDataToTableThree(new TableThreeDeliveryModal(hawb.getText().toString(), spinnerID,
                        attemptsDropDown.getSelectedItem().toString(), formattedDate, batLevel, preferences.getLowType(), preferences.getPhotoBoolean(), preferences.getLatitude(), preferences.getLongitude(), preferences.getImagePath(), imageName2));
            }
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

                    if (totalDocuments == 0) {
                        Toast.makeText(context, "Nenhum Hawbs na lista.", Toast.LENGTH_SHORT).show();
                    } else {
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

                            if (dna == 8192 || dna == 65536) {
                                boolean check = mDatabaseHelper.CheckResearchListData(hawbCode);
                                if (check) {
                                    Toast.makeText(context, "A pesquisa " + hawbCode + " já existe", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject object1 = object.getJSONObject("endereco");
                                    String publicPlace = object1.getString("logradouro");
                                    String aptNo = object1.getString("numero");
                                    String neighbourHood = object1.getString("bairro");
                                    String city = object1.getString("cidade");
                                    String state = object1.getString("UF");
                                    int pinCode = object1.getInt("CEP");

                                    ResearchListModal researchListModal = new ResearchListModal(hawbCode, numberOrder, recipientName, dna,
                                            aptNo, publicPlace, neighbourHood, city, state, pinCode, customerID, contractID, lists);
                                    boolean success = mDatabaseHelper.AddResearchList(researchListModal);
                                    System.out.println("Data Added to ResearchList Table " + success);
                                    Log.e(TAG, "exists: ");
                                }
                            } else {
                                Log.e(TAG, "Research doesn't exist: ");
                            }

                            if (object.has("latitude") && object.has("longitude")) {
                                double latitude = object.getDouble("latitude");
                                double longitude = object.getDouble("longitude");
                                TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                        hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber, latitude, longitude);
                                boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                                System.out.println(success);

                                boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                                System.out.println(tableFourHawbCode);
                                Log.e(TAG, "Hawb has lat, long");

                            } else {
                                TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                        hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber, 0, 0);
                                boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                                System.out.println(success);

                                boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                                System.out.println(tableFourHawbCode);
                                Log.e(TAG, "Hawb doesn't have lat, long");
                            }
                            FragmentTransaction fragmentTransaction = context
                                    .getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, new HawbLists());
                            fragmentTransaction.commit();
                        }
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
                params.put("x-versao-rt", VERSION);
                params.put("x-rastreador", preferences.getTracker());
                return params;
            }
        };
        queue.add(request);
    }

    private void JsonParseListScreen2() {
        ListScreenProgressBar.setVisibility(View.VISIBLE);
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

                    if (totalDocuments == 0) {
                        Toast.makeText(context, "Nenhum Hawbs na lista.", Toast.LENGTH_SHORT).show();
                    } else {
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

                            if (dna == 8192 || dna == 65536) {
                                boolean check = mDatabaseHelper.CheckResearchListData(hawbCode);
                                if (check) {
                                    Toast.makeText(context, "A pesquisa " + hawbCode + " já existe", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject object1 = object.getJSONObject("endereco");
                                    String publicPlace = object1.getString("logradouro");
                                    String aptNo = object1.getString("numero");
                                    String neighbourHood = object1.getString("bairro");
                                    String city = object1.getString("cidade");
                                    String state = object1.getString("UF");
                                    int pinCode = object1.getInt("CEP");

                                    ResearchListModal researchListModal = new ResearchListModal(hawbCode, numberOrder, recipientName, dna,
                                            aptNo, publicPlace, neighbourHood, city, state, pinCode, customerID, contractID, lists);
                                    boolean success = mDatabaseHelper.AddResearchList(researchListModal);
                                    System.out.println("Data Added to ResearchList Table " + success);
                                    Log.e(TAG, "exists: ");
                                }
                            } else {
                                Log.e(TAG, "doesn't exist: ");
                            }

                            if (object.has("latitude") && object.has("longitude")) {
                                double latitude = object.getDouble("latitude");
                                double longitude = object.getDouble("longitude");
                                TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                        hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber, latitude, longitude);
                                boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                                System.out.println(success);

                                boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                                System.out.println(tableFourHawbCode);
                                Log.e(TAG, "Hawb has lat, long");

                            } else {
                                TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                        hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber, 0, 0);
                                boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                                System.out.println(success);

                                boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                                System.out.println(tableFourHawbCode);
                                Log.e(TAG, "Hawb doesn't have lat, long");
                            }
                            Delivery();
                        }
                        Toast.makeText(context, "Listas baixadas com sucesso", Toast.LENGTH_SHORT).show();
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
                params.put("x-versao-rt", VERSION);
                params.put("x-rastreador", preferences.getTracker());
                return params;
            }
        };
        queue.add(request);
    }

    private void JsonParseListScreen3() {
        ListScreenProgressBar.setVisibility(View.VISIBLE);
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

                    if (totalDocuments == 0) {
                        Toast.makeText(context, "Nenhum Hawbs na lista.", Toast.LENGTH_SHORT).show();
                    } else {
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

                            if (dna == 8192 || dna == 65536) {
                                boolean check = mDatabaseHelper.CheckResearchListData(hawbCode);
                                if (check) {
                                    Toast.makeText(context, "A pesquisa " + hawbCode + " já existe", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject object1 = object.getJSONObject("endereco");
                                    String publicPlace = object1.getString("logradouro");
                                    String aptNo = object1.getString("numero");
                                    String neighbourHood = object1.getString("bairro");
                                    String city = object1.getString("cidade");
                                    String state = object1.getString("UF");
                                    int pinCode = object1.getInt("CEP");

                                    ResearchListModal researchListModal = new ResearchListModal(hawbCode, numberOrder, recipientName, dna,
                                            aptNo, publicPlace, neighbourHood, city, state, pinCode, customerID, contractID, lists);
                                    boolean success = mDatabaseHelper.AddResearchList(researchListModal);
                                    System.out.println("Data Added to ResearchList Table " + success);
                                    Log.e(TAG, "exists: ");
                                }
                            } else {
                                Log.e(TAG, "doesn't exist: ");
                            }

                            if (object.has("latitude") && object.has("longitude")) {
                                double latitude = object.getDouble("latitude");
                                double longitude = object.getDouble("longitude");
                                TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                        hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber, latitude, longitude);
                                boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                                System.out.println(success);

                                boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                                System.out.println(tableFourHawbCode);
                                Log.e(TAG, "Hawb has lat, long");

                            } else {
                                TableTwoListModal tableTwoListModal = new TableTwoListModal(customerID, contractID,
                                        hawbCode, numberOrder, recipientName, dna, attempts, specialPhoto, score, clientNumber, 0, 0);
                                boolean success = mDatabaseHelper.addDataToTableTwo(tableTwoListModal);
                                System.out.println(success);

                                boolean tableFourHawbCode = mDatabaseHelper.addDataToTableFour(hawbCode, clientNumber);
                                System.out.println(tableFourHawbCode);
                                Log.e(TAG, "Hawb doesn't have lat, long");
                            }
                            Returns();
                        }
                        Toast.makeText(context, "Listas baixadas com sucesso", Toast.LENGTH_SHORT).show();
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
                params.put("x-versao-rt", VERSION);
                params.put("x-rastreador", preferences.getTracker());
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
        ArrayList<String> imagePath = new ArrayList<>();
        ArrayList<String> imageName = new ArrayList<>();

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
            imagePath.add(data.getString(10));
            imageName.add(data.getString(11));

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonObj1 = new JSONObject();
            try {
                Storage storage = UploadImages.setCredentials(context.getAssets().open("key.json"));
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: " + Utils.ConvertArrayListToString(imagePath));
                        Log.e(TAG, "run2: " + Utils.ConvertArrayListToString(imageName));
                        if (Utils.ConvertArrayListToString(imagePath).equals("") || Utils.ConvertArrayListToString(imagePath).equals(" ") || Utils.ConvertArrayListToString(imagePath).equals(null)) {
                            Log.e(TAG, "PutList: no image");
                        } else {
                            UploadImages.transmitImageFile(storage, Utils.ConvertArrayListToString(imagePath), Utils.ConvertArrayListToString(imageName));
                        }
                    }
                });
                thread.start();

                if (Utils.ConvertArrayListToString(tipoBaixa).equals("ENTREGA")) {
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
                } else {
                    jsonObj.put("codHawb", Utils.ConvertArrayListToString(codHawb));
                    jsonObj.put("dataHoraBaixa", Utils.ConvertArrayListToString(dataHoraBaixa));
                    jsonObj.put("nivelBateria", Utils.ConvertArrayListToString(nivelBateria));
                    jsonObj.put("tipoBaixa", Utils.ConvertArrayListToString(tipoBaixa));
                    jsonObj.put("foto", Utils.ConvertArrayListToString(foto));
                    jsonObj.put("latitude", Utils.ConvertArrayListToString(latitude));
                    jsonObj.put("longitude", Utils.ConvertArrayListToString(longitude));
                    jsonObj.put("idMotivo", Utils.ConvertArrayListToString(relationID));
                    jsonArray.put(jsonObj);

                    jsonObj1.put("imei", preferences.getIMEI());
                    jsonObj1.put("franquia", preferences.getFranchise());
                    jsonObj1.put("sistema", preferences.getSystem());
                    jsonObj1.put("lista", preferences.getListID());
                    jsonObj1.put("entregas", jsonArray);

                    Log.e(TAG, "PutJsonRequest: " + jsonObj1);
                }

                String url1 = ApiUtils.GET_LIST;
                String url2 = preferences.getHostUrl() + ApiUtils.GET_LIST1;

                final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url2 + preferences.getListID(), jsonObj1, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PostResposne();
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
                        params.put("x-versao-rt", VERSION);
                        params.put("x-rastreador", preferences.getTracker());
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
                imagePath.clear();
                imageName.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void PostResposne() {
        Cursor data = mDatabaseHelper.getDeliveryData(); //table3
        Cursor data1 = mDatabaseHelper.getDataFromTableFour();//Table4
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
            preferences.setImagePath(currentPhotoPath);
            preferences.setPhotoBoolean("true");
            camera.setText("Selecione a foto" + "   ✔");
            Toast.makeText(context, getResources().getString(R.string.list_screen4), Toast.LENGTH_SHORT).show();
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

    private void DnaConfirmForHawbET() {
        String a = mDatabaseHelper.GetDna(hawb.getText().toString());
        boolean checkHawb = mDatabaseHelper.CheckHawbCode(hawb.getText().toString());

        boolean check = Arrays.asList(deliveryDna).contains(a);//dna numbers
        boolean check2 = Arrays.asList(returnDna).contains(a);//dna numbers

        if (preferences.getLowType().equals("ENTREGA")) {

            if (hawb.getText().toString().length() <= 0) {
                camera.setVisibility(View.GONE);
            } else if (checkHawb) {//check if hawb is present in the list
                java.util.List<Double> latLongs = mDatabaseHelper.GetLatLong(hawb.getText().toString());
                double latit = Utils.GetLatitude(latLongs);
                double longtit = Utils.GetLongitude(latLongs);
                Log.e(TAG, "DnaConfirmForHawbET: " + latit + ", " + longtit);
                //Perimeter Check
                boolean perimeterCheck = Utils.FindPerimeter(latit, longtit, Double.parseDouble(preferences.getLatitude()), Double.parseDouble(preferences.getLongitude()));

                if (latit == 0.0 || latit == 0) {
                    if (check) {// check if the hawb dna requires picture
                        camera.setVisibility(View.VISIBLE);
                        camera.setBackground(getResources().getDrawable(R.drawable.rounded_blue_button));
                        camera.setEnabled(true);
                    } else {
                        camera.setVisibility(View.GONE);
                    }
                } else {
                    if (check || !perimeterCheck) {// check if the hawb dna requires picture
                        camera.setVisibility(View.VISIBLE);
                        camera.setBackground(getResources().getDrawable(R.drawable.rounded_blue_button));
                        camera.setEnabled(true);
                    } else {
                        camera.setVisibility(View.GONE);
                    }
                }
            } else {
                camera.setVisibility(View.GONE);
            }

        } else {

            if (hawb.getText().toString().length() <= 0) {
                camera.setVisibility(View.GONE);
            } else if (checkHawb) {
                java.util.List<Double> latLongs = mDatabaseHelper.GetLatLong(hawb.getText().toString());
                double latit = Utils.GetLatitude(latLongs);
                double longtit = Utils.GetLongitude(latLongs);
                //Perimeter Check
                boolean perimeterCheck = Utils.FindPerimeter(latit, longtit, Double.parseDouble(preferences.getLatitude()), Double.parseDouble(preferences.getLongitude()));

                if (latit == 0.0 || latit == 0) {
                    if (check2) {
                        camera.setVisibility(View.VISIBLE);
                        camera.setBackground(getResources().getDrawable(R.drawable.rounded_blue_button));
                        camera.setEnabled(true);
                    } else {
                        camera.setVisibility(View.GONE);
                    }
                } else {
                    if (check2 || !perimeterCheck) {
                        camera.setVisibility(View.VISIBLE);
                        camera.setBackground(getResources().getDrawable(R.drawable.rounded_blue_button));
                        camera.setEnabled(true);
                    } else {
                        camera.setVisibility(View.GONE);
                    }
                }
            } else {
                camera.setVisibility(View.GONE);
            }
        }
    }

    private void ConfirmButton() {
        boolean check = mDatabaseHelper.CheckHawbCode(hawb.getText().toString());
        boolean checkHawb = mDatabaseHelper.CheckHawbCode(hawb.getText().toString());

        String a = mDatabaseHelper.GetDna(hawb.getText().toString());
        boolean check1 = Arrays.asList(deliveryDna).contains(a);
        boolean check2 = Arrays.asList(returnDna).contains(a);
        try {


            if (preferences.getLowType().equals("ENTREGA")) {
                java.util.List<Double> latLongs = mDatabaseHelper.GetLatLong(hawb.getText().toString());
                double latit = Utils.GetLatitude(latLongs);
                double longtit = Utils.GetLongitude(latLongs);
                boolean checkForZero = Utils.CheckZeroInLocation(latit);
                //Perimeter Check
                boolean perimeterCheck = Utils.FindPerimeter(latit, longtit, Double.parseDouble(preferences.getLatitude()), Double.parseDouble(preferences.getLongitude()));

                if (hawb.getText().toString().length() == 0) {
                    Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
                    hawb.requestFocus();
                } else if (!check) {
                    Log.e(TAG, "Invalid hawb: ");
                    Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
                } else if (spinner.getSelectedItem().toString().equals("-- Selecionar parentesco --")) {
                    Log.e(TAG, "select spinner ");
                    Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
                } else if (Utils.CheckZeroInLocation(latit)) {
                    Log.e(TAG, "CheckZeroInLocation: ");
                    if (check1) {
                        Log.e(TAG, "CheckZeroInLocation: t OR f");
                        if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
                    }
                } else if (check1 || !perimeterCheck) {
                    Log.e(TAG, "CheckPerimeter: ");
                    if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
                    Log.e(TAG, "StoreData: ");
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
                java.util.List<Double> latLongs = mDatabaseHelper.GetLatLong(hawb.getText().toString());
                double latit = Utils.GetLatitude(latLongs);
                double longtit = Utils.GetLongitude(latLongs);
                boolean checkForZero = Utils.CheckZeroInLocation(latit);
                //Perimeter Check
                boolean perimeterCheck = Utils.FindPerimeter(latit, longtit, Double.parseDouble(preferences.getLatitude()), Double.parseDouble(preferences.getLongitude()));

                if (hawb.getText().toString().length() == 0) {
                    Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
                    hawb.requestFocus();
                } else if (!check) {
                    Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
                } else if (spinner2.getSelectedItem().toString().equals("-- Selecionar grupo --")) {
                    Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
                } else if (Utils.CheckZeroInLocation(latit)) {
                    if (check2) {
                        if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
                } else if (check1 || !perimeterCheck) {
                    if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ConfirmButton2() {
        boolean check = mDatabaseHelper.CheckHawbCode(hawb.getText().toString());

        String a = mDatabaseHelper.GetDna(hawb.getText().toString());
        boolean check1 = Arrays.asList(deliveryDna).contains(a);
        boolean check2 = Arrays.asList(returnDna).contains(a);

        if (preferences.getLowType().equals("ENTREGA")) {
            if (hawb.getText().toString().length() == 0) {
                Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
                hawb.requestFocus();
            } else if (!check) {
                Log.e(TAG, "Invalid hawb: ");
                Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
            } else {
                java.util.List<Double> latLongs = mDatabaseHelper.GetLatLong(hawb.getText().toString());
                double latit = Utils.GetLatitude(latLongs);
                double longtit = Utils.GetLongitude(latLongs);
                //Perimeter Check
                boolean perimeterCheck = Utils.FindPerimeter(latit, longtit, Double.parseDouble(preferences.getLatitude()), Double.parseDouble(preferences.getLongitude()));

                if (spinner.getSelectedItem().toString().equals("-- Selecionar parentesco --")) {
                    Log.e(TAG, "select spinner ");
                    Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
                } else if (Utils.CheckZeroInLocation(latit)) {
                    Log.e(TAG, "CheckZeroInLocation: ");
                    if (check1) {
                        Log.e(TAG, "CheckZeroInLocation: t OR f");
                        if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
                    }
                } else if (check1 || !perimeterCheck) {
                    Log.e(TAG, "CheckPerimeter: ");
                    if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
                    Log.e(TAG, "StoreData: ");
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
            }
        } else {
            if (hawb.getText().toString().length() == 0) {
                Toast.makeText(context, "Selecione um Hawb", Toast.LENGTH_LONG).show();
                hawb.requestFocus();
            } else if (!check) {
                Log.e(TAG, "Invalid hawb: ");
                Toast.makeText(context, "Hawb inserido é inválido", Toast.LENGTH_LONG).show();
            } else {
                java.util.List<Double> latLongs = mDatabaseHelper.GetLatLong(hawb.getText().toString());
                double latit = Utils.GetLatitude(latLongs);
                double longtit = Utils.GetLongitude(latLongs);
                //Perimeter Check
                boolean perimeterCheck = Utils.FindPerimeter(latit, longtit, Double.parseDouble(preferences.getLatitude()), Double.parseDouble(preferences.getLongitude()));

                if (spinner2.getSelectedItem().toString().equals("-- Selecionar grupo --")) {
                    Toast.makeText(context, "Selecione um item da lista suspensa", Toast.LENGTH_SHORT).show();
                } else if (Utils.CheckZeroInLocation(latit)) {
                    if (check2) {
                        if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
                } else if (check2 || !perimeterCheck) {
                    if (preferences.getImagePath().equals(" ") || preferences.getImagePath().equals("")) {
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }
}
