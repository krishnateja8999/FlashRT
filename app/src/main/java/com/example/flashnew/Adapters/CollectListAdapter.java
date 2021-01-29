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

import com.example.flashnew.Fragments.CollectDetails;
import com.example.flashnew.Fragments.SearchSurvey;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.Modals.CollectListModalClass;
import com.example.flashnew.R;

import java.util.ArrayList;

public class CollectListAdapter extends RecyclerView.Adapter<CollectListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<CollectListModalClass> listModalClasses;

    public CollectListAdapter(Context context, ArrayList<CollectListModalClass> listModalClasses) {
        this.context = context;
        this.listModalClasses = listModalClasses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_lists, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.collectID.setText(listModalClasses.get(position).getHawbCode());
        holder.collectAddress.setText(listModalClasses.get(position).getAddress());

        if (listModalClasses.get(position).getTick_mark().equals("false")) {
            holder.tickImageCollect.setImageResource(R.drawable.ic_time);
            holder.collectStart.setEnabled(true);
        } else {
            holder.collectStart.setEnabled(false);
        }

        holder.collectStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new CollectDetails();
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context)
                        .getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("CID", listModalClasses.get(holder.getAdapterPosition()).getHawbCode());
                args.putString("collect_dna", listModalClasses.get(holder.getAdapterPosition()).getDna());
                args.putString("client_id", listModalClasses.get(holder.getAdapterPosition()).getClientID());
                args.putString("contract_id", listModalClasses.get(holder.getAdapterPosition()).getContractID());
                args.putDouble("latitude_collect", listModalClasses.get(holder.getAdapterPosition()).getLatitude());
                args.putDouble("longitude_collect", listModalClasses.get(holder.getAdapterPosition()).getLatitude());
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
        private ImageView tickImageCollect;
        private TextView collectID, collectAddress;
        private Button collectStart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tickImageCollect = itemView.findViewById(R.id.tickImageCollect);
            collectID = itemView.findViewById(R.id.collectID);
            collectAddress = itemView.findViewById(R.id.collectAddress);
            collectStart = itemView.findViewById(R.id.collectStart);
        }
    }
}
