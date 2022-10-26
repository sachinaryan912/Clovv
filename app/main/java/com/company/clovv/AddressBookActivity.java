package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.clovv.adapter.AddressBookAdapter;
import com.company.clovv.adapter.ProductListAdapter;
import com.company.clovv.modal.AddressBookModel;
import com.company.clovv.modal.ProductListModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddressBookActivity extends AppCompatActivity {
    ImageView img_btn;
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    ArrayList<AddressBookModel> listAddress;
    AddressBookAdapter addressBookAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        img_btn = findViewById(R.id.add_address_btn);
        recyclerView = findViewById(R.id.add_rec_view);
        builder = new AlertDialog.Builder(this);
        listAddress = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        addressBookAdapter = new AddressBookAdapter(getApplicationContext(),listAddress);

        recyclerView.setAdapter(addressBookAdapter);

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check location is on or not

                LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {


                    //Setting message manually and performing action on button click
                    builder.setMessage("Kindly Turn on the location")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Location access");
                    alert.show();


                }else {
                    Intent intent = new Intent(AddressBookActivity.this,GetAddressActivity.class);
                    startActivity(intent);
                }

            }
        });

        firebaseFirestore.collection("address").whereEqualTo("id",mUser.getUid())
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

                                        String add = dc.getDocument().getString("address");
                                        double lat = dc.getDocument().getDouble("latitude");
                                        String id = dc.getDocument().getString("id");
                                        String loc = dc.getDocument().getString("locality");
                                        double log = dc.getDocument().getDouble("longitude");

                                        AddressBookModel addressBookModel = new AddressBookModel(add,id,loc,lat,log);
                                        listAddress.add(addressBookModel);

                                    }
                                    addressBookAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }




                    }
                });

        //get data
//        Cursor res = sqLocDBHelper.getData();
//        if (res.getCount()==0){
//            Toast.makeText(this, "no address available", Toast.LENGTH_SHORT).show();
//        }else {
//            int addressIndex = res.getColumnIndex("address");
//            int localityIndex = res.getColumnIndex("locality");
//            int longitudeIndex = res.getColumnIndex("longitude");
//            int latitudeIndex = res.getColumnIndex("latitude");
//
//            while (res.moveToNext()){
//                String address = res.getString(addressIndex);
//                String locality = res.getString(localityIndex);
//                String longitude = res.getString(longitudeIndex);
//                String latitude = res.getString(latitudeIndex);
//
//                AddressBookModel addressBookModel = new AddressBookModel(address,locality,longitude,latitude);
//                listAddress.add(addressBookModel);
//
//
//            }
//
//            addressBookAdapter.notifyDataSetChanged();
//        }




    }
}