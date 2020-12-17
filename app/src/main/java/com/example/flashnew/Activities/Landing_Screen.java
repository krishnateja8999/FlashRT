package com.example.flashnew.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Fragments.Collect;
import com.example.flashnew.Fragments.List;
import com.example.flashnew.Fragments.Messages;
import com.example.flashnew.Fragments.Search;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.LoginActivity;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;
import com.example.flashnew.Server.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Landing_Screen extends AppCompatActivity {
    public NavigationView navigationView;
    public Toolbar toolbar, searchToolbar;
    private BottomNavigationView bottomNavigationView;
    private RelativeLayout layout;
    private ImageView log_out;
    public static final int PERMISSION_REQ_CODE = 200;
    private String[] permissions;
    private ProgressDialog progressDialog;
    private AppPrefernces preferences;
    private DatabaseHelper databaseHelper;
    private RequestQueue queue;
    private InternetConnectionChecker internetChecker;


    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing__screen);
        navigationView = findViewById(R.id.nav_view);
        layout = findViewById(R.id.container2);
        toolbar = findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        preferences = new AppPrefernces(this);
        permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        changeFragment(new Dashboard());
        changeFragment(new List());
        layout = findViewById(R.id.container2);
        databaseHelper = new DatabaseHelper(this);
        queue = Volley.newRequestQueue(this);
        internetChecker = new InternetConnectionChecker(this);
        PermissionsRequest();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        log_out = findViewById(R.id.log_out);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }

        if (Build.VERSION.SDK_INT <= 24) {
            // preferences.setIMEI(telephonyManager.getDeviceId());
            preferences.setIMEI(getDeviceUniqueID(this));
        } else {
            preferences.setIMEI(getDeviceUniqueID(this));
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawer.closeDrawers();
                //             navigationView.setItemBackgroundResource(R.color.white);
                switch (menuItem.getItemId()) {

                    case R.id.account:
                        startActivity(new Intent(Landing_Screen.this, Profile_Class.class));
                        return true;
                    case R.id.about:
                        // startActivity(new Intent(Landing_Screen.this,About.class));
                        Log.e(TAG, "onClick: " + preferences.getListID().toString());
                        return true;

                    case R.id.list_delete:
                        if (preferences.getListID().equals("") || preferences.getListID().equals(" ")) {
                            Toast.makeText(Landing_Screen.this, "Sem listas", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Landing_Screen.this);
                            final EditText edittext = new EditText(Landing_Screen.this);
                            edittext.setBackgroundResource(R.drawable.edit_text_border);
                            edittext.setPadding(30, 30, 30, 30);
                            edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            builder1.setCancelable(true);
                            builder1.setTitle(getResources().getString(R.string.Login_screen1));
                            builder1.setMessage(getResources().getString(R.string.Login_screen2));
                            builder1.setView(edittext);
                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (edittext.getText().toString().length() == 0) {
                                                edittext.setError(getResources().getString(R.string.Login_screen9));
                                            } else if (edittext.getText().toString().equals(getResources().getString(R.string.lista_coletas_feitass))) {
                                                listDownloadDialog();
                                            } else {
                                                edittext.setError(getResources().getString(R.string.Login_screen10));
                                                Toast.makeText(Landing_Screen.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder1.create();
                            alert.show();
                        }
                        return true;

//                    case R.id.nav_logout:
//                        preferences.logout();
//                        startActivity(new Intent(Landing_Screen.this, LoginActivity.class));
//                        databaseHelper.truncateAllTablesOnLogout();
//                        finish();
//                        return true;
                }
                return false;
            }
        });
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.list);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        final View iconView =
                menuView.getChildAt(2).findViewById(com.google.android.material.R.id.icon);
        iconView.setScaleY(1.5f);
        iconView.setScaleX(1.5f);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.collect:
                        changeFragment(new Collect());
                        return true;
                    case R.id.messages:
                        changeFragment(new Messages());
                        return true;
                    case R.id.search:
                        changeFragment(new Search());
                        return true;
                    case R.id.nav_dashboard:
                        if (internetChecker.checkInternetConnection()) {
                            Cursor data = databaseHelper.getDeliveryData(); //table3
                            if (data.getCount() == 0) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Landing_Screen.this);
                                builder1.setTitle(getResources().getString(R.string.Login_screen1));
                                builder1.setMessage("Sem listas para sincronizar");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert1 = builder1.create();
                                alert1.show();
                            } else {
                                progressDialog = new ProgressDialog(Landing_Screen.this);
                                progressDialog.setTitle("Carregando dados");
                                progressDialog.setMessage("Por favor, espere..");
                                progressDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
                                progressDialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            PutJsonRequest();
                                            Intent intent = new Intent("list_code_status");
                                            LocalBroadcastManager.getInstance(Landing_Screen.this).sendBroadcast(intent);

                                            Intent intent1 = new Intent("list_screen");
                                            LocalBroadcastManager.getInstance(Landing_Screen.this).sendBroadcast(intent1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                            }
                        } else {
                            Toast.makeText(Landing_Screen.this, "Sem conexão de internet", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.list:
                        changeFragment(new List());
                        return true;
                }
                return false;
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Landing_Screen.this);
                builder1.setTitle(getResources().getString(R.string.Login_screen1));
                builder1.setMessage("Você quer sair?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferences.logout();
                        startActivity(new Intent(Landing_Screen.this, LoginActivity.class));
                        databaseHelper.truncateAllTablesOnLogout();
                        finish();
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //Creating dialog box
                AlertDialog alert1 = builder1.create();
                alert1.show();
            }
        });

    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    public void changeFragment1(int fragment) {
        switch (fragment) {
            case 1:
//                bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(R.id.list);
                break;
            case 3:
                bottomNavigationView.setSelectedItemId(R.id.collect);
                break;
            case 4:
                bottomNavigationView.setSelectedItemId(R.id.search);
                break;
            case 5:
                bottomNavigationView.setSelectedItemId(R.id.messages);
                break;

        }
    }

    private void listDownloadDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setBackgroundResource(R.drawable.edit_text_border);
        edittext.setPadding(30, 30, 30, 30);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setTitle(getResources().getString(R.string.Login_screen1));
        builder.setMessage("Digite o código da lista para excluir");
        builder.setCancelable(false);
        builder.setView(edittext);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //preferences.setListID(edittext.getText().toString());
                        if (preferences.getListID().equals(edittext.getText().toString())) {
                            databaseHelper.DeleteDataFromTableTwo();
                            preferences.clearListID();
                            Intent intent = new Intent("list_code_status");
                            LocalBroadcastManager.getInstance(Landing_Screen.this).sendBroadcast(intent);
                        } else {
                            Toast.makeText(Landing_Screen.this, "O código da lista inserido está errado", Toast.LENGTH_SHORT).show();
                        }
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
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Landing_Screen.this, Landing_Screen.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public String getDeviceUniqueID(Activity activity) {
        @SuppressLint("HardwareIds") String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    private void PermissionsRequest() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), permissions[4]) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Landing_Screen.this);
            builder1.setTitle(getResources().getString(R.string.Login_screen1));
            builder1.setMessage(getResources().getString(R.string.permissions_request));
            builder1.setCancelable(false);
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    acceptPermissions();
                }
            });
            builder1.setNegativeButton("SAIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //Creating dialog box
            AlertDialog alert1 = builder1.create();
            alert1.show();
        }
    }

    private void acceptPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[4]) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(permissions, PERMISSION_REQ_CODE);
            else {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]) != PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[4]) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(permissions, PERMISSION_REQ_CODE);
            }
        }
    }

    public void PutJsonRequest() {

        Cursor data = databaseHelper.getDeliveryData(); //table3
        Cursor data1 = databaseHelper.getDataFromTableFour();
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
                relationID.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DeleteDataUponSyncOrUpload();
        databaseHelper.DeleteFromTableThreeUponSync();
        SyncFinished();
        Intent intent = new Intent("list_view_updater");
        LocalBroadcastManager.getInstance(Landing_Screen.this).sendBroadcast(intent);
        Log.e(TAG, "getDeliveryData: " + data.getCount());
        Log.e(TAG, "getDataFromTableFour: " + data1.getCount());
        if (data.getCount() == 0 && data1.getCount() == 0) {
            databaseHelper.DeleteDataFromTableTwo();
            preferences.clearListID();
            changeFragment(new List());
        }
    }

    private void SyncFinished() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(getResources().getString(R.string.Login_screen1));
        builder1.setMessage("Sincronização concluída");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    private void DeleteDataUponSyncOrUpload() {
        Cursor data = databaseHelper.getDeliveryData();
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(TAG, "HawbStringArray: ");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                databaseHelper.DeleteDataUponUpload(Utils.ConvertArrayListToString(list));
                list.clear();
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}