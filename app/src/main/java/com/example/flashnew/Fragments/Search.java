package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.Adapters.SearchListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.HelperClasses.GetCurrentLocation;
import com.example.flashnew.Modals.SearchListModalClass;
import com.example.flashnew.R;

import java.util.ArrayList;

public class Search extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView no_pesquisa;
    private TextView title, imei;
    private Landing_Screen context;
    private AppPrefernces prefernces;
    private ReseachListUpdater listUpdater;
    private DatabaseHelper mDatabaseHelper;
    private SearchListAdapter searchAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerViewSearchList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<SearchListModalClass> searchListModalClasses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search, container, false);

        prefernces = new AppPrefernces(context);
        imei = view.findViewById(R.id.actionbarImei);
        mDatabaseHelper = new DatabaseHelper(context);
        title = view.findViewById(R.id.actionbarTitle);
        no_pesquisa = view.findViewById(R.id.no_pesquisa);
        swipeRefreshLayout = view.findViewById(R.id.swipe_collect_research);

        title.setText("Pesquisa");
        imei.setText("IMEI : " + prefernces.getIMEI());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        listUpdater = new ReseachListUpdater();
        LocalBroadcastManager.getInstance(context).registerReceiver(listUpdater, new IntentFilter("research_list_update"));

        try {
            GetCurrentLocation.Location(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchListModalClasses = new ArrayList<>();
        recyclerViewSearchList = view.findViewById(R.id.recyclerViewSearchList);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchList.setLayoutManager(layoutManager);

        ResearchList();

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
        this.context = (Landing_Screen) context;
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
