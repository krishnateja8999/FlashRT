package com.example.flashnew.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.searchName.setText(listModalClasses.get(position).getName());
        holder.searchAddress.setText(listModalClasses.get(position).getAddress());

        if (listModalClasses.get(position).getTick_mark().equals("true")) {
            holder.img_research.setImageResource(R.drawable.ic_right);
            holder.start.setEnabled(false);
        } else {
            holder.start.setEnabled(true);
        }

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new SearchSurvey();
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context)
                        .getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("Research", listModalClasses.get(holder.getAdapterPosition()).getHawbCode());
                args.putString("Customer_code", listModalClasses.get(holder.getAdapterPosition()).getCustomerID());
                args.putString("Contract_code", listModalClasses.get(holder.getAdapterPosition()).getClientID());
                args.putString("Client_name", listModalClasses.get(holder.getAdapterPosition()).getClientName());
                fr.setArguments(args);
                fragmentTransaction.replace(R.id.content, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listModalClasses.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView searchName;
        private TextView searchAddress, hawbCode;
        private Button start;
        private ImageView img_research;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            searchName = itemView.findViewById(R.id.search_name);
            searchAddress = itemView.findViewById(R.id.search_address);
            hawbCode = itemView.findViewById(R.id.hawb_code);
            start = itemView.findViewById(R.id.start);
            img_research = itemView.findViewById(R.id.img_research);

        }
    }
}
