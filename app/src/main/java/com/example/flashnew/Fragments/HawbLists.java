package com.example.flashnew.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.Adapters.HawbListAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.flashnew.Server.Utils.TAG;

public class HawbLists extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //implements SwipeRefreshLayout.OnRefreshListener
    private TextView title, imei;
    private ArrayList<com.example.flashnew.Modals.HawbLists> hawbListsArrayList;
    private RecyclerView recyclerViewHawbList;
    private LinearLayoutManager layoutManager;
    private HawbListAdapter adapter;
    private DatabaseHelper mDatabaseHelper;
    private AppPrefernces prefernces;
    private ListViewUpdater listViewUpdater;
    private Landing_Screen context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView textView;
    private FragmentManager fm;


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
        listViewUpdater = new ListViewUpdater();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listViewUpdater, new IntentFilter("list_view_updater"));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        fm = getFragmentManager();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        textView = view.findViewById(R.id.textView);

        ListsView();

        return view;
    }

    private void ListsView() {
        Cursor data = mDatabaseHelper.getData();
        Log.e(TAG, "ListsView: ");
        hawbListsArrayList = new ArrayList<>();

        if (data.getCount() == 0) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
//            Toast.makeText(getContext(), "Sem dados", Toast.LENGTH_SHORT).show();
            Fragment fragment = new List();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else {
            while (data.moveToNext()) {
                hawbListsArrayList.add(new com.example.flashnew.Modals.HawbLists(data.getString(3), data.getString(5),
                        data.getFloat(10), data.getFloat(11), data.getString(12)));
            }
            adapter = new HawbListAdapter(getContext(), hawbListsArrayList);
            Log.e(TAG, "ListsView2: " + hawbListsArrayList);
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
            Log.e(TAG, "ListViewUpdater: ");
            ListsView();
//            assert getFragmentManager() != null;
//            Fragment frg = getFragmentManager ().findFragmentByTag("unique_tag");
//            final FragmentTransaction ft = getFragmentManager ().beginTransaction();
//            assert frg != null;
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();

        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            // Refresh your fragment here
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//            Log.i("IsRefresh", "Yes");
//        }
//    }
}