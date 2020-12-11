package com.example.flashnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.retrofitRelated.APIservice;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private CardView masterPasswordDialog1, masterPasswordDialog2;
    private TextView masterPasswordOK, hostserverUrl, identification, userName, password;
    private EditText masterPasswordEditText;
    private Button confirm_login, cancel_login;
    private RequestQueue queue;
    private ProgressBar loginProgressBar;
    private ProgressDialog dialog;
    private androidx.appcompat.app.AlertDialog.Builder dialog1;
    private AppPrefernces preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        masterPasswordOK = findViewById(R.id.masterPasswordOK);
        masterPasswordDialog1 = findViewById(R.id.masterPasswordDialog);
        masterPasswordDialog2 = findViewById(R.id.masterPasswordDialog2);
        masterPasswordEditText = findViewById(R.id.masterPasswordEditText);
        hostserverUrl = findViewById(R.id.hostserverUrl);
        identification = findViewById(R.id.identification);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        confirm_login = findViewById(R.id.confirm_login);
        cancel_login = findViewById(R.id.cancel_login);
        queue = Volley.newRequestQueue(this);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        dialog = new ProgressDialog(this);
        dialog1 = new androidx.appcompat.app.AlertDialog.Builder(this);
        preferences = new AppPrefernces(this);

        if (preferences.isLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, Landing_Screen.class);
            startActivity(i);
            finish();
        }

        masterPasswordOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterPasswordEditText.getText().toString().length() <= 0) {
                    masterPasswordEditText.setError(getResources().getString(R.string.Login_screen9));
                } else if (masterPasswordEditText.getText().toString().equals(getResources().getString(R.string.lista_coletas_feitass))) {
                    masterPasswordDialog1.setVisibility(View.GONE);
                    masterPasswordDialog2.setVisibility(View.VISIBLE);
                } else {
                    masterPasswordEditText.setError(getResources().getString(R.string.Login_screen10));
                }
            }
        });

        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userName.setBackgroundResource(R.drawable.edit_text_border);
                } else {
                    userName.setBackgroundResource(R.drawable.edit_text_grey_border);
                }
            }
        });

        identification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    identification.setBackgroundResource(R.drawable.edit_text_border);
                } else {
                    identification.setBackgroundResource(R.drawable.edit_text_grey_border);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    password.setBackgroundResource(R.drawable.edit_text_border);
                } else {
                    password.setBackgroundResource(R.drawable.edit_text_grey_border);
                }
            }
        });

        confirm_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (identification.getText().toString().length() <= 0) {
                        identification.setError(getResources().getString(R.string.Login_screen5));
                    } else if (userName.getText().toString().length() <= 0) {
                        userName.setError(getResources().getString(R.string.Login_screen6));
                    } else if (password.getText().toString().length() <= 0) {
                        password.setError(getResources().getString(R.string.Login_screen7));
                    } else {
                        jsonParse();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "LoginActivityJson2: " + e.getMessage());
                }
            }
        });


        cancel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void jsonParse() {
        try {
            loginProgressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.GET, ApiUtils.LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "LoginActivityJson: " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String id = object.getString("id");
                        String login = object.getString("login");
                        String name = object.getString("nome");

                        Log.d("TAG", "LoginResponse: " + login);
                        preferences.setID(id);
                        preferences.setUserName(login);
                        preferences.setName(name);
                        preferences.setPaso(password.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(LoginActivity.this, Landing_Screen.class);
                    startActivity(i);
                    loginProgressBar.setVisibility(View.GONE);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loginProgressBar.setVisibility(View.GONE);
                    try {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "LoginActivityJson3: " + error.getMessage());

                    } catch (Exception e) {
                        Log.d("TAG", "LoginActivityJson4: " + e.getMessage());
                        dialog1.setTitle("Erro");
                        dialog1.setMessage(getResources().getString(R.string.Login_screen8));
                        dialog1.setCancelable(true);
                        dialog1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        userName.setText("");
                                        identification.setText("");
                                        password.setText("");
                                        dialog.cancel();
                                    }
                                });
                        dialog1.show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String auth1 = "Basic "
                            + Base64.encodeToString((userName.getText().toString() + ":" + password.getText().toString()).getBytes(),
                            Base64.NO_WRAP);
                    params.put("Authorization", auth1);
                    return params;
                }
            };
            queue.add(request);

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "LoginActivityJson1: " + e.getMessage());

        }

    }

    private void retroParse() {

        preferences.setUserName(userName.getText().toString());
        preferences.setPaso(password.getText().toString());


        APIservice mAPIService = ApiUtils.getAPIService();
        JSONObject sendingObj = new JSONObject();

        Call<JsonElement> call = mAPIService.login();
        call.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Log.e("TAG", "retrofitResponse: " + response);
                Intent i = new Intent(LoginActivity.this, Landing_Screen.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("TAG", "retrofitResponse: " + t);

            }
        });
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


//This is a test