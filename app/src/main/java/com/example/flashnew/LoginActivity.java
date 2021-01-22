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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.flashnew.Server.InternetConnectionChecker;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.flashnew.Server.Utils.MASTER_PASSWORD;

public class LoginActivity extends AppCompatActivity {

    private TextView userName;
    private TextView password;
    private CheckBox checkbox;
    private RequestQueue queue;
    private TextView hostserverUrl;
    private TextView identification;
    private AppPrefernces preferences;
    private ProgressBar loginProgressBar;
    private EditText masterPasswordEditText;
    private InternetConnectionChecker internetChecker;
    private androidx.appcompat.app.AlertDialog.Builder dialog1;
    private CardView masterPasswordDialog1, masterPasswordDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        checkbox = findViewById(R.id.checkbox);
        queue = Volley.newRequestQueue(this);
        preferences = new AppPrefernces(this);
        hostserverUrl = findViewById(R.id.hostserverUrl);
        identification = findViewById(R.id.identification);
        Button cancel_login = findViewById(R.id.cancel_login);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        Button confirm_login = findViewById(R.id.confirm_login);
        internetChecker = new InternetConnectionChecker(this);
        TextView masterPasswordOK = findViewById(R.id.masterPasswordOK);
        masterPasswordDialog1 = findViewById(R.id.masterPasswordDialog);
        masterPasswordDialog2 = findViewById(R.id.masterPasswordDialog2);
        masterPasswordEditText = findViewById(R.id.masterPasswordEditText);
        dialog1 = new androidx.appcompat.app.AlertDialog.Builder(this);

        if (preferences.isLoggedIn() && preferences.isLoggedIn1()) {
            Intent i = new Intent(LoginActivity.this, Landing_Screen.class);
            startActivity(i);
            finish();
        }

        masterPasswordOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterPasswordEditText.getText().toString().length() <= 0) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen9), Toast.LENGTH_LONG).show();
                    masterPasswordEditText.setText("");
                } else if (masterPasswordEditText.getText().toString().equals(MASTER_PASSWORD)) {
                    masterPasswordDialog1.setVisibility(View.GONE);
                    masterPasswordDialog2.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen10), Toast.LENGTH_LONG).show();
                    masterPasswordEditText.setText("");
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

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hostserverUrl.setEnabled(isChecked);
            }
        });

        confirm_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identification.getText().toString().length() <= 0) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen5), Toast.LENGTH_LONG).show();
                    identification.requestFocus();
                } else if (userName.getText().toString().length() <= 0) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen6), Toast.LENGTH_LONG).show();
                    userName.requestFocus();
                } else if (password.getText().toString().length() <= 0) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen7), Toast.LENGTH_LONG).show();
                    password.requestFocus();
                } else if (!checkbox.isChecked()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen11), Toast.LENGTH_LONG).show();
                } else {
                    if (internetChecker.checkInternetConnection()) {
                        jsonParse();
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_screen12), Toast.LENGTH_LONG).show();
                    }
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
        loginProgressBar.setVisibility(View.VISIBLE);
        String url1 = ApiUtils.LOGIN;
        String url2 = hostserverUrl.getText().toString() + ApiUtils.LOGIN1;
        Log.e("TAG", "jsonParseLogin: " + url2);
        StringRequest request = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
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
                    preferences.setHostUrl(hostserverUrl.getText().toString());
                    preferences.setTracker(identification.getText().toString());

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
                dialog1.setTitle("Erro");
                dialog1.setMessage(getResources().getString(R.string.Login_screen8));
                dialog1.setCancelable(true);
                dialog1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userName.requestFocus();
                                dialog.cancel();
                            }
                        });
                dialog1.show();
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