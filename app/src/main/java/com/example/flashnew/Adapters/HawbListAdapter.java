package com.example.flashnew.Adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashnew.Modals.HawbLists;
import com.example.flashnew.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HawbListAdapter extends RecyclerView.Adapter<HawbListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<HawbLists> hawbLists;

    public HawbListAdapter(Context context, ArrayList<HawbLists> hawbLists) {
        this.context = context;
        this.hawbLists = hawbLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hawb_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Hcode.setText(hawbLists.get(position).getHawbCode());
        holder.name.setText(hawbLists.get(position).getName());

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(hawbLists.get(position).getLatitude(), hawbLists.get(position).getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            holder.address.setText(address);
            holder.city.setText(city);
            holder.postalCode.setText(postalCode);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return hawbLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView Hcode;
        private TextView address, city, name, postalCode;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Hcode = itemView.findViewById(R.id.hawbCode12345);
            address = itemView.findViewById(R.id.address_hawb_list);
            city = itemView.findViewById(R.id.city_hawb_list);
            name = itemView.findViewById(R.id.name_hawb_list);
            postalCode = itemView.findViewById(R.id.pincode_hawb_List);

        }
    }
}
