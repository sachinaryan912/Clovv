package com.company.clovv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.R;
import com.company.clovv.modal.ShopIdModal;

import java.util.ArrayList;

public class ShopIdAdapter extends RecyclerView.Adapter<ShopIdAdapter.MyViewHolder> {


    Context context;
    ArrayList<ShopIdModal> listId;

    public ShopIdAdapter(Context context, ArrayList<ShopIdModal> listId) {
        this.context = context;
        this.listId = listId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_ids_lay,parent,false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ShopIdModal shopIdModal = listId.get(position);

        holder.txt.setText(shopIdModal.getShopIds());

    }

    @Override
    public int getItemCount() {
        return listId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt = itemView.findViewById(R.id.txtId);
        }
    }
}
