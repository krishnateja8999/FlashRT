package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.Activities.ScannerActivity;
import com.example.flashnew.Adapters.CollectListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.HelperClasses.GetCurrentLocation;
import com.example.flashnew.Modals.CollectListModalClass;
import com.example.flashnew.Modals.TableFiveModel;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;

import net.skoumal.fragmentback.BackFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import fr.arnaudguyon.xmltojsonlib.XmlToJson;

import static android.content.ContentValues.TAG;


public class Collect extends Fragment implements BackFragment, SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar bar;
    private TextView no_lists;
    private Landing_Screen context;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;
    private RecyclerView recyclerViewCollectList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private InternetConnectionChecker internetChecker;
    private ArrayList<CollectListModalClass> listModalClasses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collect, container, false);

        prefernces = new AppPrefernces(context);
        bar = view.findViewById(R.id.collectBar);
        no_lists = view.findViewById(R.id.no_lists);
        mDatabaseHelper = new DatabaseHelper(context);
        Button button = view.findViewById(R.id.coletaScan);
        TextView imei = view.findViewById(R.id.actionbarImei);
        TextView title = view.findViewById(R.id.actionbarTitle);
        Button coletaDigit = view.findViewById(R.id.coletaDigit);
        internetChecker = new InternetConnectionChecker(context);
        swipeRefreshLayout = view.findViewById(R.id.swipe_collect);
        recyclerViewCollectList = view.findViewById(R.id.collectRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        QRCodeValidator qrCodeValidator = new QRCodeValidator();
        LocalBroadcastManager.getInstance(context).registerReceiver(qrCodeValidator, new IntentFilter("qr_code_validate"));
        ListUpdater listUpdater = new ListUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listUpdater, new IntentFilter("list_updater"));

        listModalClasses = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewCollectList.setLayoutManager(layoutManager);

        try {
            GetCurrentLocation.Location(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        title.setText("Coletas");
        imei.setText("IMEI : " + prefernces.getIMEI());
        Cursor data = mDatabaseHelper.getDataFromTableFour();
        if (data.getCount() == 0) {
            mDatabaseHelper.DeleteTableNine();
        }
        ColetaLists();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ScannerActivity.class);
                startActivity(i);
            }
        });

        coletaDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetChecker.checkInternetConnection()) {
                    DigitalColleta();
                } else {
                    Toast.makeText(context, "Sem conexão de internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void DigitalColleta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        edittext.setBackgroundResource(R.drawable.edit_text_border);
        edittext.setPadding(30, 30, 30, 30);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView title = new TextView(context);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.SANS_SERIF);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        title.setPadding(30, 20, 0, 20);
        title.setLayoutParams(lp);
        title.setText("Digite o código da coleta");
        builder.setCustomTitle(title);
        builder.setCancelable(true);
        builder.setView(edittext);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (internetChecker.checkInternetConnection()) {
                            InJson(Integer.parseInt(edittext.getText().toString()));
                        } else {
                            Toast.makeText(context, "Sem conexão de internet", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
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

    private void ColetaLists() {
        Cursor data = mDatabaseHelper.GetDataFromTableFive();
        listModalClasses = new ArrayList<>();
        if (data.getCount() == 0) {
            swipeRefreshLayout.setVisibility(View.GONE);
            no_lists.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            while (data.moveToNext()) {
                listModalClasses.add(new CollectListModalClass(data.getString(1), data.getString(2) + ", " + data.getString(3) +
                        ", " + data.getString(4) + ", " + data.getString(5) + ", " + data.getString(6), data.getString(7), data.getString(8), data.getString(9)
                        , data.getString(10), data.getFloat(11), data.getFloat(12)));
            }
            CollectListAdapter collectListAdapter = new CollectListAdapter(getActivity(), listModalClasses);
            recyclerViewCollectList.setAdapter(collectListAdapter);
            collectListAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void InJson(int code) {
        bar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url1 = ApiUtils.GET_COLETA;
        String url2 = prefernces.getHostUrl() + ApiUtils.GET_COLETA1;
        StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "CollectListDownloadResponse: " + response);
                String s = String.valueOf(response);
                XmlToJson xmlToJson = new XmlToJson.Builder(s).build();
                Log.e("TAG", "XMLtoJson: " + xmlToJson);
                JSONObject s1 = xmlToJson.toJson();
                try {
                    JSONObject object = s1.getJSONObject("coleta");
                    String s9 = object.getString("statusRetorno");
                    if (s9.equals("00")) {
                        String s2 = object.getString("coletaId");
                        String s3 = object.getString("bairro");
                        String s4 = object.getString("numEnd");
                        String s5 = object.getString("cidade");
                        String s6 = object.getString("uf");
                        String s7 = object.getString("cep");
                        String s8 = object.getString("dnaColeta");
                        String s10 = object.getString("clienteId");
                        String s11 = object.getString("contratoId");

                        boolean check = mDatabaseHelper.CheckColetaData(s2);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        if (check) {
                            builder1.setTitle(getResources().getString(R.string.Login_screen1));
                            builder1.setMessage("Coleta " + s2 + " já foi adicionado");
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
                        } else {
                            builder1.setTitle("Sucesso");
                            builder1.setMessage("Coleta " + s2 + " lido com sucesso");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent("qr_code_validate");
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                            });
                            //Creating dialog box
                            AlertDialog alert1 = builder1.create();
                            alert1.show();
                            TableFiveModel tableFiveModel = new TableFiveModel(s2, s3, s4, s5, s6, s7, s8, s10, s11, 0, 0);
                            boolean success = mDatabaseHelper.AddDateToTableFive(tableFiveModel);
                            System.out.println(success);
                        }
                    } else {
                        Toast.makeText(context, "Insira um código de coleta válido", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "onErrorResponse: " + error);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/xml; charset=" +
                        getParamsEncoding();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String body = getXML(code, prefernces.getUserName(), prefernces.getPaso());
                return body.getBytes();
            }
        };
        queue.add(request);
    }

    private String getXML(int code, String userName, String paso) {
        String result = "<consultaColetaXML>\n  <coletaId>" + code + "</coletaId>\n" +
                "<usuario>" + userName + "</usuario>\n" +
                "<senha>" + paso + "</senha>\n </consultaColetaXML>";
        System.out.println(result);
        return result;
    }

    @Override
    public void onRefresh() {
        ColetaLists();
    }

    private class QRCodeValidator extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ColetaLists();
            no_lists.setVisibility(View.GONE);
        }
    }

    private class ListUpdater extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ColetaLists();
            Log.e(TAG, "onReceive: Collect" + intent);
            no_lists.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }

    @Override
    public boolean onBackPressed() {
        super.getActivity().onBackPressed();

        Intent intent = new Intent(getContext(), Collect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.left_to_right, R.animator.right_to_left);
        startActivity(intent);

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}