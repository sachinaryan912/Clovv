package com.company.clovv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.R;
import com.company.clovv.modal.AddressBookModel;

import java.util.ArrayList;

public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.MyViewHolder> {

    Context context;
    ArrayList<AddressBookModel> listAddress;

    public AddressBookAdapter(Context context, ArrayList<AddressBookModel> listAddress) {
        this.context = context;
        this.listAddress = listAddress;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.addressbook_item,parent,false);
        return new AddressBookAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AddressBookModel addressBookModel = listAddress.get(position);
        holder.txtAddress.setText(addressBookModel.getAddress());
        holder.txtLocality.setText(addressBookModel.getLocality());
        holder.txtLongitude.setText(String.valueOf(addressBookModel.getLongitude()));
        holder.txtLatitude.setText(String.valueOf(addressBookModel.getLatitude()));

    }

    @Override
    public int getItemCount() {
        return listAddress.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtAddress,txtLocality,txtLongitude,txtLatitude;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtLocality = itemView.findViewById(R.id.txtLocality);
            txtLongitude = itemView.findViewById(R.id.txtLongitude);
            txtLatitude = itemView.findViewById(R.id.txtLatitude);
        }
    }
}
