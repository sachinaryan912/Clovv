package com.company.clovv.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.company.clovv.DBHelper;
import com.company.clovv.GetLocationActivity;
import com.company.clovv.R;
import com.company.clovv.SearchProducts;
import com.company.clovv.adapter.ShopListAdapter;
import com.company.clovv.adapter.SliderAdapter;
import com.company.clovv.modal.ShopListModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private TextView add_line,local_line,txtItems,txtBtnView;
    RelativeLayout set_again,cartBarLay;
    RecyclerView recyclerView;
    DatabaseReference reference;
    ShopListAdapter shopListAdapter;
    ArrayList<ShopListModal> listShop;
    LottieAnimationView search;
    DBHelper helper;
    RelativeLayout r1;
    Spinner spinFilter;


    SliderView sliderView;

    int[] images = {
            R.drawable.spice1,
            R.drawable.spice2,
            R.drawable.spice3
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        r1 = v.findViewById(R.id.vvt);
        cartBarLay = v.findViewById(R.id.hme_cart_bar);
        txtItems = v.findViewById(R.id.num_item);
        txtBtnView = v.findViewById(R.id.v_btn);
        spinFilter = v.findViewById(R.id.spinFilter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.shop_filter, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilter.setAdapter(adapter2);


        helper = new DBHelper(getContext());
        int count = 0;
        Cursor res = helper.getData();
        count = res.getCount();

        if (count!=0){
            cartBarLay.setVisibility(View.VISIBLE);
            txtItems.setText(count + " item in cart");
            txtBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to cart
//                    startActivity(new Intent(getContext(), CartActivity.class));

                }
            });

        }else {
            cartBarLay.setVisibility(View.GONE);
        }

        //end size snack

        reference = FirebaseDatabase.getInstance("https://clovv-e763d-default-rtdb.firebaseio.com/")
                .getReference().child("shopusers");

        add_line = v.findViewById(R.id.add_line1);
        local_line = v.findViewById(R.id.locality_text1);
        set_again = v.findViewById(R.id.set_again_loc);

        sliderView = v.findViewById(R.id.image_slider);
        recyclerView = v.findViewById(R.id.shop_list_recycle_view);
        search = v.findViewById(R.id.search_btn_lot);

        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.playAnimation();
                startActivity(new Intent(getActivity(), SearchProducts.class));

            }
        });

        SharedPreferences sp = v.getContext().getSharedPreferences("usersDeliveryLocation", Context.MODE_PRIVATE);
        String local = sp.getString("Locality","");
        String add = sp.getString("Address","");
        String latitude = sp.getString("lati_tude","");
        String longitude = sp.getString("longi_tude","");

        add_line.setText(add);
        local_line.setText(local);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listShop = new ArrayList<>();
        shopListAdapter = new ShopListAdapter(getContext(),listShop);
        recyclerView.setAdapter(shopListAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (listShop!=null){
                        listShop.clear();
                    }

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ShopListModal shopListModal = dataSnapshot.getValue(ShopListModal.class);
                        listShop.add(shopListModal);

                    }
                    shopListAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getActivity(), "no shop found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        set_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GetLocationActivity.class));

            }
        });




        return v;
    }







}