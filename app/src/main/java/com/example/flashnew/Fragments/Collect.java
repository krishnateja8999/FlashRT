package com.example.flashnew.Fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.Activities.ScannerActivity;
import com.example.flashnew.Adapters.CollectListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.Modals.CollectListModalClass;
import com.example.flashnew.Modals.TableFiveModel;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.Utils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.google.zxing.*;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;

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
        mDatabaseHelper = new DatabaseHelper(getContext());
        title.setText("Coletas");
        imei.setText("IMEI : " + prefernces.getIMEI());
        prefernces.setQRCode("null");
        qrCodeValidator = new QRCodeValidator();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(qrCodeValidator, new IntentFilter("qr_code_validate"));
        ValidateQRCode();

        recyclerViewCollectList = view.findViewById(R.id.collectRecyclerView);
        listModalClasses = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCollectList.setLayoutManager(layoutManager);


        listModalClasses.add(new CollectListModalClass("544524", "H.no-7-11, Prakash Nagar, Miyapur. 500050"));
        collectListAdapter = new CollectListAdapter(getActivity(), listModalClasses);
        if (listModalClasses.size() == 0) {
            no_lists.setVisibility(View.VISIBLE);
            Log.e("TAG", "onCreateView: " + listModalClasses.size());
        }
        recyclerViewCollectList.setAdapter(collectListAdapter);


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
                //Utils.DialogClass(getContext(), "Title", "Message", "Ok");
            }
        });

        return view;
    }

    private void DigitalColleta() {
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

    private void ValidateQRCode() {
        String s = prefernces.getQRCode();
        if (s.equals("null")) {

        } else {
            String[] item = s.split("\\|");
            if (item.length < 10) {
                Utils.DialogClass(requireContext(), "Erro", "Inválido", "OK");
            } else {
                Utils.DialogClass(requireContext(), "Sucesso", "Coleta " + item[2] + " lido com sucesso", "OK");
                TableFiveModel tableFiveModel = new TableFiveModel(item[2], item[8], item[9], item[12], item[13], item[14]);
                boolean success = mDatabaseHelper.AddDateToTableFive(tableFiveModel);
                System.out.println(success);
            }
        }
    }

    private class QRCodeValidator extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ValidateQRCode();
        }
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
