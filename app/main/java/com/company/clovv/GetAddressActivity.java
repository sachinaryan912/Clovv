package com.company.clovv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GetAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    private EditText search_location1;
    LinearLayout btn_current;
    private TextView add_line,local_line;
    private Button set_location;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    double lat,lng;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        search_location1 = findViewById(R.id.search_location);
        btn_current = findViewById(R.id.current_btn_location);
        add_line = findViewById(R.id.add_line);
        local_line = findViewById(R.id.locality_text);
        set_location = findViewById(R.id.set_delivery_location);

        progressDialog = new ProgressDialog(GetAddressActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Finding your current delivery location...");

        progressDialog.show();




        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);


        supportMapFragment.getMapAsync(GetAddressActivity.this);


        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get location

//
                String LocalityData = local_line.getText().toString();
                String AddressData = add_line.getText().toString();


                setDataSqlite(AddressData,LocalityData,lng,lat);

                Intent intent = new Intent(GetAddressActivity.this,AddressBookActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }
    public void setDataSqlite(String address,String locality,double longitude,double latitude){

        String ids = String.valueOf(System.currentTimeMillis());

        //add
        HashMap<String, Object> map = new HashMap<>();
        map.put("address",address);
        map.put("locality",locality);
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("id",mUser.getUid());
        map.put("key",ids);

        firebaseFirestore.collection("address")
                .document(ids)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GetAddressActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });




//        Cursor res = sqLocDBHelper.getData();
//        if (res.getCount()==0){
//            Boolean insertData = sqLocDBHelper.insertAddressData(address,locality,longitude,latitude);
//            if (insertData){
//                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//
//            }else {
//                Toast.makeText(getApplicationContext(), "Failed to add address. TRY AGAIN", Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            int idIndex = res.getColumnIndex("address");
//            while (res.moveToNext()){
//                String id = res.getString(idIndex);
//                if (id.equals(address)){
//                    Toast.makeText(getApplicationContext(), "This address already present in your cart", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Boolean insertData = sqLocDBHelper.insertAddressData(address,locality,longitude,latitude);
//                    if (insertData){
//                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//
//                    }else {
//                        Toast.makeText(getApplicationContext(), "Failed to add address. TRY AGAIN", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
//
//
//        }


        //end of storing data in sqlite

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;

        getDeviceLocation();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        init();



    }

    private void init(){
        search_location1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){

                    geoLocate();

                }


                return false;
            }
        });

        btn_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //current location
                getDeviceLocation();

            }
        });
    }

    private void geoLocate() {

        String searchString = search_location1.getText().toString();

        Geocoder geocoder = new Geocoder(GetAddressActivity.this);
        List<Address> list = new ArrayList<>();

        try {

            list = geocoder.getFromLocationName(searchString, 1);


        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (list.size()>0){
            Address address = list.get(0);
//
//            Toast.makeText(getApplicationContext(), "Address: "+address.toString(), Toast.LENGTH_LONG).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),15f, address.getAddressLine(0));

            add_line.setText(address.getAddressLine(0));
            local_line.setText(address.getLocality());
            lat = address.getLatitude();
            lng = address.getLongitude();
        }

    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GetAddressActivity.this);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){


                        Location currentLocation = (Location) task.getResult();

                        if (currentLocation!=null){
                            Geocoder geo = new Geocoder(GetAddressActivity.this, Locale.getDefault());
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f,"Your delivery Location");
                            try {
                                List<Address> addressList = geo.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                                String add1 = addressList.get(0).getAddressLine(0);
                                String local1 = addressList.get(0).getLocality();
                                add_line.setText(add1);
                                local_line.setText(local1);
                                lat = currentLocation.getLatitude();
                                lng = currentLocation.getLongitude();
                                progressDialog.dismiss();


                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), "Check your location service is on or off", Toast.LENGTH_SHORT).show();
                        }





                    }else {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), "Unable to find location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
            progressDialog.cancel();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void moveCamera(LatLng latLng, float zoom,String title){
//        Toast.makeText(getApplicationContext(), "moving camera to : lat:"+latLng.latitude + ", lng: "+latLng.longitude, Toast.LENGTH_SHORT).show();

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions markerOptions = new MarkerOptions()
                .title(title)
                .position(latLng);

        mGoogleMap.addMarker(markerOptions);



    }

}