package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.company.clovv.adapter.ProductListAdapter;
import com.company.clovv.modal.ProductListModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopProductsActivity extends AppCompatActivity {

    ImageView backBtn,searchBtn;
    TextView name,locality,distance;
    RecyclerView recyclerView;

    FirebaseFirestore firebaseFirestore;
    ArrayList<ProductListModal> listProducts;
    ProductListAdapter productListAdapter;

    DatabaseReference dbRef;

    DBHelper helper;
    LinearLayout productSnackBar;
    TextView itemTxt,viewCartBtn,status;
    EditText searchVal;
    boolean isSearchVisible = false;
    ImageView imgData;

    LottieAnimationView lotShopFav;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String shopIds;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_products);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseFirestore = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.back_btn);
        name = findViewById(R.id.shop_name_data);
        locality = findViewById(R.id.shop_locality_data);
        recyclerView = findViewById(R.id.products_list_recycle);
        searchVal = findViewById(R.id.searchProductShop);
        searchBtn = findViewById(R.id.searchBtnShopIcon);
        distance = findViewById(R.id.shopDistance);
        status = findViewById(R.id.shopStatus);
        imgData = findViewById(R.id.shopImageData);
        lotShopFav = findViewById(R.id.lot_fav2);

        productSnackBar = findViewById(R.id.productSnackBar);
        itemTxt = findViewById(R.id.itemDataSnack);
        viewCartBtn = findViewById(R.id.viewBtnSnack);
        shopIds = getIntent().getStringExtra("shopId");
        String shopDistance = getIntent().getStringExtra("shopDistance");

        distance.setText(shopDistance);

        helper = new DBHelper(this);



        listProducts = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        productListAdapter = new ProductListAdapter(getApplicationContext(),listProducts);

        recyclerView.setAdapter(productListAdapter);





        dbRef = FirebaseDatabase.getInstance().getReference().child("shopusers").child(shopIds);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nameData = snapshot.child("shop_name").getValue().toString();
                    String localityData = snapshot.child("locality_shop").getValue().toString();
                    boolean openStatus = Boolean.parseBoolean(snapshot.child("shopOpen").getValue().toString());

                    if (snapshot.child("profileImageUrl").exists()){
                        String image = snapshot.child("profileImageUrl").getValue().toString();
                        Picasso.get().load(image).into(imgData);
                    }

                    if (openStatus){
                        status.setText("Closed");
                        status.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                    }else {
                        status.setText("Open");
                        status.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }
                    name.setText(nameData);
                    locality.setText(localityData);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearchVisible){
                    searchVal.setVisibility(View.VISIBLE);
                    isSearchVisible = true;
                }else {
                    searchVal.setVisibility(View.GONE);
                    isSearchVisible = false;
                }

            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            firebaseFirestore.collection("favShops")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if (document.getData().containsKey(shopIds)){
                                        lotShopFav.setMinAndMaxProgress(0.5f,1.0f);
                                    }else {
                                        lotShopFav.setMinAndMaxProgress(0.0f,0.5f);
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });






        }


        lotShopFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set fav
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();


                    try{



                        firebaseFirestore.collection("favShops")
                                .document(currentUser.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()){
                                                if (document.getData().containsKey(shopIds)){
                                                    Toast.makeText(ShopProductsActivity.this, "exists"+shopIds, Toast.LENGTH_SHORT).show();

                                                    //remove
                                                    // Remove the 'capital' field from the document
                                                    Map<String,Object> updates = new HashMap<>();
                                                    updates.put(shopIds, FieldValue.delete());

                                                    firebaseFirestore.collection("favShops")
                                                            .document(currentUser.getUid())
                                                            .update(updates)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        lotShopFav.playAnimation();
                                                                        lotShopFav.setMinAndMaxProgress(0.5f,1.0f);

                                                                        Toast.makeText(getApplicationContext(), "removed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                }else{
                                                    Toast.makeText(ShopProductsActivity.this, "not exists hello", Toast.LENGTH_SHORT).show();


                                                    HashMap<String, Boolean> map1 = new HashMap<>();
                                                    map1.put(shopIds, true);

                                                    firebaseFirestore.collection("favShops")
                                                            .document(currentUser.getUid())
                                                            .set(map1, SetOptions.merge())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        lotShopFav.playAnimation();
                                                                        lotShopFav.setMinAndMaxProgress(0.0f,0.5f);
                                                                        Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                                                                    }else {

                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }else {

                                                HashMap<String, Boolean> map1 = new HashMap<>();
                                                map1.put(shopIds, true);

                                                firebaseFirestore.collection("favShops")
                                                        .document(currentUser.getUid())
                                                        .set(map1, SetOptions.merge())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    lotShopFav.playAnimation();
                                                                    lotShopFav.setMinAndMaxProgress(0.0f,0.5f);
                                                                    Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                                                                }else {

                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                            }
                                        }
                                    }
                                });



                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }









                }else {
                    Toast.makeText(getApplicationContext(), "please login to add favourite", Toast.LENGTH_SHORT).show();
                }




            }
        });


        getProducts(shopIds);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopProductsActivity.this,Dashboard.class));
                finish();
            }
        });

        //popup window
        int count = 0;
        Cursor res = helper.getData();
        count = res.getCount();

        if (count!=0) {

            productSnackBar.setVisibility(View.GONE);
            itemTxt.setText(count+" item in your cart");

            viewCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(ShopProductsActivity.this,CartActivity.class));
                }
            });
        }else{
            productSnackBar.setVisibility(View.GONE);
        }

    }

    private void getProducts(String shopIds) {

        firebaseFirestore.collection("Products").whereEqualTo("productOwnerId",shopIds)
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

                                        listProducts.add(dc.getDocument().toObject(ProductListModal.class));

                                    }
                                    productListAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }




                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShopProductsActivity.this,Dashboard.class));
        finish();
    }
}