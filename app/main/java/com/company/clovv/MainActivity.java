package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.company.clovv.adapter.AdapterWalkthrough;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    Button set_location_btn;

    public ViewPager viewPager;
    AdapterWalkthrough adapterWalkthrough;

    public static final int PERMISSION_REQUEST_CODE = 9001;
    private final int PLAY_SERVICE_ERROR_CODE = 9002;
    private boolean mLocationPermissionGranted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        viewPager = findViewById(R.id.view_pager1);
        CircleIndicator indicator = findViewById(R.id.indicator);
        set_location_btn = findViewById(R.id.set_delivery_location);

        adapterWalkthrough = new AdapterWalkthrough(getSupportFragmentManager());
        viewPager.setAdapter(adapterWalkthrough);

        indicator.setViewPager(viewPager);

        adapterWalkthrough.registerDataSetObserver(indicator.getDataSetObserver());

        SharedPreferences sp = getApplicationContext().getSharedPreferences("usersDeliveryLocation", Context.MODE_PRIVATE);
        String local = sp.getString("Locality","");
        String add = sp.getString("Address","");

        if (!local.isEmpty()){
            startActivity(new Intent(MainActivity.this,Dashboard.class));
            finish();
        }

//        initGoogleMap();


        //walkthrough end

        set_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_DENIED){

                        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};

                        requestPermissions(permission,PERMISSION_REQUEST_CODE);

                    }
                    else {
                        startActivity(new Intent(MainActivity.this,GetLocationActivity.class));

                    }
                }



//                //location activity
//                if (mLocationPermissionGranted){
//
//                    startActivity(new Intent(MainActivity.this,GetLocationActivity.class));
//                    Toast.makeText(getApplicationContext(), "Ready to map!", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(getApplicationContext(), "hey there", Toast.LENGTH_SHORT).show();
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
//                        }
//                    }
//                }


            }
        });


        //activity result


    }

    public void initGoogleMap() {
//
//        if (isServiceOk()){

            if (mLocationPermissionGranted){
                Toast.makeText(getApplicationContext(), "Ready to map!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,GetLocationActivity.class));

            }else {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                    }
                }
            }

        }


//    public boolean isServiceOk() {
//
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//        int result = googleApiAvailability.isGooglePlayServicesAvailable(MainActivity.this);
//
//        if (result == ConnectionResult.SUCCESS){
//            return true;
//        }else if (googleApiAvailability.isUserResolvableError(result)){
//            Dialog dialog = googleApiAvailability.getErrorDialog(MainActivity.this,result,PLAY_SERVICE_ERROR_CODE,task->
//                    Toast.makeText(getApplicationContext(), "Dialog is cancelled by user", Toast.LENGTH_SHORT).show());
//            dialog.show();
//        }else {
//            Toast.makeText(getApplicationContext(), "Play services are required by this application", Toast.LENGTH_SHORT).show();
//        }
//
//        return false;
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted=true;
            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else {
            mLocationPermissionGranted=false;
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}