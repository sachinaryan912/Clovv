package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.clovv.adapter.SearchProductAdapter;
import com.company.clovv.adapter.SearchShopAdapter;
import com.company.clovv.modal.ProductListModal;
import com.company.clovv.modal.SearchProductModal;
import com.company.clovv.modal.SearchShopModal;
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

import java.util.ArrayList;

public class SearchProducts extends AppCompatActivity {

    ImageView btn_back;
    RecyclerView recyclerView,recyclerView2;
    EditText editText;
    FirebaseFirestore firebaseFirestore;
    ArrayList<SearchProductModal> searchProductModalArrayList;
    SearchProductAdapter searchProductAdapter;
    ArrayList<SearchShopModal> shopNameList;
    SearchShopAdapter searchShopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        btn_back = findViewById(R.id.btn_back);
        editText = findViewById(R.id.search_text);
        recyclerView = findViewById(R.id.rec_search_product);
        recyclerView2 = findViewById(R.id.rec_search_shop);

        searchProductModalArrayList = new ArrayList<>();
        shopNameList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchProducts.this,Dashboard.class));
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchProductAdapter = new SearchProductAdapter(getApplicationContext(),searchProductModalArrayList);
        recyclerView.setAdapter(searchProductAdapter);

        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchShopAdapter = new SearchShopAdapter(getApplicationContext(),shopNameList);
        recyclerView2.setAdapter(searchShopAdapter);





        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProductModalArrayList.clear();
                shopNameList.clear();

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length()==0){
                    searchProductModalArrayList.clear();
                    shopNameList.clear();
                }else {
                    searchRealtime(s.toString().toLowerCase());

                    searchProducts(s.toString().toLowerCase());
                    searchProductAdapter.notifyDataSetChanged();
                    searchShopAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void searchRealtime(CharSequence charSequence){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("shopusers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String shopName = dataSnapshot.child("shop_name").getValue().toString().toLowerCase();

                        if (shopName.contains(charSequence)){
                            SearchShopModal searchShopModal = dataSnapshot.getValue(SearchShopModal.class);
                            shopNameList.add(searchShopModal);

                        }

                    }

                    searchShopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchProducts(CharSequence s) {

        firebaseFirestore.collection("Products")
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

                                            String data = dc.getDocument().getString("productName").toLowerCase();
                                            String desc = dc.getDocument().getString("productDescription").toLowerCase();
                                            if (!s.equals(" ")){
                                                if (data.contains(s) || desc.contains(s)){
                                                    searchProductModalArrayList.add(dc.getDocument().toObject(SearchProductModal.class));
                                                    searchProductAdapter.notifyDataSetChanged();
                                                }
                                            }

//



                                        }

                                    }else {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        }





                });


    }

    private void getProducts() {

        firebaseFirestore.collection("Products")
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

                                        searchProductModalArrayList.add(dc.getDocument().toObject(SearchProductModal.class));

//                                        if (dc != null){
//                                            String qunt = dc.getDocument().getString("productItemAvailable");

//                                        }




                                    }
                                    searchProductAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }




                    }
                });

    }
}