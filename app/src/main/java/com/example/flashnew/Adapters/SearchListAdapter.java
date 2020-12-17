package com.example.flashnew.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashnew.Fragments.HawbLists;
import com.example.flashnew.Fragments.List;
import com.example.flashnew.Fragments.SearchSurvey;
import com.example.flashnew.Modals.SearchListModalClass;
import com.example.flashnew.R;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SearchListModalClass> listModalClasses;


    public SearchListAdapter(Context context, ArrayList<SearchListModalClass> listModalClasses) {
        this.context = context;
        this.listModalClasses = listModalClasses;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_view, parent, false);
        return new SearchListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.searchName.setText(listModalClasses.get(position).getName());
        holder.searchAddress.setText(listModalClasses.get(position).getAddress());

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context)
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, new SearchSurvey());
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listModalClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView searchName;
        private TextView searchAddress;
        private Button start;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            searchName = itemView.findViewById(R.id.search_name);
            searchAddress = itemView.findViewById(R.id.search_address);
            start = itemView.findViewById(R.id.start);

        }
    }
}
