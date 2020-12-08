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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.Fragments.Collect;
import com.example.flashnew.Fragments.List;
import com.example.flashnew.Fragments.Messages;
import com.example.flashnew.Fragments.Search;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.LoginActivity;
import com.example.flashnew.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import static android.content.ContentValues.TAG;

public class Landing_Screen extends AppCompatActivity {
    public NavigationView navigationView;
    private int mCartCount;
    RelativeLayout rl_notofications;

    private LayerDrawable mCartMenuIcon;
    TextView name;
    public Toolbar toolbar, searchToolbar;
    BottomNavigationView bottomNavigationView;
    RelativeLayout layout;
    CircularImageView profile_image;
    ImageView imageView, profile;
    public static final int PERMISSION_REQ_CODE = 200;
    private String[] permissions;
    ProgressDialog progressDialog;
    private AppPrefernces preferences;
    private DatabaseHelper databaseHelper;

    CircularImageView iv_profile_auditor;
    String mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing__screen);
        navigationView = findViewById(R.id.nav_view);
        layout = findViewById(R.id.container2);
        toolbar = findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        preferences = new AppPrefernces(this);
        permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        acceptPermissions();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        changeFragment(new Dashboard());
        changeFragment(new List());
        layout = findViewById(R.id.container2);
        databaseHelper = new DatabaseHelper(this);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.list_delete);
//        if (preferences.getListID().equals("")||preferences.getListID().equals(" ")) {
//            nav_item2.setEnabled(false);
//        }else {
//            nav_item2.setEnabled(true);
//        }
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
                            Toast.makeText(Landing_Screen.this, "No Lists", Toast.LENGTH_SHORT).show();
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

                    case R.id.nav_logout:
                        preferences.logout();
                        startActivity(new Intent(Landing_Screen.this, LoginActivity.class));
                        databaseHelper.truncateAllTablesOnLogout();
                        finish();
                        return true;
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
//                       changeFragment(new Dashboard());
                        progressDialog = new ProgressDialog(Landing_Screen.this);
                        progressDialog.setTitle("Carregando dados");
                        progressDialog.setMessage("Por favor, espere..");
                        progressDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
                        progressDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 3000);
                        return true;
                    case R.id.list:
                        changeFragment(new List());
                        return true;
                }
                return false;
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

    private void acceptPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(permissions, PERMISSION_REQ_CODE);
            else {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]) != PackageManager.PERMISSION_GRANTED))
                    requestPermissions(permissions, PERMISSION_REQ_CODE);
            }
        }
    }

}