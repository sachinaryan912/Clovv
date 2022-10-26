package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.company.clovv.fragment.CartFragment;
import com.company.clovv.fragment.HomeFragment;
import com.company.clovv.fragment.LoginProfileFragment;
import com.company.clovv.fragment.OrderHistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Context context;
    LottieAnimationView lot1;
    RelativeLayout r1,r2,bLay;
    Button btn1;
    DBHelper helper;

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    TextView txtMsg;
    Dialog clovvLoaderDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        lot1 = findViewById(R.id.lot_no_internet);
        r1 = findViewById(R.id.no_internet_lay);
        r2 = findViewById(R.id.main_lay);
        bLay = findViewById(R.id.blocked_lay);
        btn1 = findViewById(R.id.retry_btn);
        txtMsg = findViewById(R.id.messageTxt);


        clovvLoaderDialog = new Dialog(Dashboard.this);
        clovvLoaderDialog.setContentView(R.layout.clovv_loader_dialog);
        clovvLoaderDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        clovvLoaderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        clovvLoaderDialog.setCancelable(false);
        clovvLoaderDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        clovvLoaderDialog.getWindow().setGravity(Gravity.CENTER);








        //get size

        bLay.setVisibility(View.GONE);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.dash_board);



        if (!isConnected()){
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            lot1.playAnimation();
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                    finish();
                }
            });
            Toast.makeText(getApplicationContext(), "No internet access", Toast.LENGTH_SHORT).show();
        }else {
            r1.setVisibility(View.GONE);

            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                LottieAnimationView lottieAnimationView;
                lottieAnimationView = clovvLoaderDialog.findViewById(R.id.clov_loader_view);





                clovvLoaderDialog.show();
                lottieAnimationView.playAnimation();
                lottieAnimationView.loop(true);
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("customers").child(mUser.getUid());



                //check status account
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            boolean isAccountActive = Boolean.parseBoolean(snapshot.child("active").getValue().toString());
                            String blockReason = snapshot.child("blockReason").getValue().toString();
                            if (!isAccountActive){


                                txtMsg.setText(blockReason);

                                bLay.setVisibility(View.VISIBLE);
                                r2.setVisibility(View.GONE);
                                clovvLoaderDialog.dismiss();

                            }

                            if (isAccountActive){
                                clovvLoaderDialog.dismiss();
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                bLay.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);


                //set fragment
                ListFragment fragment = new ListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_lay,fragment);
                fragmentTransaction.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new HomeFragment()).commit();

                bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.dash_board:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new HomeFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                                }
                                return true;


                            case R.id.cart:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new CartFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));
                                }
                                return true;

                            case R.id.history:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new OrderHistoryFragment()).commit();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));
                                }
                                return true;

                            case R.id.sett:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new LoginProfileFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));
                                }
                                return true;

                        }

                        return false;
                    }
                });


            }else{
                bLay.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                if (clovvLoaderDialog.isShowing()){
                    clovvLoaderDialog.dismiss();
                }


                //set fragment
                ListFragment fragment = new ListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_lay,fragment);
                fragmentTransaction.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new HomeFragment()).commit();

                bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.dash_board:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new HomeFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                                }
                                return true;


                            case R.id.cart:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new CartFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));
                                }
                                return true;

                            case R.id.history:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new OrderHistoryFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));
                                }
                                return true;

                            case R.id.sett:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay,new LoginProfileFragment()).commit();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));
                                }
                                return true;

                        }

                        return false;
                    }
                });

            }








        }













    }






    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected()){

        }else {

        }
    }



    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}