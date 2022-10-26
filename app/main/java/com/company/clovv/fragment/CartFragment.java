package com.company.clovv.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.DBHelper;
import com.company.clovv.Dashboard;
import com.company.clovv.Interface.ChangeNumberItemListener;
import com.company.clovv.LoginActivity;
import com.company.clovv.OrderPlacedActivity;
import com.company.clovv.PaymentPage;
import com.company.clovv.R;
import com.company.clovv.ShopProductsActivity;
import com.company.clovv.adapter.CartItemAdapter;
import com.company.clovv.modal.SendOrderDetailsModal;
import com.company.clovv.modal.SentProductsModal;
import com.company.clovv.modal.SqLiteModalCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    DBHelper helper;
    ArrayList<SqLiteModalCart> listCart;
    CartItemAdapter adapter;
    LinearLayout l1;
    RelativeLayout l2;
    TextView size,shopName,subtotal,totalPrice,totalPrice2,txtLocation,btnChange,distData;
    public String id = "";
    FirebaseFirestore firebaseFirestore;
    double sum=0;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button checkout,startShopping;
    String isBtnDisabled="";
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog mDialog,timerDialog;
    CardView addMore;
    double latitude,longitude,totalPriceData;
    int codeLength= 5;
    SendOrderDetailsModal sendOrderDetailsModal;
    String add,distu,modyPay,sizeData;
    DatabaseReference reference;
    boolean isTimerRuningPlace = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    TextView txtCharges;
    ProgressDialog progressDialog,placingOrder;
    ProgressBar pBar;
    TextView txtTimeData;
    CountDownTimer timer;
    int counter = 60;
    String OrdersId = "";
    String ShopsId = "";
    Dialog cancelDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_cart, container, false);



        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("getting data ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        placingOrder = new ProgressDialog(getContext());
        placingOrder.setCancelable(false);
        placingOrder.setMessage("Placing your order ...");
        placingOrder.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        timerDialog = new Dialog(getContext());
        timerDialog.setContentView(R.layout.btn_place_order_timer);
        timerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        timerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timerDialog.setCancelable(false);
        timerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        timerDialog.getWindow().setGravity(Gravity.BOTTOM);


        cancelDialog = new Dialog(getContext());
        cancelDialog.setContentView(R.layout.cancel_order_dialog);
        cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelDialog.setCancelable(false);
        cancelDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        cancelDialog.getWindow().setGravity(Gravity.CENTER);

        pBar = timerDialog.findViewById(R.id.progressBar_place);
        txtTimeData = timerDialog.findViewById(R.id.time_dialog_place);

        pBar.setMax(counter);






        sharedPreferences = getContext().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();


        recyclerView = mView.findViewById(R.id.rec_cart_view);
        l1 = mView.findViewById(R.id.emp_lay_cart);
        l2 = mView.findViewById(R.id.lay_items_in_cart);
        size = mView.findViewById(R.id.size_list);
        helper = new DBHelper(getContext());
        shopName = mView.findViewById(R.id.shopName);
        subtotal = mView.findViewById(R.id.subTotal);
        totalPrice = mView.findViewById(R.id.totalPrice);
        totalPrice2 = mView.findViewById(R.id.totalPrice2);
        checkout = mView.findViewById(R.id.checkout_btn);
        txtLocation = mView.findViewById(R.id.delivery_location_cart);
        btnChange = mView.findViewById(R.id.change_location);
        startShopping = mView.findViewById(R.id.btnStartShopping);
        addMore = mView.findViewById(R.id.card3);
        distData = mView.findViewById(R.id.distData);
        txtCharges = mView.findViewById(R.id.deliveryChargeTxt);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();


        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Dashboard.class));
            }
        });

        SharedPreferences sp = mView.getContext().getSharedPreferences("usersDeliveryLocation", MODE_PRIVATE);
        String local = sp.getString("Locality","");
        add = sp.getString("Address","");
        latitude = Double.parseDouble(sp.getString("lati_tude",""));
        longitude = Double.parseDouble(sp.getString("longi_tude",""));

        txtLocation.setText(add);


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //working
                Toast.makeText(getContext(), "not available", Toast.LENGTH_SHORT).show();
            }
        });


        checkout.setBackground(getContext().getResources().getDrawable(R.drawable.disable_btn));

        //create new array data creating new objects
        listCart = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartItemAdapter(getContext(), listCart, new ChangeNumberItemListener() {
            @Override
            public void change() {
                calculation();

            }
        });
        recyclerView.setAdapter(adapter);

        radioGroup = (RadioGroup) mView.findViewById(R.id.radio);





        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = group.getCheckedRadioButtonId();
                if (checkedId == -1){

                    isBtnDisabled = "shanu";
                    checkout.setBackground(getContext().getResources().getDrawable(R.drawable.disable_btn));



                }else {

                    isBtnDisabled = "sachin";
                    checkout.setBackground(getContext().getResources().getDrawable(R.drawable.btn_main));
                    // find the radiobutton by returned id
                    radioButton = (RadioButton) mView.findViewById(checkedId);
                    modyPay = radioButton.getText().toString();



                }

            }
        });




        Cursor res = helper.getData();
        if (res.getCount()==0){
            Toast.makeText(getContext(), "no item in your cart!", Toast.LENGTH_SHORT).show();
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);

        }else {
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.VISIBLE);
            progressDialog.show();
            int productNameIndex = res.getColumnIndex("productName");
            int productPricing = res.getColumnIndex("productPrice");
            int idIndex = res.getColumnIndex("shopIdData");
            int quantityIndex = res.getColumnIndex("qValData");
            int userWtIndex = res.getColumnIndex("userWtType");
            int productTypeIndex = res.getColumnIndex("productType");
            int productUnitIndex = res.getColumnIndex("productUnitPrice");
            int shopPerWtTypeIndex = res.getColumnIndex("shopPerWtType");
            int productIdIndex = res.getColumnIndex("productId");
            int productStockWtTypeIndex = res.getColumnIndex("productStockWtType");
            int productTotalStockIndex = res.getColumnIndex("totalStock");

            while (res.moveToNext()){
                id = res.getString(idIndex);
                String proName = res.getString(productNameIndex);
                String proPrice = res.getString(productPricing);
                String quantity = res.getString(quantityIndex);
                String userWeight = res.getString(userWtIndex);
                String proType = res.getString(productTypeIndex);
                String proUnitPrice = res.getString(productUnitIndex);
                String shopPerWtType = res.getString(shopPerWtTypeIndex);
                String proId = res.getString(productIdIndex);
                String proWtType = res.getString(productStockWtTypeIndex);
                double pData = Double.parseDouble(proPrice);

                sum = sum+pData;


                firebaseFirestore.collection("Products").document(proId)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().exists()){
                                    String proTotalStock = task.getResult().getString("productItemAvailable");

                                    SqLiteModalCart sqLiteModalCart = new SqLiteModalCart(proName,proPrice,quantity,
                                            userWeight,proType,proUnitPrice,shopPerWtType,proId,proWtType,proTotalStock);
                                    listCart.add(sqLiteModalCart);
                                    size.setText("Total item : "+String.valueOf(listCart.size()));

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

                adapter.notifyDataSetChanged();







            }

            getDataIdName(id);
            String sums = String.format("%.2f", sum);
            subtotal.setText("Subtotal : "+getContext().getResources().getString(R.string.rs)+ sums);
            sizeData = String.valueOf(res.getCount());
            String s1 = sharedPreferences.getString("distanceValue", "");
            double disVal = 0;

            if (s1.equals("")){
                getDataIdName(id);
            }else {
                txtCharges.setText(s1);

                disVal = Double.parseDouble(s1);

            }





            //check distance
            double finalDisVal = disVal;
            reference.child("shopusers").child(id).child("deliveryCharge")
                    .child("case1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String disCase1 = snapshot.child("deliveryDistance").getValue().toString();
                                String amountCase1 = snapshot.child("orderPrice").getValue().toString();
                                if (finalDisVal >Integer.valueOf(disCase1) && sum>Integer.valueOf(amountCase1)){

                                        reference.child("shopusers").child(id).child("deliveryCharge").child("case1").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                    txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+delPrice);
                                                    totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+Integer.valueOf(delPrice))));
                                                    totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                    totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                    progressDialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                }else{
                                    reference.child("shopusers").child(id).child("deliveryCharge")
                                            .child("case2").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        String disCase2 = snapshot.child("deliveryDistance").getValue().toString();
                                                        String amountCase2 = snapshot.child("orderPrice").getValue().toString();

                                                        if (finalDisVal >Integer.valueOf(disCase2) && sum<Integer.valueOf(amountCase2)){

                                                            reference.child("shopusers").child(id).child("deliveryCharge").child("case2").addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()){
                                                                        String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                                        double updateDeliveryCharge1 = Double.parseDouble(String.format("%.2f",Integer.parseInt(delPrice)* finalDisVal));
                                                                        txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+updateDeliveryCharge1);
                                                                        totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+updateDeliveryCharge1)));
                                                                        totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                        totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });


                                                            progressDialog.dismiss();

                                                        }else{
                                                            reference.child("shopusers").child(id).child("deliveryCharge")
                                                                    .child("case3").addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if (snapshot.exists()){

                                                                                String disCase2 = snapshot.child("deliveryDistance").getValue().toString();
                                                                                String amountCase2 = snapshot.child("orderPrice").getValue().toString();

                                                                                if (finalDisVal <Integer.valueOf(disCase2) && sum>Integer.valueOf(amountCase2)){

                                                                                    reference.child("shopusers").child(id).child("deliveryCharge").child("case3").addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            if (snapshot.exists()){
                                                                                                String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                                                                txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+delPrice);
                                                                                                totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+Integer.valueOf(delPrice))));
                                                                                                totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                progressDialog.dismiss();
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });


                                                                                    progressDialog.dismiss();
                                                                                }else {
                                                                                    reference.child("shopusers").child(id).child("deliveryCharge")
                                                                                            .child("case4").addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    if (snapshot.exists()){

                                                                                                        String disCase2 = snapshot.child("deliveryDistance").getValue().toString();
                                                                                                        String amountCase2 = snapshot.child("orderPrice").getValue().toString();

                                                                                                        if (finalDisVal <Integer.valueOf(disCase2) && sum<Integer.valueOf(amountCase2)){
                                                                                                            reference.child("shopusers").child(id).child("deliveryCharge").child("case4").addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                    if (snapshot.exists()){
                                                                                                                        String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                                                                                        txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+delPrice);
                                                                                                                        totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+Integer.valueOf(delPrice))));
                                                                                                                        totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                                        totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                                        progressDialog.dismiss();
                                                                                                                    }
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                }
                                                                                                            });

                                                                                                            progressDialog.dismiss();

                                                                                                        }

                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                }

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });


                                                        }


                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






            addMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ShopProductsActivity.class);
                    intent.putExtra("shopId",id);
                    startActivity(intent);

                }
            });

        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check shop is open or not
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("shopusers")
                        .child(id);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            boolean openShop = Boolean.parseBoolean(snapshot.child("shopOpen").getValue().toString());

                            if (openShop){
                                Toast.makeText(getContext(), "shop closed", Toast.LENGTH_SHORT).show();

                            }else {


                                if (isBtnDisabled.equals("sachin")){

                                    //check for login & logout

                                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                                        currentUser = mAuth.getCurrentUser();
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("customers").child(currentUser.getUid());

                                        placingOrder.show();

                                        //unique product code
                                        char[] chars = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();
                                        StringBuilder sb = new StringBuilder();
                                        Random random = new SecureRandom();

                                        for (int i = 0; i < codeLength; i++) {
                                            char c = chars[random.nextInt(chars.length)];
                                            sb.append(c);
                                        }

                                        String orderId = "CLOVV"+sb.toString().trim();

                                        OrdersId = orderId;
                                        ShopsId = id;


                                        DatabaseReference ref = FirebaseDatabase.getInstance("https://clovv-e763d-default-rtdb.firebaseio.com/")
                                                .getReference().child("Orders").child(orderId);

                                        DatabaseReference refData = FirebaseDatabase.getInstance("https://clovv-e763d-default-rtdb.firebaseio.com/")
                                                .getReference().child("Orders").child(orderId).child("productList");






                                        String TotalPrice = String.valueOf(totalPriceData);
                                        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                                        String orderDate = sdf.format(new Date());

                                        SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm a",Locale.getDefault());
                                        String orderTime = sdf2.format(new Date());

                                        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z",Locale.getDefault());
                                        String currentDateandTime = sdf3.format(new Date());



                                        db.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    String customerName = snapshot.child("name").getValue().toString();
                                                    String custPhone = snapshot.child("phone").getValue().toString();
                                                    sendOrderDetailsModal = new SendOrderDetailsModal(customerName,custPhone,
                                                            add,distu,modyPay,orderId,TotalPrice,sizeData,orderDate,
                                                            orderTime,"none",id,currentUser.getUid(),currentDateandTime, false, false,false,
                                                            false,false,false,false);
                                                    ref.setValue(sendOrderDetailsModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            //getsqlitedata

                                                            Cursor res = helper.getData();
                                                            if (res.getCount()==0){
                                                                Toast.makeText(getContext(), "no item in your cart!", Toast.LENGTH_SHORT).show();

                                                            }else {
                                                                int productNameIndex = res.getColumnIndex("productName");
                                                                int productPricing = res.getColumnIndex("productPrice");
                                                                int quantityIndex = res.getColumnIndex("qValData");
                                                                int userWtIndex = res.getColumnIndex("userWtType");
                                                                int productUnitIndex = res.getColumnIndex("productUnitPrice");
                                                                int productIdIndex = res.getColumnIndex("productId");
                                                                int productTypeIndex = res.getColumnIndex("productType");

                                                                while (res.moveToNext()) {
                                                                    String proName = res.getString(productNameIndex);
                                                                    String proPrice = res.getString(productPricing);
                                                                    String quantity = res.getString(quantityIndex);
                                                                    String userWeight = res.getString(userWtIndex);
                                                                    String proUnitPrice = res.getString(productUnitIndex);
                                                                    String proId = res.getString(productIdIndex);
                                                                    double pData = Double.parseDouble(proPrice);
                                                                    String proType = res.getString(productTypeIndex);

                                                                    String amount = String.format("%.2f",pData);

                                                                    SentProductsModal sentProductsModal = new SentProductsModal(proId,proName,quantity,proUnitPrice,amount,proType,userWeight);

                                                                    refData.child(proId).setValue(sentProductsModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                //for admin
                                                                                placingOrder.dismiss();

                                                                                //timer
                                                                                timerDialog.show();
                                                                                countDown();
                                                                                ref.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        if (snapshot.exists()){
                                                                                            boolean accepted = Boolean.parseBoolean(snapshot.child("accepted").getValue().toString());
                                                                                            boolean cancelled = Boolean.parseBoolean(snapshot.child("canceled").getValue().toString());
                                                                                            boolean outForDelivery1 = Boolean.parseBoolean(snapshot.child("outForDelivery").getValue().toString());
                                                                                            String mode = snapshot.child("modePay").getValue().toString();
                                                                                            String price = snapshot.child("priceOrder").getValue().toString();


                                                                                            if (accepted && !outForDelivery1){
                                                                                                if (mode.equals("Cash On Delivery")){

                                                                                                    if (timer!=null){
                                                                                                        timer.cancel();
                                                                                                        timer=null;
                                                                                                    }
                                                                                                    timerDialog.dismiss();

                                                                                                    Intent i = new Intent(getActivity(),OrderPlacedActivity.class);
                                                                                                    startActivity(i);
                                                                                                }

                                                                                                if (mode.equals("Online Payment")) {
                                                                                                    if (timer != null) {
                                                                                                        timer.cancel();
                                                                                                        timer = null;
                                                                                                    }
                                                                                                    Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

                                                                                                    timerDialog.dismiss();
                                                                                                    Intent intent = new Intent(getContext(), PaymentPage.class);
                                                                                                    intent.putExtra("orderId", orderId);
                                                                                                    intent.putExtra("orderPrice", price);
                                                                                                    startActivity(intent);

                                                                                                }


                                                                                            }





                                                                                            if (cancelled){

                                                                                                timerDialog.dismiss();
                                                                                                if (timer!=null){
                                                                                                    timer.cancel();
                                                                                                    timer=null;
                                                                                                }

                                                                                                isTimerRuningPlace = false;


                                                                                            }


                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });

                                                                                timerDialog.findViewById(R.id.cancelPlacingBtn).setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {


                                                                                        cancelDialog.show();

                                                                                        cancelDialog.findViewById(R.id.no_order_btn).setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                cancelDialog.dismiss();
                                                                                            }
                                                                                        });

                                                                                        cancelDialog.findViewById(R.id.cancel_order_btn).setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                timerDialog.dismiss();
                                                                                                cancelDialog.dismiss();
                                                                                                //cancel order
                                                                                                ref.child("canceled").setValue(true);
                                                                                                ref.child("cancelledBy").setValue("customer");

                                                                                                //stop timer
                                                                                                if (timer!=null){
                                                                                                    timer.cancel();
                                                                                                    timer=null;
                                                                                                }

                                                                                                //make timer false
                                                                                                isTimerRuningPlace=false;
                                                                                                Toast.makeText(getContext(), "Order Cancelled by you", Toast.LENGTH_SHORT).show();


                                                                                            }
                                                                                        });


                                                                                    }
                                                                                });


                                                                                //end timer

                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });




                                                                }
                                                            }



                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });







                                    }else {

                                        placingOrder.dismiss();

                                        mDialog = new Dialog(getContext());
                                        mDialog.setContentView(R.layout.login_alert_dialog);
                                        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                                        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        mDialog.setCancelable(false);
                                        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
                                        mDialog.getWindow().setGravity(Gravity.CENTER);

                                        mDialog.show();

                                        mDialog.findViewById(R.id.can_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDialog.dismiss();

                                            }
                                        });

                                        mDialog.findViewById(R.id.log_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }else {

                                    Toast.makeText(getContext(), "Please select Mode Of Payment", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        return mView;
    }


    public void countDown(){


        DatabaseReference ref34 = FirebaseDatabase.getInstance("https://clovv-e763d-default-rtdb.firebaseio.com/")
                .getReference().child("Orders").child(OrdersId);

        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                txtTimeData.setText(""+String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes( counter*1000),
                        TimeUnit.MILLISECONDS.toSeconds(counter*1000) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter*1000))));

                counter--;

                pBar.setProgress(counter);
                isTimerRuningPlace = true;

            }
            @Override
            public void onFinish() {
                //orderexpire set
                timerDialog.dismiss();

                //set cancelled
                if (timer!=null){
                    timer.cancel();
                    timer=null;
                }

                ref34.child("orderExpired").setValue(true);
                ref34.child("canceled").setValue(true);

                isTimerRuningPlace=false;


            }
        }.start();
    }


    public void calculation(){

        sum = 0;
        Cursor res = helper.getData();
        if (res.getCount()==0){
            Toast.makeText(getContext(), "no item in your cart!", Toast.LENGTH_SHORT).show();
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);

        }else {
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.VISIBLE);
            progressDialog.show();
            int productNameIndex = res.getColumnIndex("productName");
            int productPricing = res.getColumnIndex("productPrice");
            int idIndex = res.getColumnIndex("shopIdData");
            int quantityIndex = res.getColumnIndex("qValData");
            int userWtIndex = res.getColumnIndex("userWtType");
            int productTypeIndex = res.getColumnIndex("productType");
            int productUnitIndex = res.getColumnIndex("productUnitPrice");
            int shopPerWtTypeIndex = res.getColumnIndex("shopPerWtType");
            int productId = res.getColumnIndex("productId");
            int productStockWtTypeIndex = res.getColumnIndex("productStockWtType");
            int productTotalStockIndex = res.getColumnIndex("totalStock");

            while (res.moveToNext()){
                id = res.getString(idIndex);
                String proName = res.getString(productNameIndex);
                String proPrice = res.getString(productPricing);
                String quantity = res.getString(quantityIndex);
                String userWeight = res.getString(userWtIndex);
                String proType = res.getString(productTypeIndex);
                String proUnitPrice = res.getString(productUnitIndex);
                String shopPerWtType = res.getString(shopPerWtTypeIndex);
                String proId = res.getString(productId);
                String proWtType = res.getString(productStockWtTypeIndex);

                double pData = Double.parseDouble(proPrice);

                sum = sum+pData;


                firebaseFirestore.collection("Products").document(proId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().exists()){
                                    String proTotalStock = task.getResult().getString("productItemAvailable");

                                    SqLiteModalCart sqLiteModalCart = new SqLiteModalCart(proName,proPrice,quantity,
                                            userWeight,proType,proUnitPrice,shopPerWtType,proId,proWtType,proTotalStock);
                                    listCart.add(sqLiteModalCart);
                                    size.setText("Total item : "+String.valueOf(listCart.size()));

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

                adapter.notifyDataSetChanged();







            }


            String sums = String.format("%.2f", sum);

            subtotal.setText("Subtotal : "+getContext().getResources().getString(R.string.rs)+ sums);

            sizeData = String.valueOf(res.getCount());

            String s1 = sharedPreferences.getString("distanceValue", "");

            txtCharges.setText(s1);

            double disVal = Double.parseDouble(s1);


            //check distance
            reference.child("shopusers").child(id).child("deliveryCharge")
                    .child("case1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String disCase1 = snapshot.child("deliveryDistance").getValue().toString();
                                String amountCase1 = snapshot.child("orderPrice").getValue().toString();
                                if (disVal>Integer.valueOf(disCase1) && sum>Integer.valueOf(amountCase1)){

                                    reference.child("shopusers").child(id).child("deliveryCharge").child("case1").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+delPrice);
                                                totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+Integer.valueOf(delPrice))));
                                                totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                }else{
                                    reference.child("shopusers").child(id).child("deliveryCharge")
                                            .child("case2").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        String disCase2 = snapshot.child("deliveryDistance").getValue().toString();
                                                        String amountCase2 = snapshot.child("orderPrice").getValue().toString();

                                                        if (disVal>Integer.valueOf(disCase2) && sum<Integer.valueOf(amountCase2)){

                                                            reference.child("shopusers").child(id).child("deliveryCharge").child("case2").addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()){
                                                                        String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                                        double updateDeliveryCharge1 = Double.parseDouble(String.format("%.2f",Integer.parseInt(delPrice)*disVal));
                                                                        txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+updateDeliveryCharge1);
                                                                        totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+updateDeliveryCharge1)));
                                                                        totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                        totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });


                                                            progressDialog.dismiss();

                                                        }else{
                                                            reference.child("shopusers").child(id).child("deliveryCharge")
                                                                    .child("case3").addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if (snapshot.exists()){

                                                                                String disCase2 = snapshot.child("deliveryDistance").getValue().toString();
                                                                                String amountCase2 = snapshot.child("orderPrice").getValue().toString();

                                                                                if (disVal<Integer.valueOf(disCase2) && sum>Integer.valueOf(amountCase2)){

                                                                                    reference.child("shopusers").child(id).child("deliveryCharge").child("case3").addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            if (snapshot.exists()){
                                                                                                String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                                                                txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+delPrice);
                                                                                                totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+Integer.valueOf(delPrice))));
                                                                                                totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                progressDialog.dismiss();
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });


                                                                                    progressDialog.dismiss();
                                                                                }else {
                                                                                    reference.child("shopusers").child(id).child("deliveryCharge")
                                                                                            .child("case4").addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    if (snapshot.exists()){

                                                                                                        String disCase2 = snapshot.child("deliveryDistance").getValue().toString();
                                                                                                        String amountCase2 = snapshot.child("orderPrice").getValue().toString();

                                                                                                        if (disVal<Integer.valueOf(disCase2) && sum<Integer.valueOf(amountCase2)){
                                                                                                            reference.child("shopusers").child(id).child("deliveryCharge").child("case4").addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                    if (snapshot.exists()){
                                                                                                                        String delPrice = snapshot.child("deliveryCharge").getValue().toString();
                                                                                                                        txtCharges.setText("Delivery charge : "+getContext().getResources().getString(R.string.rs)+delPrice);
                                                                                                                        totalPriceData = Double.parseDouble(String.format("%.2f", (Double.parseDouble(sums)+Integer.valueOf(delPrice))));
                                                                                                                        totalPrice.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                                        totalPrice2.setText("Total : " +getContext().getResources().getString(R.string.rs)+ totalPriceData);
                                                                                                                        progressDialog.dismiss();
                                                                                                                    }
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                }
                                                                                                            });

                                                                                                            progressDialog.dismiss();

                                                                                                        }

                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                }

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });


                                                        }


                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });







        }
    }


    public void getDataIdName(String idy){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://clovv-e763d-default-rtdb.firebaseio.com/")
                .getReference().child("shopusers").child(idy);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("shop_name").getValue().toString();
                String shopLat = snapshot.child("latitude_shop").getValue().toString();
                String shopLog = snapshot.child("longitude_shop").getValue().toString();
                shopName.setText(name);

                double sLat = Double.parseDouble(shopLat);
                double sLng = Double.parseDouble(shopLog);

                distance(latitude,longitude,sLat,sLng);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
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
        myEdit.putString("distanceValue", String.valueOf(dist));
        myEdit.commit();


        if (dist<1){
            dist = dist*1000;
            int distance = (int) dist;
            distu = distance+" m";
            distData.setText(distance+" m");





        }else {
            int distance = (int) dist;
            distu = distance+" KM";
            distData.setText(distu);



        }

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }





}