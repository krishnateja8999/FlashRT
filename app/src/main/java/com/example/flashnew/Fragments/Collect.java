package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.ScannerActivity;
import com.example.flashnew.Adapters.CollectListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.Modals.CollectListModalClass;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;

import net.skoumal.fragmentback.BackFragment;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Collect extends Fragment implements BackFragment {
    private TextView title, imei;
    private Button coletaDigit, button;
    private ArrayList<CollectListModalClass> listModalClasses;
    private RecyclerView recyclerViewCollectList;
    private LinearLayoutManager layoutManager;
    private CollectListAdapter collectListAdapter;
    private String strtext = "null";
    private QRCodeValidator qrCodeValidator;
    private TextView no_lists;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;
    // private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collect, container, false);
        button = view.findViewById(R.id.coletaScan);
        title = view.findViewById(R.id.actionbarTitle);
        imei = view.findViewById(R.id.actionbarImei);
        prefernces = new AppPrefernces(getContext());
        coletaDigit = view.findViewById(R.id.coletaDigit);
        no_lists = view.findViewById(R.id.no_lists);
        //queue = Volley.newRequestQueue(getContext());
        mDatabaseHelper = new DatabaseHelper(getContext());
        title.setText("Coletas");
        imei.setText("IMEI : " + prefernces.getIMEI());
        qrCodeValidator = new QRCodeValidator();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(qrCodeValidator, new IntentFilter("qr_code_validate"));

        recyclerViewCollectList = view.findViewById(R.id.collectRecyclerView);
        listModalClasses = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCollectList.setLayoutManager(layoutManager);
        ColetaLists();

//        listModalClasses.add(new CollectListModalClass("544524", "H.no-7-11, Prakash Nagar, Miyapur. 500050"));
//        collectListAdapter = new CollectListAdapter(getActivity(), listModalClasses);
//        if (listModalClasses.size() == 0) {
//            no_lists.setVisibility(View.VISIBLE);
//            Log.e("TAG", "onCreateView: " + listModalClasses.size());
//        }
//        recyclerViewCollectList.setAdapter(collectListAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ScannerActivity.class);
                startActivity(i);
            }
        });

        coletaDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DigitalColleta();
            }
        });

        return view;
    }

    private void DigitalColleta() {
        InJson();
        //oldFile();
        // UsingRetrofit();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText edittext = new EditText(getContext());
        edittext.setBackgroundResource(R.drawable.edit_text_border);
        edittext.setPadding(30, 30, 30, 30);
        TextView title = new TextView(getContext());
        title.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.SANS_SERIF);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        title.setPadding(30, 20, 0, 20);
        title.setLayoutParams(lp);
        title.setText("Digite o Codigo da coleta");
        builder.setCustomTitle(title);
        //builder.setTitle("Digite o Codigo da coleta");
        builder.setCancelable(true);
        builder.setView(edittext);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    private class QRCodeValidator extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ColetaLists();
            no_lists.setVisibility(View.GONE);
        }
    }

    private void ColetaLists() {
        Cursor data = mDatabaseHelper.GetDataFromTableFive();
        listModalClasses = new ArrayList<>();
        if (data.getCount() == 0) {
            no_lists.setVisibility(View.VISIBLE);
        } else {
            while (data.moveToNext()) {
                listModalClasses.add(new CollectListModalClass(data.getString(1), data.getString(2) + ", " + data.getString(3) +
                        ", " + data.getString(4) + ", " + data.getString(5) + ", " + data.getString(6), data.getString(7)));
            }
            collectListAdapter = new CollectListAdapter(getActivity(), listModalClasses);
            recyclerViewCollectList.setAdapter(collectListAdapter);
            collectListAdapter.notifyDataSetChanged();
        }
    }

    private void InJson() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final int c = 856846;

        StringRequest request = new StringRequest(Request.Method.POST, ApiUtils.GET_COLETA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "onResponse: " + response);

                InputStream is = convertStringToDocument(response);
                XmlPullParser parser = Xml.newPullParser();
                try {
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(is, null);
                    parser.nextTag();
                    Log.e("TAG", "onResponse2: " + parser);

                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/xml; charset=" +
                        getParamsEncoding();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                String body = getXML(c, prefernces.getUserName(), prefernces.getPaso());
                return body.getBytes();
            }

        };
        queue.add(request);
    }

    private String getXML(int code, String userName, String paso) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<consultaColetaXML>\n  <coletaId>" + code + "</coletaId>\n")
                .append("<usuario>" + userName + "</usuario>\n")
                .append("<senha>" + paso + "</senha>\n </consultaColetaXML>");
        String result = stringBuilder.toString();
        System.out.println(result);
        return result;
    }

    private static InputStream convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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