package com.company.clovv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.R;
import com.company.clovv.modal.FavProductModal;

import java.util.ArrayList;

public class FavProductAdapter extends RecyclerView.Adapter<FavProductAdapter.myViewHolder> {
    Context context;
    ArrayList<String> listIds;

    public FavProductAdapter(Context context, ArrayList<String> listIds) {
        this.context = context;
        this.listIds = listIds;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items_fav_product,parent,false);
        return new FavProductAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String s = listIds.get(position);
        holder.txtIds.setText(s);


    }

    @Override
    public int getItemCount() {
        return listIds.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView txtIds;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIds = itemView.findViewById(R.id.ids);
        }
    }
}
