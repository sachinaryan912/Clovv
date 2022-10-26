package com.company.clovv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.R;
import com.company.clovv.ShopProductsActivity;
import com.company.clovv.modal.SearchProductModal;
import com.company.clovv.modal.SearchShopModal;

import java.util.ArrayList;

public class SearchShopAdapter extends RecyclerView.Adapter<SearchShopAdapter.MyViewHolder> {

    Context context;
    ArrayList<SearchShopModal> shopNameList;


    public SearchShopAdapter(Context context, ArrayList<SearchShopModal> shopNameList) {
        this.context = context;
        this.shopNameList = shopNameList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_shop_items,parent,false);
        return new SearchShopAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SearchShopModal searchShopModal = shopNameList.get(position);

        holder.nameData.setText(searchShopModal.getShop_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopProductsActivity.class);
                intent.putExtra("shopId",searchShopModal.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return shopNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameData;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameData = itemView.findViewById(R.id.shopNameSearchData);
        }
    }
}
