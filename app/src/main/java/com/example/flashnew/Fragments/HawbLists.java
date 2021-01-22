package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flashnew.Adapters.HawbListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.R;

import java.util.ArrayList;


public class HawbLists extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView title;
    private TextView textView;
    private FragmentManager fm;
    private DatabaseHelper mDatabaseHelper;
    private RecyclerView recyclerViewHawbList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<com.example.flashnew.Modals.HawbLists> hawbListsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_hawb_lists, container, false);

        title = view.findViewById(R.id.actionbarTitle);
        mDatabaseHelper = new DatabaseHelper(getContext());
        TextView imei = view.findViewById(R.id.actionbarImei);
        AppPrefernces prefernces = new AppPrefernces(getContext());
        recyclerViewHawbList = view.findViewById(R.id.recyclerViewHawbList);
        title.setText("Lista : " + prefernces.getListID());
        imei.setVisibility(View.GONE);

        ListViewUpdater listViewUpdater = new ListViewUpdater();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listViewUpdater, new IntentFilter("list_view_updater"));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        textView = view.findViewById(R.id.textView);

        fm = getFragmentManager();
        hawbListsArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewHawbList.setLayoutManager(layoutManager);

        ListsView();

        return view;
    }

    private void ListsView() {
        Cursor data = mDatabaseHelper.getData();
        hawbListsArrayList = new ArrayList<>();

        if (data.getCount() == 0) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            Fragment fragment = new List();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

        } else {
            while (data.moveToNext()) {
                hawbListsArrayList.add(new com.example.flashnew.Modals.HawbLists(data.getString(3), data.getString(5),
                        data.getString(10), data.getString(11)));
            }
            HawbListAdapter adapter = new HawbListAdapter(getContext(), hawbListsArrayList);
            recyclerViewHawbList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        ListsView();
    }

    private class ListViewUpdater extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {
            ListsView();
        }
    }
}