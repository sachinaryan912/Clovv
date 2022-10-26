package com.company.clovv.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.company.clovv.R;
import com.company.clovv.adapter.FavProductAdapter;
import com.company.clovv.adapter.ProductListAdapter;
import com.company.clovv.modal.FavProductModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class FavProductFragment extends Fragment {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<String> listIds;
    RecyclerView recyclerView;
    FavProductAdapter favProductAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav_product, container, false);

        recyclerView = v.findViewById(R.id.favProductsRecView);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        listIds = new ArrayList<>();

        getFavProducts();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        favProductAdapter = new FavProductAdapter(getContext(),listIds);

        recyclerView.setAdapter(favProductAdapter);



        return v;
    }

    public void getFavProducts(){

        firebaseFirestore.collection("favProducts")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String[] strings = documentSnapshot.getData().values().toArray(new String[0]);
                            for (int i=0;i<documentSnapshot.getData().values().size();i++){
                                String val = strings[i];

                                listIds.add(val);
                            }
                            favProductAdapter.notifyDataSetChanged();

                        }
                    }
                });

    }
}