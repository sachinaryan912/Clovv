package com.company.clovv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.HistoryOrderDetails;
import com.company.clovv.R;
import com.company.clovv.modal.OrderHistoryModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>{

    Context context;
    ArrayList<OrderHistoryModal> listOrders;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference reference;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryModal> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_order_items,parent,false);
        return new OrderHistoryAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        OrderHistoryModal orderHistoryModal = listOrders.get(position);
        holder.txtId.setText(orderHistoryModal.getOrderId());
        //format date
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        String today = sdf.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -2);
        String yesterday = sdf.format(cal.getTime());
        String twoDaysAgo = sdf.format(cal1.getTime());

        if (orderHistoryModal.getOrderDate().equals(today)){
            holder.txtDate.setText("Today");
        }else if (orderHistoryModal.getOrderDate().equals(yesterday)){
            holder.txtDate.setText("Yesterday");
        }else if (orderHistoryModal.getOrderDate().equals(twoDaysAgo)){
            holder.txtDate.setText("2 days ago");
        }else {
            holder.txtDate.setText(orderHistoryModal.getOrderDate());
        }


        //get shop name
        reference = FirebaseDatabase.getInstance().getReference()
                .child("shopusers").child(orderHistoryModal.getShopId());

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

            }
        });



        holder.txtItem.setText("items: "+orderHistoryModal.getItemOrder());

//        check order status



        if (orderHistoryModal.isAccepted() && !orderHistoryModal.isCanceled() && !orderHistoryModal.isOutForDelivery()
        && !orderHistoryModal.isOrderExpired() && !orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Order Accepted");
            holder.orderStatusLay.setBackgroundResource(R.drawable.accepted_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.acceptedOrder));
        }

        if (!orderHistoryModal.isAccepted() && orderHistoryModal.isCanceled() && !orderHistoryModal.isOutForDelivery()
                && !orderHistoryModal.isOrderExpired() && !orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Order Cancelled");
            holder.orderStatusLay.setBackgroundResource(R.drawable.cancel_order_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.cancelOrder));
        }

        if (orderHistoryModal.isAccepted() && orderHistoryModal.isCanceled() && !orderHistoryModal.isOutForDelivery()
                && !orderHistoryModal.isOrderExpired() && !orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Order Cancelled");
            holder.orderStatusLay.setBackgroundResource(R.drawable.cancel_order_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.cancelOrder));
        }

        if (orderHistoryModal.isAccepted() && !orderHistoryModal.isCanceled() && orderHistoryModal.isOutForDelivery()
                && !orderHistoryModal.isOrderExpired() && !orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Out For Delivery");
            holder.orderStatusLay.setBackgroundResource(R.drawable.out_delivery_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.deliveryOrder));
        }

        if (orderHistoryModal.isAccepted() && !orderHistoryModal.isCanceled() && orderHistoryModal.isOutForDelivery()
                && !orderHistoryModal.isOrderExpired() && orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Order Success");
            holder.orderStatusLay.setBackgroundResource(R.drawable.success_order_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.successOrder));
        }

        if (!orderHistoryModal.isAccepted() && orderHistoryModal.isCanceled() && !orderHistoryModal.isOutForDelivery()
                && orderHistoryModal.isOrderExpired() && !orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Order Expire");
            holder.orderStatusLay.setBackgroundResource(R.drawable.expire_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.expireOrder));
        }

        if (orderHistoryModal.isAccepted() && orderHistoryModal.isCanceled() && !orderHistoryModal.isOutForDelivery()
                && orderHistoryModal.isOrderExpired() && !orderHistoryModal.isOrderSuccess()){
            holder.txtOrderStatus.setText("Order Expire");
            holder.orderStatusLay.setBackgroundResource(R.drawable.expire_tag);
            holder.txtOrderStatus.setTextColor(context.getResources().getColor(R.color.expireOrder));
        }





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //got to details
                Intent intent = new Intent(context, HistoryOrderDetails.class);
                intent.putExtra("orderId",orderHistoryModal.getOrderId());
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtId,txtPrice,txtDate,txtTime,txtShop,txtItem,txtOrderStatus;
        LinearLayout orderStatusLay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.orderIdHistory);
//            txtPrice = itemView.findViewById(R.id.orderPriceHistory);
            txtDate = itemView.findViewById(R.id.orderDateHistory);
            txtShop = itemView.findViewById(R.id.orderShopHistory);
            txtItem = itemView.findViewById(R.id.orderItemHistory);
            orderStatusLay = itemView.findViewById(R.id.orderStatusBg);
            txtOrderStatus = itemView.findViewById(R.id.orderStatusText);
//            txtTime = itemView.findViewById(R.id.orderTimeHistory);


        }
    }
}
