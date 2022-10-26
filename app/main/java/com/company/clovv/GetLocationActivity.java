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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mGoogleMap;

    private EditText search_location1;
    LinearLayout btn_current;

    private TextView add_line,local_line;
    private Button set_location;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    double lat,lng;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        search_location1 = findViewById(R.id.search_location);
        btn_current = findViewById(R.id.current_btn_location);
        add_line = findViewById(R.id.add_line);
        local_line = findViewById(R.id.locality_text);
        set_location = findViewById(R.id.set_delivery_location);

        progressDialog = new ProgressDialog(GetLocationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Finding your current delivery location...");

        progressDialog.show();

        sharedPreferences = getSharedPreferences("usersDeliveryLocation", Context.MODE_PRIVATE);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);


        supportMapFragment.getMapAsync(GetLocationActivity.this);


        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get location

                SharedPreferences.Editor editor = sharedPreferences.edit();

                String LocalityData = local_line.getText().toString();
                String AddressData = add_line.getText().toString();

                editor.putString("Locality",LocalityData);
                editor.putString("Address",AddressData);
                editor.putString("lati_tude",String.valueOf(lat));
                editor.putString("longi_tude",String.valueOf(lng));
                editor.commit();
                Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(GetLocationActivity.this,Dashboard.class);
                startActivity(intent);
                finish();

            }
        });





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

        Geocoder geocoder = new Geocoder(GetLocationActivity.this);
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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GetLocationActivity.this);

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
                            Geocoder geo = new Geocoder(GetLocationActivity.this, Locale.getDefault());
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
                            Toast.makeText(getApplicationContext(), "Check your location service is on or off", Toast.LENGTH_SHORT).show();
                        }





                    }else {
                        Toast.makeText(getApplicationContext(), "Unable to find location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
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