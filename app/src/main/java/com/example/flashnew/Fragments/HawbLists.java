package com.example.flashnew.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.Adapters.HawbListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.R;

import java.util.ArrayList;

public class HawbLists extends Fragment {
    private TextView title, imei;
    private ArrayList<com.example.flashnew.Modals.HawbLists> hawbListsArrayList;
    private RecyclerView recyclerViewHawbList;
    private LinearLayoutManager layoutManager;
    private HawbListAdapter adapter;
    private DatabaseHelper mDatabaseHelper;
    private AppPrefernces prefernces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_hawb_lists, container, false);
        title = view.findViewById(R.id.actionbarTitle);
        imei = view.findViewById(R.id.actionbarImei);
        hawbListsArrayList = new ArrayList<>();
        recyclerViewHawbList = view.findViewById(R.id.recyclerViewHawbList);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewHawbList.setLayoutManager(layoutManager);
        mDatabaseHelper = new DatabaseHelper(getContext());
        prefernces = new AppPrefernces(getContext());
        imei.setVisibility(View.GONE);
        title.setText("Lista : " + prefernces.getListID());

        Cursor data = mDatabaseHelper.getData();

        if (data.getCount() == 0) {
            Toast.makeText(getContext(), "Sem dados", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                hawbListsArrayList.add(new com.example.flashnew.Modals.HawbLists(data.getString(3), data.getString(5),
                        data.getFloat(10), data.getFloat(11)));
            }
            adapter = new HawbListAdapter(getContext(), hawbListsArrayList);
            recyclerViewHawbList.setAdapter(adapter);
        }
        return view;
    }
}