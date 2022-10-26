package com.company.clovv.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.R;
import com.company.clovv.ShopProductsActivity;
import com.company.clovv.modal.ProductListModal;
import com.company.clovv.modal.ShopListModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ShopListModal> listShop;
    ArrayList<String> list = new ArrayList<String>();

    FirebaseFirestore db;



    public ShopListAdapter(Context context, ArrayList<ShopListModal> listShop) {
        this.context = context;
        this.listShop = listShop;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        db = FirebaseFirestore.getInstance();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item,parent,false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        ShopListModal shopListModal = listShop.get(position);
        holder.txtName.setText(shopListModal.getShop_name());
        holder.txtLocality.setText(shopListModal.getLocality_shop());

        //get user location
        SharedPreferences sp = context.getSharedPreferences("usersDeliveryLocation", Context.MODE_PRIVATE);
        double latitude = Double.parseDouble(sp.getString("lati_tude",""));
        double longitude = Double.parseDouble(sp.getString("longi_tude",""));

        double shopLat = Double.parseDouble(shopListModal.getLatitude_shop());
        double shopLng = Double.parseDouble(shopListModal.getLongitude_shop());

        float results[] = new float[listShop.size()];

        distance(latitude,longitude,shopLat,shopLng);

        holder.txtDistance.setText(String.valueOf(list.get(position)));

        String id_shop = shopListModal.getUid();




        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);

        if (shopListModal.isShopOpen()) {
            final int color = R.color.expireOrder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.itemView.setClickable(false);
                holder.itemView.setEnabled(false);
                holder.txtTime.setText("Closed");

                holder.shopImage.setColorFilter(cf);
                holder.shopImage.setImageAlpha(128);
                holder.txtName.setTextColor(context.getResources().getColor(R.color.gray2));
                holder.txtTime.setTextColor(context.getResources().getColor(R.color.red));
                holder.txtDistance.setVisibility(View.GONE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.itemView.setClickable(true);
                holder.itemView.setEnabled(true);
                holder.txtTime.setText("Open");
                holder.shopImage.setColorFilter(0);
                holder.shopImage.setImageAlpha(255);
                holder.txtName.setTextColor(context.getResources().getColor(R.color.black));
                holder.txtTime.setTextColor(context.getResources().getColor(R.color.green));
                holder.txtDistance.setVisibility(View.VISIBLE);
                holder.itemView.setForeground(null);
            }
        }




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("shopusers")
                                .child(id_shop);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("profileImageUrl").exists()){
                        String image = snapshot.child("profileImageUrl").getValue().toString();
                        Picasso.get().load(image).into(holder.shopImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        db.collection("Products").whereEqualTo("productOwnerId",id_shop)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        if (value != null){
                            for (DocumentChange dc:value.getDocumentChanges()){
                                if (dc.getDocument().exists()){
                                    if (dc.getType()==DocumentChange.Type.ADDED){



                                        if (dc != null){
                                            String type = dc.getDocument().getString("shopType");

                                            if (type.equals("Mill")){
                                                holder.mill.setVisibility(View.VISIBLE);
                                            }
                                            if (type.equals("Dairy")){
                                                holder.dairy.setVisibility(View.VISIBLE);
                                            }
                                            if (type.equals("Kirana")){
                                                holder.kirana.setVisibility(View.VISIBLE);
                                            }

                                            if (type.equals("Mill")&&type.equals("Dairy")){

                                                holder.mill.setVisibility(View.VISIBLE);
                                                holder.dairy.setVisibility(View.VISIBLE);

                                            }

                                            if (type.equals("Kirana")&&type.equals("Dairy")){

                                                holder.kirana.setVisibility(View.VISIBLE);
                                                holder.dairy.setVisibility(View.VISIBLE);

                                            }

                                            if (type.equals("Mill")&&type.equals("Kirana")){
                                                holder.kirana.setVisibility(View.VISIBLE);
                                                holder.mill.setVisibility(View.VISIBLE);

                                            }

                                            if (type.equals("Mill")&&type.equals("Kirana")&&type.equals("Dairy")){
                                                holder.kirana.setVisibility(View.VISIBLE);
                                                holder.mill.setVisibility(View.VISIBLE);
                                                holder.dairy.setVisibility(View.VISIBLE);

                                            }

                                        }




                                    }
                                }else {
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopProductsActivity.class);
                intent.putExtra("shopName",shopListModal.getShop_name());
                intent.putExtra("shopLocality",shopListModal.getLocality_shop());
                intent.putExtra("shopId",shopListModal.getUid());
                intent.putExtra("shopDistance",list.get(position));
                context.startActivity(intent);

            }
        });

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = (dist/0.6214);
        if (dist<1){
            dist = dist*1000;
            int distance = (int) dist;

            list.add(distance+"m");

        }else {
            int distance = (int) dist;

            list.add(distance+" KM");
        }

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {


        return listShop.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName,txtLocality,txtDistance;
        CardView dairy,mill,kirana;
        ImageView shopImage;
        RelativeLayout r_lay;
        TextView txtTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.shop_name);
            txtLocality = itemView.findViewById(R.id.shop_locality);
            txtDistance = itemView.findViewById(R.id.distance_data);
            shopImage = itemView.findViewById(R.id.shopImageProfile);

            dairy = itemView.findViewById(R.id.dairy_icon);
            mill = itemView.findViewById(R.id.mill_icon);
            kirana = itemView.findViewById(R.id.kirana_icon);
//            r_lay = itemView.findViewById(R.id.disable_lay);
            txtTime = itemView.findViewById(R.id.timeVal);
        }
    }
}
