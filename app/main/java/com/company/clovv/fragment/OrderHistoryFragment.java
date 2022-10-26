package com.company.clovv.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.company.clovv.R;
import com.company.clovv.adapter.OrderHistoryAdapter;
import com.company.clovv.modal.OrderHistoryModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class OrderHistoryFragment extends Fragment {

    public boolean filterFav = false;
    LottieAnimationView filter;
    Dialog filterDialog;

    ArrayList<OrderHistoryModal> listOrders;

    RecyclerView recyclerView;

    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v2 = inflater.inflate(R.layout.fragment_order_history, container, false);

        filter = v2.findViewById(R.id.filterBtn);
        progressBar = v2.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = v2.findViewById(R.id.recViewCustomerHistory);

        filterDialog = new Dialog(getContext());
        filterDialog.setContentView(R.layout.filter_dialog);
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.setCancelable(false);
        filterDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        filterDialog.getWindow().setGravity(Gravity.CENTER);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterFav){
                    filter.playAnimation();
                    filter.setMinAndMaxProgress(0.5f,1.0f);
                    filterFav=false;

                }else {
                    filter.playAnimation();
                    filter.setMinAndMaxProgress(0.0f,0.5f);
                    filterFav=true;

                }

                filterDialog.show();
                filterDialog.findViewById(R.id.close_btn_filter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterDialog.dismiss();
                        if (filterFav){
                            filter.playAnimation();
                            filter.setMinAndMaxProgress(0.5f,1.0f);
                            filterFav=false;

                        }else {
                            filter.playAnimation();
                            filter.setMinAndMaxProgress(0.0f,0.5f);
                            filterFav=true;

                        }
                    }
                });


            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders");

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            listOrders = new ArrayList<>();
            OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(getContext(),listOrders);
            recyclerView.setAdapter(orderHistoryAdapter);


            ref.orderByChild("date").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if (listOrders!=null){
                            listOrders.clear();
                        }
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                            try {
                                String myid = dataSnapshot.child("customerId").getValue().toString();

                                if (myid.equals(mUser.getUid())){
                                    OrderHistoryModal orderHistoryModal = dataSnapshot.getValue(OrderHistoryModal.class);
                                    listOrders.add(orderHistoryModal);

                                }
                            }catch (Exception e){

                            }

                        }

                        Collections.reverse(listOrders);
                        progressBar.setVisibility(View.GONE);

                        orderHistoryAdapter.notifyDataSetChanged();
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            Toast.makeText(getContext(), "Login to see your order history", Toast.LENGTH_SHORT).show();
        }






        return v2;
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}