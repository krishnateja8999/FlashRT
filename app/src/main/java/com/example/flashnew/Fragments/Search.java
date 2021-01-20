package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.flashnew.Adapters.SearchListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.Modals.SearchListModalClass;
import com.example.flashnew.R;

import java.util.ArrayList;

public class Search extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private TextView title, imei;
    private ArrayList<SearchListModalClass> searchListModalClasses;
    private RecyclerView recyclerViewSearchList;
    private LinearLayoutManager layoutManager;
    private SearchListAdapter searchAdapter;
    private Context context;
    private AppPrefernces prefernces;
    private DatabaseHelper mDatabaseHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView no_pesquisa;
    private ReseachListUpdater listUpdater;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search, container, false);
        title = view.findViewById(R.id.actionbarTitle);
        imei = view.findViewById(R.id.actionbarImei);
        prefernces = new AppPrefernces(context);
        title.setText("Pesquisa");
        imei.setText("IMEI: " + prefernces.getIMEI());
        mDatabaseHelper = new DatabaseHelper(context);
        swipeRefreshLayout = view.findViewById(R.id.swipe_collect_research);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        no_pesquisa = view.findViewById(R.id.no_pesquisa);
        listUpdater = new ReseachListUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listUpdater, new IntentFilter("research_list_update"));
        searchListModalClasses = new ArrayList<>();
        recyclerViewSearchList = view.findViewById(R.id.recyclerViewSearchList);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchList.setLayoutManager(layoutManager);
        ResearchList();

//        searchListModalClasses.add(new SearchListModalClass("sdf","Augusto Vasquez", "PRAJA DO CERIO 21 VILA PAULISTA, SAO PAULO, SP","false", "sdfdg", "41656", "research"));
//        searchAdapter = new SearchListAdapter(getActivity(), searchListModalClasses);
//        recyclerViewSearchList.setAdapter(searchAdapter);

        return view;
    }

    private void ResearchList() {
        Cursor data = mDatabaseHelper.GetResearchList();
        searchListModalClasses = new ArrayList<>();
        if (data.getCount() == 0) {
            swipeRefreshLayout.setVisibility(View.GONE);
            no_pesquisa.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            while (data.moveToNext()) {
                searchListModalClasses.add(new SearchListModalClass(data.getString(1), data.getString(3),
                        data.getString(5) + ", " + data.getString(6) + ", " + data.getString(7) + ", " + data.getString(8) +
                                ", " + data.getString(9) + ", " + data.getString(10), data.getString(11), data.getString(12), data.getString(13), data.getString(2), data.getString(14)));
                searchAdapter = new SearchListAdapter(getActivity(), searchListModalClasses);
                recyclerViewSearchList.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onRefresh() {
        ResearchList();
    }

    private class ReseachListUpdater extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ResearchList();
            no_pesquisa.setVisibility(View.VISIBLE);
            Log.i("TAG", "onReceiveResearchList: Receiving");
        }
    }
}
