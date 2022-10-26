package com.company.clovv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.R;
import com.company.clovv.SearchProducts;
import com.company.clovv.ShopProductsActivity;
import com.company.clovv.modal.SearchProductModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<SearchProductModal> searchProductModalArrayList;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;


    public SearchProductAdapter(Context context, ArrayList<SearchProductModal> searchProductModalArrayList) {
        this.context = context;
        this.searchProductModalArrayList = searchProductModalArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_product,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        SearchProductModal searchProductModal = searchProductModalArrayList.get(position);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("shopusers").child(searchProductModal.getProductOwnerId());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shopName = snapshot.child("shop_name").getValue().toString();

                    holder.txtShop.setText(shopName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        if (searchProductModal.getProductImageUrl()!=null){

            Picasso.get().load(searchProductModal.getProductImageUrl()).into(holder.imgProduct);

        }


        holder.txtName.setText(searchProductModal.getProductName());
        holder.txtPrice.setText(context.getResources().getString(R.string.rs)+" "+searchProductModal.getProductPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopProductsActivity.class);
                intent.putExtra("shopId",searchProductModal.getProductOwnerId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return searchProductModalArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName,txtShop,txtPrice;
        ImageView imgProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.proName);
            txtShop = itemView.findViewById(R.id.proShop);
            imgProduct = itemView.findViewById(R.id.productImageSearch);
            txtPrice = itemView.findViewById(R.id.proPrice);

        }
    }
}
